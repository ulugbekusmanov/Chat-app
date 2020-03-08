package ru.hiday.chatapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hiday.chatapp.R;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.input_email_login)
    EditText inputEmailLogin;
    @BindView(R.id.input_password_login)
    EditText inputPasswordLogin;
    @BindView(R.id.btn_login_activity)
    Button btn_login;
    FirebaseAuth auth;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Вход");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEamilL = inputEmailLogin.getText().toString().trim();
                String userPasswordL = inputPasswordLogin.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(userEamilL).matches()) {
                    inputEmailLogin.setError("Неверный адрес электронной почты");
                    inputEmailLogin.setFocusable(true);


                } else if (userPasswordL.length() < 6) {
                    inputPasswordLogin.setError("\n" +
                            "Пароль не менее 6 символов");
                    inputPasswordLogin.setFocusable(true);

                } else {
                    auth.signInWithEmailAndPassword(userEamilL, userPasswordL)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();

                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    inputPasswordLogin.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    inputEmailLogin.onEditorAction(EditorInfo.IME_ACTION_DONE);

                }

            }
        });
    }
}

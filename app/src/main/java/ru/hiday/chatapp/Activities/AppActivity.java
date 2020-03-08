package ru.hiday.chatapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hiday.chatapp.R;

public class AppActivity extends AppCompatActivity {
    @BindView(R.id.registor)
    Button registorr;
    @BindView(R.id.login)
    Button loginn;
    FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            startActivity(new Intent(AppActivity.this, MainActivity.class));
            finish();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        ButterKnife.bind(this);
        user = FirebaseAuth.getInstance().getCurrentUser();

        loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppActivity.this, LoginActivity.class));
            }
        });
        registorr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppActivity.this, RgistorActivity.class));

            }
        });
    }
}

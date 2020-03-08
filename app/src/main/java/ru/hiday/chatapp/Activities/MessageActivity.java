package ru.hiday.chatapp.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ru.hiday.chatapp.Adapter.MessageAdapter;
import ru.hiday.chatapp.R;
import ru.hiday.chatapp.models.Chat;
import ru.hiday.chatapp.models.User;

public class MessageActivity extends AppCompatActivity {
    @BindView(R.id.profile_image)
    CircleImageView profile_image;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.text_send)
    EditText et_send_message;
    @BindView(R.id.btn_send)
    ImageButton ib_send_message;
    @BindView(R.id.recycler_view_messenge)
    RecyclerView recyclerView;
    List<Chat> mchat;
    MessageAdapter messageAdapter;
    public static final String  TAG = "MessageActivity";
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        intent = getIntent();
        final String userId = intent.getStringExtra("userid");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext());
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);


        ib_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_send_message.getText().toString().trim();
                if (!msg.equals("")) {
                    sendMessage(firebaseUser.getUid(), userId, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "Вы не можете отправить пустой собщения!", Toast.LENGTH_SHORT).show();
                }
                et_send_message.setText("");
                et_send_message.onEditorAction(EditorInfo.IME_ACTION_DONE);


            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.drawable.ic_action_person_white);

                } else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);

                }
                readMessages(firebaseUser.getUid(),userId,user.getImageURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MessageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void sendMessage(String sender, String recever, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("recever", recever);
        hashMap.put("message", message);
        reference.child("Chats").push().setValue(hashMap);


    }
    private void readMessages(final String myid, final String userid, final String imageurl) {
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mchat.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chats = snapshot.getValue(Chat.class);
                        if (chats.getReceiver().equals(myid) && chats.getSender().equals(userid) || chats.getReceiver().equals(userid) && chats.getSender().equals(myid)) {
                            mchat.add(chats);


                        }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat,imageurl);
                    recyclerView.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

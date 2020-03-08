package ru.hiday.chatapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ru.hiday.chatapp.Adapter.TabAdapter;
import ru.hiday.chatapp.Fragments.ChatsFragment;
import ru.hiday.chatapp.Fragments.UsersFragment;
import ru.hiday.chatapp.R;
import ru.hiday.chatapp.models.User;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.profile_image)
    CircleImageView profile_image;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    DatabaseReference reference;
    FirebaseUser user;
    private TabAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        ButterKnife.bind(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new ChatsFragment(), "Чат");
        adapter.addFragment(new UsersFragment(), "Пользователи");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.drawable.ic_action_person_white);

                } else {
                    Glide.with(MainActivity.this).load(user.getImageURL()).into(profile_image);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, AppActivity.class));
                finish();
                return true;






        }
        return false;
    }



}

package ru.hiday.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.hiday.chatapp.Activities.MessageActivity;
import ru.hiday.chatapp.R;
import ru.hiday.chatapp.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> users;
    private Context context;
    public UserAdapter(Context context, List<User> list){
        this.users = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_items, viewGroup, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        final User user = users.get(position);
        viewHolder.usernamee.setText(user.getUsername());
        if (user.getImageURL().equals("default")){
            viewHolder.profile_imagee.setImageResource(R.drawable.ic_action_person);


        }else {
            Glide.with(context).load(user.getImageURL()).into(viewHolder.profile_imagee);

        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernamee;
        CircleImageView profile_imagee;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernamee = itemView.findViewById(R.id.username_item);
            profile_imagee = itemView.findViewById(R.id.profile_image);
        }
    }
}

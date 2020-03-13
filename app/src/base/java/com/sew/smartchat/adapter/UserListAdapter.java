package com.sew.smartchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sew.smartchat.R;
import com.sew.smartchat.database.UserDetail;
import com.sew.smartchat.model.ChatMessage;

import java.util.ArrayList;
import java.util.Random;

public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<UserDetail> allUsers;
    private int[] colorsList = new int[]{R.color.userColor1, R.color.userColor2,
                                            R.color.userColor3, R.color.userColor4, R.color.userColor5};
    private Random random;

    public UserListAdapter(Context context, ArrayList<UserDetail> allUsers) {
        this.context = context;
        this.allUsers = allUsers;
        random = new Random();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.user_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((UserViewHolder) holder).setUserDetails(allUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }


    class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCircularCharacter, txtUsername;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCircularCharacter = itemView.findViewById(R.id.txtCircularCharacter);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            int colorIndex = random.nextInt(colorsList.length);
            txtCircularCharacter.setBackgroundTintList(context.getColorStateList(colorsList[colorIndex]));
        }

        void setUserDetails(UserDetail currentUserDetail) {
            String userName = currentUserDetail.getUserName();
            String firstChar;
            if (userName.length() == 0) {
                firstChar = "X";
            } else {
                firstChar = (userName.charAt(0) + "").toUpperCase();
            }
            txtCircularCharacter.setText(firstChar);
            txtUsername.setText(userName);
        }
    }

}

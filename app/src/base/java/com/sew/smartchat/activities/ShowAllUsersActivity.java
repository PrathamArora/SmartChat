package com.sew.smartchat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;

import com.sew.smartchat.R;
import com.sew.smartchat.adapter.UserListAdapter;
import com.sew.smartchat.database.RoomDBHelper;
import com.sew.smartchat.database.UserDetail;

import java.util.ArrayList;
import java.util.List;

public class ShowAllUsersActivity extends BaseActivity {

    RecyclerView rvAllUsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_users);

        rvAllUsersList = findViewById(R.id.rvAllUsersList);

        RoomDBHelper roomDBHelper = new RoomDBHelper(this);
        List<UserDetail> allUsersDB = roomDBHelper.getAllUsers();
        Log.d(TAG, allUsersDB.size() + "");

        UserListAdapter userListAdapter = new UserListAdapter(this, (ArrayList<UserDetail>) allUsersDB);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvAllUsersList.setLayoutManager(recyclerLayoutManager);
        rvAllUsersList.setAdapter(userListAdapter);
    }
}

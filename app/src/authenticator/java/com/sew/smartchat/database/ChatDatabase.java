package com.sew.smartchat.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MessageDetail.class, TopicDetail.class, UserDetail.class}, version = 1, exportSchema = false)
public abstract class ChatDatabase extends RoomDatabase{

    public abstract DaoAccess daoAccess();

}

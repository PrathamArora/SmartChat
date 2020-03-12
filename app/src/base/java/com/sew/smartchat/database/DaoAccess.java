package com.sew.smartchat.database;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.sew.smartchat.model.SavedMessageFormat;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    Long insertUser(UserDetail userDetail);

    @Insert
    Long insertTopic(TopicDetail topicDetail);

    @Insert
    Long insertMessage(MessageDetail messageDetail);

    @Query("select topicID from TopicDetail where topicName=:topicName")
    List<Integer> getTopicID(String topicName);

    @Query("select userName from UserDetail where userToken=:userToken")
    List<String> getUsername(String userToken);

    @Query("select MessageDetail.date, MessageDetail.time, MessageDetail.message, UserDetail.userToken, UserDetail.userName " +
            "from MessageDetail inner join UserDetail on MessageDetail.userToken = UserDetail.userToken")
    List<SavedMessageFormat> getAllMessages();

}

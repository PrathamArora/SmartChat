package com.sew.smartchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sew.smartchat.R;
import com.sew.smartchat.model.ChatMessage;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    public void setAllMessages(ArrayList<ChatMessage> allMessages) {
        this.allMessages = allMessages;
    }

    private ArrayList<ChatMessage> allMessages;
    private static int SELF_MESSAGE = 1;
    private static int OTHER_MESSAGE = 2;

    public ChatAdapter(Context context, ArrayList<ChatMessage> allMessages ){
        this.context = context;
        this.allMessages = allMessages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == SELF_MESSAGE){
            view = LayoutInflater.from(context).inflate(R.layout.own_message, parent, false);
            return new OwnMessageViewHolder(view);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.others_message, parent, false);
            return new OtherMessageViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(allMessages.get(position).isSelf()){
            return SELF_MESSAGE;
        }else{
            return OTHER_MESSAGE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if( getItemViewType(position) == SELF_MESSAGE){
            ((OwnMessageViewHolder) holder).setMessageDetails(allMessages.get(position));
        }else{
            ((OtherMessageViewHolder) holder).setMessageDetails(allMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return allMessages.size();
    }

    class OwnMessageViewHolder extends RecyclerView.ViewHolder{

        private TextView message_textview, date_textview, time_textview;

        OwnMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            message_textview = itemView.findViewById(R.id.message_text);
            date_textview = itemView.findViewById(R.id.date);
            time_textview = itemView.findViewById(R.id.time);
        }

        void setMessageDetails(ChatMessage chatMessage){
            message_textview.setText(chatMessage.getMessage());
            date_textview.setText(chatMessage.getDate());
            time_textview.setText(chatMessage.getTime());
        }
    }

    class OtherMessageViewHolder extends RecyclerView.ViewHolder{
        private TextView message_textview, username_textview, date_textview, time_textview;

        OtherMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            message_textview = itemView.findViewById(R.id.message_text);
            username_textview = itemView.findViewById(R.id.message_username);
            date_textview = itemView.findViewById(R.id.date);
            time_textview = itemView.findViewById(R.id.time);
        }

        void setMessageDetails(ChatMessage chatMessage){
            message_textview.setText(chatMessage.getMessage());
            username_textview.setText(chatMessage.getTitle());
            date_textview.setText(chatMessage.getDate());
            time_textview.setText(chatMessage.getTime());
        }
    }


}

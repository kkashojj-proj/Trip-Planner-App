package com.example.koushik.homework9;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by KOUSHIK on 21-04-2017.
 */

public class UserMessageList implements Serializable {
    ArrayList<String> messageIds;
    String userId;

    public UserMessageList() {
        messageIds=new ArrayList<>();
    }

    public void addMessagetoList(String s){
        this.messageIds.add(s);
    }

    public ArrayList<String> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(ArrayList<String> messageIds) {
        this.messageIds = messageIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

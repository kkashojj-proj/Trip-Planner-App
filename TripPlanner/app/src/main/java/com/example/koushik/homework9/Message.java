package com.example.koushik.homework9;

import java.io.Serializable;

/**
 * Created by HARSH on 21-04-2017.
 */

public class Message implements Serializable {
    String meg_key,sent_by,imgurl,message,tripId;
    Long time;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }





    public Message(String meg_key, String sent_by, String imgurl, String message, Long time) {
        this.meg_key = meg_key;
        this.sent_by = sent_by;
        this.imgurl = imgurl;
        this.message = message;
        this.time = time;
    }

    public Message() {

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMeg_key() {
        return meg_key;
    }

    public void setMeg_key(String meg_key) {
        this.meg_key = meg_key;
    }

    public String getSent_by() {
        return sent_by;
    }

    public void setSent_by(String sent_by) {
        this.sent_by = sent_by;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}

package com.example.koushik.homework9;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by KOUSHIK on 14-04-2017.
 */

public class Trip implements Serializable {
    String createdBy,tripId,title,location,imageUrl,chatroomId;
    ArrayList<String> members;
    ArrayList<TripLocation> locations;

    public void addLocation(TripLocation location){
        this.locations.add(location);
    }

    public ArrayList<TripLocation> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<TripLocation> locations) {
        this.locations = locations;
    }

    public Trip() {
        members=new ArrayList<>();
        locations=new ArrayList<>();
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public void addMember(String member) {
        this.members.add(member);
    }
}

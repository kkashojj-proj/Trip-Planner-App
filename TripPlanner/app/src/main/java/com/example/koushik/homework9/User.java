package com.example.koushik.homework9;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by KOUSHIK on 14-04-2017.
 */

public class User implements Serializable {
    String firstNmame;
    String lastName;
    String gender;
    String imageUrl;
    String email;
    String password;
    String id;
    ArrayList<String> friends;
    ArrayList<String> requestSent;
    ArrayList<String> requestReceived;
    ArrayList<String> trips;

    public void addMessage(String s){
        this.messages.add(s);
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    ArrayList<String> messages;

    public String getId() {
        return id;
    }

    public void addFriend(String string){
        if(!friends.contains(string)){
            friends.add(string);
        }
    }

    public void addRequestSent(String string){
        if(!requestSent.contains(string)){
            requestSent.add(string);
        }
    }

    public void addRequestReceived(String string){
        if(!requestReceived.contains(string)){
            requestReceived.add(string);
        }
    }

    public void addTrip(String string){
        if(!trips.contains(string)){
            trips.add(string);
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public User() {
        this.friends = new ArrayList<>();
        this.requestSent = new ArrayList<>();
        this.requestReceived = new ArrayList<>();
        this.trips = new ArrayList<>();
        this.messages=new ArrayList<>();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public ArrayList<String> getRequestSent() {
        return requestSent;
    }

    public void setRequestSent(ArrayList<String> requestSent) {
        this.requestSent = requestSent;
    }

    public ArrayList<String> getRequestReceived() {
        return requestReceived;
    }

    public void setRequestReceived(ArrayList<String> requestReceived) {
        this.requestReceived = requestReceived;
    }

    public ArrayList<String> getTrips() {
        return trips;
    }

    public void setTrips(ArrayList<String> trips) {
        this.trips = trips;
    }

    public String getFirstNmame() {
        return firstNmame;
    }

    public void setFirstNmame(String firstNmame) {
        this.firstNmame = firstNmame;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}

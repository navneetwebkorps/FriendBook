package com.friendbook1.entities;
import jakarta.persistence.Embeddable;

@Embeddable
public class Comment {
    private String userName;
    private String comment;

    public Comment() {}

    public Comment(String userName, String comment) {
        this.userName = userName;
        this.comment = comment;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Comment [userName=" + userName + ", comment=" + comment + "]";
    }
}

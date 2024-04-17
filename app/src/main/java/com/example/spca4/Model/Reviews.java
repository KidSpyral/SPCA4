package com.example.spca4.Model;

public class Reviews {


    private String Rating;
    private String Comment;

    public Reviews(){

    }

    public Reviews(String rating, String comment) {
        this.Rating = rating;
        this.Comment = comment;
    }

    // Getters
    public String getRating() {
        return Rating;
    }

    public String getComment() {
        return Comment;
    }

    // Setters
    public void setRating(String rating) {
        this.Rating = rating;
    }

    public void setComment(String comment) {
        this.Comment = comment;
    }
}

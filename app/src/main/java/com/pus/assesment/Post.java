package com.pus.assesment;

public class Post {
    private String title;
    private  String author;
    private  String imageUrl;
    private  String date;
    private  String description;
    public Post(String title, String author, String imageUrl, String date, String description) {
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.date = date;
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }
    public String getAuthor() {
        return this.author;
    }
    public String getImageUrl() {
        return this.imageUrl;
    }
    public String getDate() {
        return this.date;
    }
    public String getDescription() {
        return this.description;
    }

}

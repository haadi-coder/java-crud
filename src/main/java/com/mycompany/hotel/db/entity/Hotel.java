package com.mycompany.hotel.db.entity;

public class Hotel {
    private Integer id;           
    private String title;         
    private int rating;         

    public Hotel() {
    }

    public Hotel(String title, int rating) {
        this.title = title;
        this.rating = rating;
    }

    public Hotel(Integer id, String title, int rating) {
        this.id = id;
        this.title = title;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

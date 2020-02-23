package com.example.catch_taboo;

//A class for each item; used to display in buy page
public class Player {
    private String name;
    private String id;

    public Player() {
        //empty constructor needed
    }

    public Player(String name, String id) {
        this.name = name;
        this.id=id;
    }
    //Get all things in item
    public String getName() {
        return name;
    }
    public String getId(){return id;}
    //Set all things in item
    public void setName(String Name) {
        name = Name;
    }
    public void setId(String id) {this.id = id;}
}
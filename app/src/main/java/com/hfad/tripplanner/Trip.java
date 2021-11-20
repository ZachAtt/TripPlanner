package com.hfad.tripplanner;

public class Trip {
    private int id;
    private String name;

    public Trip(){
        setId(0);
        setName("");
    }

    public Trip(int id, String name){
        setId(id);
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

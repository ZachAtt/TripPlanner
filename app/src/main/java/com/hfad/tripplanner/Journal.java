package com.hfad.tripplanner;

import java.util.Date;

public class Journal {
    private int id;
    private int destinationId;
    private int tripId;
    private Date date;
    private String entry;

    public Journal(){
        this.setId(0);
        this.setDestinationId(0);
        this.setTripId(0);
        this.setDate(new Date());
        this.setEntry("");
    }

    public Journal(int id, int destinationId, int tripId, Date date, String entry){
        this.setDestinationId(destinationId);
        this.setTripId(tripId);
        this.setDate(date);
        this.setEntry(entry);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}

package com.hfad.tripplanner;

import java.util.Date;

public class Destination {
    private int id;
    private int tripId;
    private String city;
    private String state;
    private String country;
    private double lat;
    private double lon;
    private Date arrivalDate;
    private Date departureDate;
    private int nextDestination;
    private String travelType;
    private int travelNumber;
    private String travelUrl;
    private int lodgingNumber;
    private String lodgingUrl;

    public Destination(){
        setId(0);
        this.setTripId(0);
        this.setCity("");
        this.setState("");
        this.setCountry("");
        this.setLat(0.0);
        this.setLon(0.0);
        this.setArrivalDate(new Date());
        this.setDepartureDate(new Date());
        this.setNextDestination(0);
        this.setTravelType("");
        this.setTravelNumber(0);
        this.setTravelUrl("");
        this.setLodgingNumber(0);
        this.setLodgingUrl("");
    }

    public Destination(int id, int tripId, String city, String state, String country,
                       double lat, double lon, Date arrivalDate, Date departureDate,
                       int nextDestination, String travelType, int travelNumber,
                       String travelUrl, int lodgingNumber, String lodgingUrl){
        setId(id);
        setTripId(tripId);
        setCity(city);
        setState(state);
        setCountry(country);
        setLat(lat);
        setLon(lon);
        setArrivalDate(arrivalDate);
        setDepartureDate(departureDate);
        setNextDestination(nextDestination);
        setTravelType(travelType);
        setTravelNumber(travelNumber);
        setTravelUrl(travelUrl);
        setLodgingNumber(lodgingNumber);
        setLodgingUrl(lodgingUrl);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public int getNextDestination() {
        return nextDestination;
    }

    public void setNextDestination(int nextDestination) {
        this.nextDestination = nextDestination;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    public int getTravelNumber() {
        return travelNumber;
    }

    public void setTravelNumber(int travelNumber) {
        this.travelNumber = travelNumber;
    }

    public String getTravelUrl() {
        return travelUrl;
    }

    public void setTravelUrl(String travelUrl) {
        this.travelUrl = travelUrl;
    }

    public int getLodgingNumber() {
        return lodgingNumber;
    }

    public void setLodgingNumber(int lodgingNumber) {
        this.lodgingNumber = lodgingNumber;
    }

    public String getLodgingUrl() {
        return lodgingUrl;
    }

    public void setLodgingUrl(String lodgingUrl) {
        this.lodgingUrl = lodgingUrl;
    }
}

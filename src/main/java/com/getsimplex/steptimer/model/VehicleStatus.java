package com.getsimplex.steptimer.model;

public class VehicleStatus {

    private String truckNumber;
    private String origin;
    private String destination;
    private Integer milesFromShop;
    private Integer odomoterReading;

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getMilesFromShop() {
        return milesFromShop;
    }

    public void setMilesFromShop(Integer milesFromShop) {
        this.milesFromShop = milesFromShop;
    }

    public Integer getOdomoterReading() {
        return odomoterReading;
    }

    public void setOdomoterReading(Integer odomoterReading) {
        this.odomoterReading = odomoterReading;
    }
}

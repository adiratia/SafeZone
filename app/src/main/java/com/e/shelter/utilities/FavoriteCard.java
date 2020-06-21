package com.e.shelter.utilities;

public class FavoriteCard {
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public FavoriteCard() {
    }

    /**
     * constructor
     * @param name
     * @param address
     * @param latitude
     * @param longitude
     */
    public FavoriteCard(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * getting name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * setting name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getting address
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * setting address
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * getting latitude
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * setting latitude
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * getting longitude
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * set longitude
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

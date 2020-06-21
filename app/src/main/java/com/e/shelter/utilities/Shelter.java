package com.e.shelter.utilities;

public class Shelter {
    private String name;
    private String address;
    private String lat;
    private String lon;
    private String status;
    private String capacity;
    private String rating;
    private String rateCount;

    /**
     * constructor
     * @param name
     * @param address
     * @param lat
     * @param lon
     * @param status
     * @param capacity
     * @param rating
     * @param rateCount
     */
    public Shelter(String name, String address, String lat, String lon, String status, String capacity, String rating, String rateCount) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.status = status;
        this.capacity = capacity;
        this.rating = rating;
        this.rateCount = rateCount;
    }

    /**
     *
     */
    public Shelter() {
    }

    /**
     * setting shelter name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setting shelter address
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * setting latitude
     * @param lat
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * setting longitude
     * @param lon
     */
    public void setLon(String lon) {
        this.lon = lon;
    }

    /**
     * setting status (open or closed)
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * setting capacity
     * @param capacity
     */
    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    /**
     * getting shelter name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * getting shelter address
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * getting shelter latitude
     * @return
     */
    public String getLat() {
        return lat;
    }

    /**
     * getting shelter longitude
     * @return
     */
    public String getLon() {
        return lon;
    }

    /**
     * getting shelter status (open or closed)
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * getting shelter capacity
     * @return
     */
    public String getCapacity() {
        return capacity;
    }

    /**
     * getting shelter ratingss
     * @return
     */
    public String getRating() {
        return rating;
    }

    /**
     * setting shelter rating
     * @param rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * getting rating counter
     * @return
     */
    public String getRateCount() {
        return rateCount;
    }

    /**
     * setting rating counter
     * @param rateCount
     */
    public void setRateCount(String rateCount) {
        this.rateCount = rateCount;
    }

    @Override
    public String toString() {
        return "Shelter{"
                + "name='" + name + '\''
                + ", address='" + address + '\''
                + ", lat='" + lat + '\''
                + ", lon='" + lon + '\''
                + ", status='" + status + '\''
                + ", capacity='" + capacity + '\''
                + ", rating='" + rating + '\''
                + ", rateCount='" + rateCount
                + '\''
                + '}';
    }
}

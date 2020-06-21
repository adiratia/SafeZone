package com.e.shelter.utilities;

public class Review {

    private String shelterName;
    private String userName;
    private String userEmail;
    private String review;
    private String stars;
    private String time;

    public Review() {
    }

    /**
     * constructor
     * @param shelterName
     * @param userName
     * @param userEmail
     * @param review
     * @param stars
     * @param time
     */
    public Review(String shelterName, String userName, String userEmail, String review, String stars, String time){
        this.review=review;
        this.shelterName=shelterName;
        this.userName=userName;
        this.userEmail=userEmail;
        this.stars = stars;
        this.time=time;
    }

    /**
     * getting time
     * @return
     */
    public String getTime() {
        return time;
    }

    /**
     * setting time
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * getting rating
     * @return
     */
    public String getStars() {
        return stars;
    }

    /**
     * setting rating
     * @param stars
     */
    public void setStars(String stars) {
        this.stars = stars;
    }

    /**
     * getting shelter name
     * @return
     */
    public String getShelterName() {
        return shelterName;
    }

    /**
     * setting shelter name
     * @param shelterName
     */
    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    /**
     * getting user name
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setting user name
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * getting user email
     * @return
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * setting user email
     * @param userEmail
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * getting review
     * @return
     */
    public String getReview() {
        return review;
    }

    /**
     * setting review
     * @param review
     */
    public void setReview(String review) {
        this.review = review;
    }



}

package com.e.shelter.utilities;

import java.util.ArrayList;

public class FavoriteShelter {
    private String email;
    private ArrayList<FavoriteCard> favoriteShelters;

    public FavoriteShelter() {
    }

    /**
     * Constructor
     * @param email
     * @param favoriteShelters
     */
    public FavoriteShelter(String email, ArrayList<FavoriteCard> favoriteShelters) {
        this.email = email;
        this.favoriteShelters = favoriteShelters;
    }

    /**
     * getting email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * setting email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getting FavoriteShelters
     * @return
     */
    public ArrayList<FavoriteCard> getFavoriteShelters() {
        return favoriteShelters;
    }

    /**
     * setting FavoriteShelters
     * @param favoriteShelters
     */
    public void setFavoriteShelters(ArrayList<FavoriteCard> favoriteShelters) {
        this.favoriteShelters = favoriteShelters;
    }
}

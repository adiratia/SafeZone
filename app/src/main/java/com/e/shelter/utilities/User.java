package com.e.shelter.utilities;

import java.io.Serializable;


public class User implements Serializable {
    private String name;
    private String phoneNumber;
    private String permission;
    private String email;
    private Boolean blocked;

    public User() {
        //do not delete.
    }

    /**
     * constructor
     * @param name
     * @param phoneNumber
     * @param permission
     * @param email
     * @param blocked
     */
    public User(String name, String phoneNumber, String permission,String email,Boolean blocked) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.permission = permission;
        this.email=email;
        this.blocked=blocked;

    }

    /**
     * getting user email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * setting user email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Return true if user is blocked else false
     * @return
     */
    public Boolean getBlocked() {
        return blocked;
    }

    /**
     * Blocking user
     * @param blocked
     */
    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    /**
     * getting user name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * setting user name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getting user phone number
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * setting user phone number
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * getting permission to check is user is admin
     * @return
     */
    public String getPermission() {
        return permission;
    }

    /**
     * setting user to be admin or regular user
     * @param permission
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }
}

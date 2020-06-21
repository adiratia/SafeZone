package com.e.shelter.utilities;

public class Emails {
    private String email;
    private Boolean blocked;
    public Emails(){}

    /**
     * constuctor
     * @param email
     * @param blocked
     */
    public Emails(String email,Boolean blocked){
        this.email=email;
        this.blocked=blocked;
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
     * setting blocked user
     * @param blocked
     */
    public void setBlocked(Boolean blocked){
         this.blocked=blocked;
    }

    /**
     * getting blocked user
     * @return
     */
    public Boolean getBlocked(){
        return this.blocked;
    }
}

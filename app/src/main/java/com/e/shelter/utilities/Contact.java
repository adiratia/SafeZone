package com.e.shelter.utilities;

import java.io.Serializable;

public class Contact implements Serializable {
    private String name;
    private String nameInEnglish;
    private String phoneNumber;

    public Contact() {
    }


    /**
     * constructor
     * @param name
     * @param nameInEnglish
     * @param phoneNumber
     */
    public Contact(String name, String nameInEnglish, String phoneNumber) {
        this.name = name;
        this.nameInEnglish = nameInEnglish;
        this.phoneNumber = phoneNumber;
    }

    /**
     * get name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * set name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get phone number
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * set phone number
     * @param phoneNumbers
     */
    public void setPhoneNumber(String phoneNumbers) {
        this.phoneNumber = phoneNumbers;
    }

    /**
     * get name in english
     * @return
     */
    public String getNameInEnglish() {
        return nameInEnglish;
    }

    /**
     * set name in english
     * @param nameInEnglish
     */
    public void setNameInEnglish(String nameInEnglish) {
        this.nameInEnglish = nameInEnglish;
    }
}

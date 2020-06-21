package com.e.shelter.validation;

import com.google.android.material.textfield.TextInputEditText;

public final class PhoneValidator {

    private PhoneValidator(){}

    public static boolean isValidPhone(String phone){
        if (phone == null || phone.isEmpty()) return false;
        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (char c : phone.toCharArray()){
            if (Character.isAlphabetic(c)){
                return false;
            }
        }

        return true;
    }

    public static boolean isValidNameTextInputEditText(String phoneNumber, TextInputEditText textInputEditText) {
        if (phoneNumber.isEmpty()) {
            textInputEditText.setError("Please fill out this field");
            return false;
        }
        if (!isValidPhone(phoneNumber)) {
            textInputEditText.setError("Invalid phone number");
            return false;
        }
        return true;
    }

}

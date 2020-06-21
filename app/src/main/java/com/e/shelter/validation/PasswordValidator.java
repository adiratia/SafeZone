package com.e.shelter.validation;

import com.google.android.material.textfield.TextInputEditText;

public final class PasswordValidator {

    private PasswordValidator() {
    }

    public static boolean isValidPassword(String pass) {
        return pass != null && pass.length() >=8 && pass.length() <= 16;
    }

    public static boolean isValidEmailTextInputEditText(String password, TextInputEditText textInputEditText) {
        if (password.isEmpty()) {
            textInputEditText.setError("Please fill out this field");
            return false;
        }
        return true;
    }
}

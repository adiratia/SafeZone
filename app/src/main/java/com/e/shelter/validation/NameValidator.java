package com.e.shelter.validation;

import com.google.android.material.textfield.TextInputEditText;

public final class NameValidator {
    private NameValidator(){
    }

    public static boolean isValidName(String name) {

        if (name == null || name.isEmpty()) return false;

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidNameTextInputEditText(String name, TextInputEditText textInputEditText) {
        if (name.isEmpty()) {
            textInputEditText.setError("Please fill out this field");
            return false;
        }
        if (!isValidName(name)) {
            textInputEditText.setError("Invalid name");
            return false;
        }
        return true;
    }
}

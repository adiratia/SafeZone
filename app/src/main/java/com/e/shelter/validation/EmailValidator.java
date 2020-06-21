package com.e.shelter.validation;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public final class EmailValidator {

    private EmailValidator() {
    }

    /**
     * Email validation pattern.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-+]{1,256}"
            + "\\@"
            + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"
            + "("
            + "\\."
            + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"
            + ")+"
    );

    /**
     * Validates if the given input is a valid email address.
     * @return true if the input is a valid email. false otherwise.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidEmailTextInputEditText(String email, TextInputEditText textInputEditText) {
        if (email.isEmpty()) {
            textInputEditText.setError("Please fill out this field");
            return false;
        }
        if (!isValidEmail(email)) {
            textInputEditText.setError("Invalid email address");
            return false;
        }
        return true;
    }
}

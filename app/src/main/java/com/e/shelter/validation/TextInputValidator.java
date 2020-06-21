package com.e.shelter.validation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public final class TextInputValidator {

    private TextInputValidator() {}

    public static boolean isValidEditText(String string, TextInputEditText textInputEditText) {
        if (string.isEmpty()) {
            textInputEditText.setError("Please fill out this field");
            return false;
        }
        return true;
    }
    public  static boolean isValidMessage(String subject,String message){
        return subject != null && message != null;
    }

    public static boolean isValidShelterName(String string, String oldName, final TextInputEditText textInputEditText) {
        if (string.isEmpty()) {
            textInputEditText.setError("Please fill out this field");
            return false;
        }
//        if (oldName.equals(string)) {
//            return true;
//        }
        boolean check = FirebaseFirestore.getInstance().collection("Shelters").whereEqualTo("name", oldName).get().getResult().isEmpty();
        if (check) {
            textInputEditText.setError("Shelter name already exist");
            return false;
        }
        return true;
    }

}

package com.e.shelter.validation;

public final class StatusValidator {

    private StatusValidator() { }

    public static boolean isValidStatus(String status){
        return status.equals("open") || status.equals("close");
    }
}

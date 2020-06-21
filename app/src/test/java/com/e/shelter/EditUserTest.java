package com.e.shelter;

import com.e.shelter.validation.NameValidator;
import com.e.shelter.validation.PhoneValidator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EditUserTest {

    @Test
    public void NameValidTest() {
        assertTrue(NameValidator.isValidName("Test Test"));
    }

    @Test
    public void NameInvalidTest() {
        assertFalse(NameValidator.isValidName("Test123"));
    }
    @Test
    public void PhoneValidTest() {
        assertTrue(PhoneValidator.isValidPhone("0505555555"));
    }

    @Test
    public void PhoneInvalidTest() {
        assertFalse(PhoneValidator.isValidPhone("050abc"));
    }



}

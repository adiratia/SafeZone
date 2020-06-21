package com.e.shelter;

import com.e.shelter.validation.PasswordValidator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChangePassTest {

    @Test
    public void PasswordValidatorTest_CorrectPassword_ReturnsTrue() {
        assertTrue(PasswordValidator.isValidPassword("123456789"));
    }

    @Test
    public void PasswordValidatorTest_InvalidPassword_ReturnsFalse() {
        assertFalse(PasswordValidator.isValidPassword("12345"));
    }

    @Test
    public void PasswordValidatorTest_EmptyPassword_ReturnsFalse() {
        assertFalse(PasswordValidator.isValidPassword(""));
    }

    @Test
    public void PasswordValidatorTest_NullPassword_ReturnsFalse() {
        assertFalse(PasswordValidator.isValidPassword(null));
    }


}

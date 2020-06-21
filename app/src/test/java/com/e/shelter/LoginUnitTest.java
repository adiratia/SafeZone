package com.e.shelter;


import com.e.shelter.validation.EmailValidator;
import com.e.shelter.validation.PasswordValidator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginUnitTest {

    @Test
    public void EmailValidatorTest_CorrectEmail_ReturnsTrue() {
        assertTrue(EmailValidator.isValidEmail("name@email.com"));
    }

    @Test
    public void EmailValidatorTest_InvalidEmail_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("name@email"));
    }

    @Test
    public void EmailValidatorTest_EmptyEmail_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail(""));
    }

    @Test
    public void EmailValidatorTest_InvalidEmailNoUsername_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("@email.com"));
    }

    @Test
    public void EmailValidatorTest_NullEmail_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail(null));
    }

    @Test
    public void EmailValidatorTest_InvalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("name@email..com"));
    }

    @Test
    public void EmailValidatorTest_InvalidEmailNoTld_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("name@email"));
    }

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

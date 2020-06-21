package com.e.shelter;

import com.e.shelter.validation.PhoneValidator;
import com.e.shelter.validation.TextInputValidator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GlobalMessageTest {

    @Test
    public void globalMessageValid(){
        assertTrue(TextInputValidator.isValidMessage("Test","Test"));


    }
    public void globalMessageInalid(){
        assertFalse(TextInputValidator.isValidMessage(null,"Test"));
        assertFalse(TextInputValidator.isValidMessage("Test",null));
    }
}

package com.e.shelter;

import com.e.shelter.validation.StatusValidator;

import org.junit.Test;
import static org.junit.Assert.*;

public class EditShelterTest {

    @Test
    public void StatusValidTest() {
        assertTrue(StatusValidator.isValidStatus("open"));
        assertTrue(StatusValidator.isValidStatus("close"));
    }

    @Test
    public void StatusInvalidTest() {
        assertFalse(StatusValidator.isValidStatus("Test"));
    }

}

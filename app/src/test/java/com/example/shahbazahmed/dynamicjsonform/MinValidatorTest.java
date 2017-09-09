package com.example.shahbazahmed.dynamicjsonform;

import com.example.shahbazahmed.dynamicjsonform.validators.MinValidator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by shahbazahmed on 09/09/17.
 */

public class MinValidatorTest {
    private String fieldName;
    private int min;

    @Before
    public void setup() {
        fieldName = "age";
        min = 18;
    }

    @Test
    public void testValidateLargerValue() {
        MinValidator minValidator = new MinValidator(fieldName, min);
        Assert.assertTrue(minValidator.isValid("19", false));
        Assert.assertEquals(minValidator.getErrorMessage(), fieldName+" cannot be less than "+min);
    }

    @Test
    public void testViolateMininumValue() {
        MinValidator minValidator = new MinValidator(fieldName, min);
        Assert.assertFalse(minValidator.isValid("16", false));
        Assert.assertEquals(minValidator.getErrorMessage(), fieldName+" cannot be less than "+min);
    }
}

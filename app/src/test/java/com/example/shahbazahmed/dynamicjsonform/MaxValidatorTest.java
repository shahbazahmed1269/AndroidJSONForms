package com.example.shahbazahmed.dynamicjsonform;

import com.example.shahbazahmed.dynamicjsonform.validators.MaxValidator;
import com.example.shahbazahmed.dynamicjsonform.validators.MinValidator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by shahbazahmed on 09/09/17.
 */

public class MaxValidatorTest {
    private String fieldName;
    private int maxValue;
    @Before
    public void setup() {
        fieldName = "age";
        maxValue = 60;
    }
    @Test
    public void testValidateAcceptableValue() {
        MaxValidator maxValidator = new MaxValidator(fieldName, maxValue);
        Assert.assertTrue(maxValidator.isValid("19", false));
        Assert.assertEquals(
                maxValidator.getErrorMessage(),
                fieldName+" cannot be greater than "+maxValue
        );
    }

    @Test
    public void testViolateMininumValue() {
        MaxValidator maxValidator = new MaxValidator(fieldName, maxValue);
        Assert.assertFalse(maxValidator.isValid("64", false));
        Assert.assertEquals(
                maxValidator.getErrorMessage(),
                fieldName+" cannot be greater than "+maxValue
        );
    }
}

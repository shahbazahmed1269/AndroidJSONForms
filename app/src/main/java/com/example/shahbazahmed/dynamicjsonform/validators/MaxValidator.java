package com.example.shahbazahmed.dynamicjsonform.validators;

import android.support.annotation.NonNull;

import com.rengwuxian.materialedittext.validation.METValidator;

/**
 * Created by shahbazahmed on 09/09/17.
 */

public class MaxValidator extends METValidator {
    private int max;

    public MaxValidator(@NonNull String fieldName, int max) {
        super(fieldName + " cannot be greater than " + max);
        this.max = max;
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        int number;
        try {
            number = Integer.parseInt(text.toString());
        } catch (NumberFormatException e) {
            return false;
        }
        return number <= max;
    }
}
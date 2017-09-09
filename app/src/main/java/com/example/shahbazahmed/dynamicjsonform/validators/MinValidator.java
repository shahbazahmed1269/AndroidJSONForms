package com.example.shahbazahmed.dynamicjsonform.validators;

import android.support.annotation.NonNull;

import com.rengwuxian.materialedittext.validation.METValidator;

/**
 * Created by shahbazahmed on 09/09/17.
 */

public class MinValidator extends METValidator {
    private int min;

    public MinValidator(@NonNull String fieldName, int min) {
        super(fieldName + " cannot be less than " + min);
        this.min = min;
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        int number;
        try {
            number = Integer.parseInt(text.toString());
        } catch (NumberFormatException e) {
            return false;
        }
        return number >= min;
    }
}

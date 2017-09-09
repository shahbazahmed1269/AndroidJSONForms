package com.example.shahbazahmed.dynamicjsonform.validators;

import android.support.annotation.NonNull;

import com.rengwuxian.materialedittext.validation.METValidator;

/**
 * Created by shahbazahmed on 09/09/17.
 */

public class ValueRequiredValidator extends METValidator {

    public ValueRequiredValidator(@NonNull String fieldName) {
        super(fieldName + " cannot be empty");
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return text.length() > 0;
    }
}
package com.example.shahbazahmed.dynamicjsonform.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;

import com.example.shahbazahmed.dynamicjsonform.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shahbazahmed on 08/08/17.
 */

public class MaterialEditTextFactory {

    public static MaterialEditText createfromJSON(Context context, JSONObject jsonData) throws JSONException {
        MaterialEditText editText = null;
        if (jsonData.getString("type").equals("text")) {
            editText = (MaterialEditText) LayoutInflater.from(context)
                    .inflate(R.layout.item_edittext, null);
            editText.setAutoValidate(true);
            String fieldName = jsonData.getString("field-name");
            editText.setHint(fieldName);
            editText.setFloatingLabelText(fieldName);
            editText.setId(View.generateViewId());
            if (jsonData.has("required") && jsonData.getBoolean("required")) {
                editText = editText.addValidator(new ValueRequiredValidator(fieldName));
            }
        } else if (jsonData.getString("type").equals("number")) {
            editText = (MaterialEditText) LayoutInflater.from(context)
                    .inflate(R.layout.item_edittext, null);
            editText.setAutoValidate(true);
            String fieldName = jsonData.getString("field-name");
            editText.setHint(fieldName);
            editText.setFloatingLabelText(fieldName);
            editText.setId(View.generateViewId());
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
            if (jsonData.has("required") && jsonData.getBoolean("required")) {
                editText = editText.addValidator(new ValueRequiredValidator(fieldName));
            }
            if (jsonData.has("min")) {
                int min = jsonData.getInt("min");
                editText = editText.addValidator(new MinValidator(fieldName, min));
            }
            if (jsonData.has("max")) {
                int max = jsonData.getInt("max");
                editText = editText.addValidator(new MaxValidator(fieldName, max));
            }
        } else if (jsonData.getString("type").equals("multiline")) {
            editText = (MaterialEditText) LayoutInflater.from(context)
                    .inflate(R.layout.item_edittext, null);
            editText.setAutoValidate(true);
            String fieldName = jsonData.getString("field-name");
            editText.setHint(fieldName);
            editText.setFloatingLabelText(fieldName);
            editText.setId(View.generateViewId());
            editText.setSingleLine(false);
            editText.setMinLines(3);
            if (jsonData.has("required") && jsonData.getBoolean("required")) {
                editText = editText.addValidator(new ValueRequiredValidator(fieldName));
            }
        }

        return editText;
    }
}

class MinValidator extends METValidator {
    private int min;

    public MinValidator(String fieldName, int min) {
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

class MaxValidator extends METValidator {
    private int max;

    public MaxValidator(String fieldName, int max) {
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

class ValueRequiredValidator extends METValidator {
    private int max;

    public ValueRequiredValidator(String fieldName) {
        super(fieldName + " cannot be empty");
        this.max = max;
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return text.length() > 0;
    }
}
package com.example.shahbazahmed.dynamicjsonform.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.example.shahbazahmed.dynamicjsonform.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by shahbazahmed on 08/08/17.
 */

public class MaterialSpinnerFactory {
    public static MaterialSpinner createfromJSON(Context context, JSONObject jsonData) throws JSONException {
        MaterialSpinner spinner = null;
        if (jsonData.getString("type").equals("dropdown")) {
            spinner = (MaterialSpinner) LayoutInflater.from(context)
                    .inflate(R.layout.item_spinner, null);
            spinner.setFloatingLabelText(jsonData.getString("field-name"));
            if (jsonData.has("options")) {
                JSONArray arr = jsonData.getJSONArray("options");
                String[] options = new String[arr.length()];
                for (int i = 0; i < arr.length(); i++) {
                    options[i] = arr.getString(i);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        context, android.R.layout.simple_spinner_item, options
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        }
        return spinner;
    }
}

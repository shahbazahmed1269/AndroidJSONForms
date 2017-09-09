package com.example.shahbazahmed.dynamicjsonform.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.shahbazahmed.dynamicjsonform.R;
import com.example.shahbazahmed.dynamicjsonform.views.MaterialEditTextFactory;
import com.example.shahbazahmed.dynamicjsonform.views.MaterialSpinnerFactory;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;

public class UpdateFormActivity extends AppCompatActivity {
    private final String mJsonFileName = "formDetails.json";
    private static final String TAG = "UpdateFormActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Map<String, String> valuesMap = new HashMap<>();
        if (getIntent() != null && getIntent().getStringExtra("jsonItem") != null) {
            try {
                JSONObject jsonItem = new JSONObject(getIntent().getStringExtra("jsonItem"));
                Log.d(TAG, "jsonItem: "+jsonItem.toString());
                // Create a map of values from JSONObject.
                Iterator<String> iter = jsonItem.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        String value = (String) jsonItem.get(key);
                        valuesMap.put(key, value);

                    } catch (JSONException e) {
                        Log.e(TAG, "Error: "+e.getMessage());
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(UpdateFormActivity.this, "Oops! Some error occured", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: "+e.toString());
            }
        }
        final LinearLayout llForms = (LinearLayout) findViewById(R.id.llForms);
        JSONArray formJSON = null;
        String jsonData = readJSONFromAsset(mJsonFileName);
        if (jsonData != null) {
            try {
                formJSON = new JSONArray(jsonData);
                for (int i = 0; i < formJSON.length(); i++) {
                    String type = formJSON.getJSONObject(i).getString("type");
                    switch (type) {
                        case "text":
                        case "number":
                        case "multiline":
                            MaterialEditText editText = MaterialEditTextFactory.createfromJSON(
                                    this, formJSON.getJSONObject(i)
                            );
                            if (editText != null) {
                                llForms.addView(editText);
                                editText.setText(valuesMap.get(editText.getHint().toString()));
                            }
                            break;
                        case "dropdown":
                            MaterialSpinner spinner = MaterialSpinnerFactory.createfromJSON(
                                    this, formJSON.getJSONObject(i)
                            );
                            if (spinner != null) {
                                llForms.addView(spinner);
                                String selectedValue = valuesMap.get(spinner.getFloatingLabelText().toString());
                                if (selectedValue != null && selectedValue.length() > 0) {
                                    SpinnerAdapter sAdapter = spinner.getAdapter();
                                    Log.d(TAG, "spinner adapter count"+sAdapter.getCount());
                                    for (int j = 0; j < sAdapter.getCount(); j++) {
                                        if (sAdapter.getItem(j).toString().equals(selectedValue)) {
                                            spinner.setSelection(j);
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(
                        getBaseContext(),
                        getString(R.string.json_input_error),
                        Toast.LENGTH_SHORT
                ).show();
            }
        } else {
            Toast.makeText(
                    getBaseContext(),
                    getString(R.string.json_data_not_found),
                    Toast.LENGTH_SHORT
            ).show();
        }

        Button btnDone = new Button(this);
        btnDone.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );
        btnDone.setText(getString(R.string.update));
        btnDone.setId(View.generateViewId());
        llForms.addView(btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int inputsCount = 0;
                JSONObject inputJSON = new JSONObject();
                for (int i = 0; i < llForms.getChildCount(); i++) {
                    View childView = llForms.getChildAt(i);
                    if (childView instanceof MaterialEditText) {
                        inputsCount++;
                        MaterialEditText editText = (MaterialEditText) childView;
                        if (editText.validate()) {
                            try {
                                inputJSON.put(
                                        editText.getHint().toString(),
                                        editText.getText().toString()
                                );
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(
                                        getBaseContext(),
                                        getString(R.string.json_writing_error),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        }
                    } else if (childView instanceof MaterialSpinner) {
                        MaterialSpinner spinner = (MaterialSpinner) childView;
                        try {
                            inputJSON.put(
                                    spinner.getFloatingLabelText().toString(),
                                    spinner.getSelectedItem().toString()
                            );
                            inputsCount++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    getBaseContext(),
                                    getString(R.string.json_writing_error),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                }
                // Check if all inputs are valid
                if (inputsCount == inputJSON.length()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("itemPosition", getIntent().getIntExtra("itemPosition", -1));
                    resultIntent.putExtra("updatedJSON", inputJSON.toString());
                    if (getParent() == null) {
                        setResult(Activity.RESULT_OK, resultIntent);
                    } else {
                        getParent().setResult(Activity.RESULT_OK, resultIntent);
                    }
                    finish();
                } else {
                    Toast.makeText(
                            UpdateFormActivity.this,
                            getString(R.string.error_enter_valid_input),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    private String readJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}




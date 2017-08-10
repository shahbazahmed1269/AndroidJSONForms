package com.example.shahbazahmed.dynamicjsonform.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

import fr.ganfra.materialspinner.MaterialSpinner;

public class FormActivity extends AppCompatActivity {
    private final String mJsonFileName = "formDetails.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
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
                            }
                            break;
                        case "dropdown":
                            MaterialSpinner spinner = MaterialSpinnerFactory.createfromJSON(
                                    this, formJSON.getJSONObject(i)
                            );
                            if (spinner != null) {
                                llForms.addView(spinner);
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
        btnDone.setText(getString(R.string.done));
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
                    resultIntent.putExtra("inputJSON", inputJSON.toString());
                    if (getParent() == null) {
                        setResult(Activity.RESULT_OK, resultIntent);
                    } else {
                        getParent().setResult(Activity.RESULT_OK, resultIntent);
                    }
                    finish();
                } else {
                    Toast.makeText(
                            FormActivity.this,
                            getString(R.string.error_enter_valid_input),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                //Log.d("FormActivity", "inputJSON: "+inputJSON.toString());
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




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
import android.widget.Toast;

import com.example.shahbazahmed.dynamicjsonform.R;
import com.example.shahbazahmed.dynamicjsonform.views.CompositeView;
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

public class FormActivity extends AppCompatActivity {
    private final String mJsonFileName = "formDetails.json";
    private Map<String, View> compositeMap;
    private Map<String, JSONObject> compositeValue;

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
                        case "composite": {
                            if (compositeMap == null)
                                compositeMap = new HashMap();
                            CompositeView cView = new CompositeView(FormActivity.this);
                            final String fieldName = formJSON.getJSONObject(i).getString("field-name");
                            final int id = fieldName.hashCode();
                            final JSONArray fields = (JSONArray) formJSON.getJSONObject(i).get("fields");
                            cView.setId(id);
                            compositeMap.put(fieldName, cView);
                            cView.setItems(formJSON.getJSONObject(i));
                            llForms.addView(cView);
                            cView.holderView1.setText(formJSON.getJSONObject(i).getString("field-name"));
                            cView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(FormActivity.this, CompositeActivity.class);
                                    i.putExtra("compositeId", fieldName);
                                    i.putExtra("itemJSON", fields.toString());
                                    if (compositeValue != null && compositeValue.containsKey(fieldName)) {
                                        i.putExtra("formValue", compositeValue.get(fieldName).toString());
                                    }
                                    startActivityForResult(i, 1);
                                }
                            });

                        }
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
                    } else if (childView instanceof CompositeView) {
                        if (compositeValue != null) {
                            for (Map.Entry<String, JSONObject> entry : compositeValue.entrySet()) {
                                if (entry.getKey().hashCode() == childView.getId()) {
                                    try {
                                        inputJSON.put(entry.getKey(), entry.getValue());
                                        inputsCount++;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        JSONObject jsonInput = new JSONObject(data.getStringExtra("inputJSON"));
                        if (data != null) {
                            String id = data.getStringExtra("compositeId");
                            if (id != null && compositeMap.containsKey(id)) {
                                CompositeView view = (CompositeView) compositeMap.get(id);
                                Iterator<String> keys = jsonInput.keys();
                                if (keys.hasNext()) {
                                    if (compositeValue == null)
                                        compositeValue = new HashMap();
                                    // Cache the value of composite view in a map
                                    compositeValue.put(id, jsonInput);

                                    String key = keys.next();
                                    view.holderView2.setVisibility(View.VISIBLE);
                                    view.textView2.setVisibility(View.VISIBLE);
                                    view.holderView2.setText(key);
                                    view.textView2.setText(jsonInput.getString(key));
                                }

                            }
                        }
                    } catch (JSONException e) {

                    }
                }
            }
            break;
        }
    }
}





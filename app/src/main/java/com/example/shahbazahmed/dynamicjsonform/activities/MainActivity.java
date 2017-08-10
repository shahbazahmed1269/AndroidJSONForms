package com.example.shahbazahmed.dynamicjsonform.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shahbazahmed.dynamicjsonform.R;
import com.example.shahbazahmed.dynamicjsonform.adapters.ItemAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static final int FORM_ACTIVITY_CODE = 1;
    private ItemAdapter mAdapter;
    private TextView mTvReportCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvReportCount = (TextView) findViewById(R.id.tvReportCount);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rvReport);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false)
        );
        mRecyclerView.hasFixedSize();
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ItemAdapter(getLayoutInflater());
        mRecyclerView.setAdapter(mAdapter);

        Button btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, FormActivity.class);
                startActivityForResult(i, FORM_ACTIVITY_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FORM_ACTIVITY_CODE: {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        JSONObject jsonInput = new JSONObject(data.getStringExtra("inputJSON"));
                        mAdapter.addItem(jsonInput);
                        // Update Report count
                        mTvReportCount.setText("Total Reports: " + mAdapter.getItemCount());
                        Log.i(TAG, "jsonInput: " + jsonInput.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(
                                this,
                                "Some error occured while fetching input data",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
                break;
            }
        }
    }
}

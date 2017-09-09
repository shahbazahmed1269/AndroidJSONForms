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
import com.example.shahbazahmed.dynamicjsonform.adapters.RecyclerViewItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static final int FORM_ACTIVITY_CODE = 1;
    private static final int UPDATE_FORM_ACTIVITY_CODE = 2;
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
        mAdapter.setListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClicked(JSONObject jsonItem, int position) {
                Intent i = new Intent(MainActivity.this, UpdateFormActivity.class);
                i.putExtra("jsonItem", jsonItem.toString());
                i.putExtra("itemPosition", position);
                startActivityForResult(i, UPDATE_FORM_ACTIVITY_CODE);
            }
        });
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
            case UPDATE_FORM_ACTIVITY_CODE: {
                try {if (data != null) {
                    int pos = data.getIntExtra("itemPosition", -1);
                    JSONObject item = new JSONObject(data.getStringExtra("updatedJSON"));
                    if (pos != -1 && item != null) {
                        mAdapter.replaceItem(item, pos);
                    }
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
        }
    }
}

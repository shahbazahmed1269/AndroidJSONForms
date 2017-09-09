package com.example.shahbazahmed.dynamicjsonform.adapters;

import org.json.JSONObject;

/**
 * Created by shahbazahmed on 09/09/17.
 */

public interface RecyclerViewItemClickListener {
    void onItemClicked(JSONObject jsonItem, int position);
}

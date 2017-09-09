package com.example.shahbazahmed.dynamicjsonform.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shahbazahmed.dynamicjsonform.R;

import org.json.JSONObject;


/**
 * Created by shahbazahmed on 09/09/17.
 */

public class CompositeView extends LinearLayout {
    public TextView textView1;
    public TextView textView2;
    public TextView holderView1;
    public TextView holderView2;
    public JSONObject item;

    public CompositeView(Context context) {
        super(context);
        initView();
    }

    public CompositeView(Context context, JSONObject item) {
        super(context);
        initView();
        setItems(item);
    }

    public CompositeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CompositeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CompositeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.item_row, null);
        holderView1 = view.findViewById(R.id.holder_field1);
        textView1 = view.findViewById(R.id.tv_field1);
        holderView2 = view.findViewById(R.id.holder_field2);
        textView2 = view.findViewById(R.id.tv_field2);
        addView(view);
    }

    public void setItems(JSONObject item) {
        this.item = item;
    }

}

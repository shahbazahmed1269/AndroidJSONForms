package com.example.shahbazahmed.dynamicjsonform.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shahbazahmed.dynamicjsonform.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.Holder> {

    private final LayoutInflater mInflator;
    private List<JSONObject> mItemList;
    private RecyclerViewItemClickListener mListener;

    public ItemAdapter(LayoutInflater inflator) {
        mInflator = inflator;
        mItemList = new ArrayList<>();
    }

    public void setListener(RecyclerViewItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflator.inflate(R.layout.item_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(mItemList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void addItem(JSONObject item) {
        mItemList.add(item);
        notifyDataSetChanged();
    }

    public void replaceItem(JSONObject item, final int position) {
        mItemList.set(position, item);
        notifyItemChanged(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView mTvField1;
        TextView mTvFieldHolder1;
        TextView mTvField2;
        TextView mTvFieldHolder2;

        public Holder(View v) {
            super(v);
            mTvField1 = v.findViewById(R.id.tv_field1);
            mTvFieldHolder1 = v.findViewById(R.id.holder_field1);
            mTvField2 = v.findViewById(R.id.tv_field2);
            mTvFieldHolder2 = v.findViewById(R.id.holder_field2);
        }

        public void bind(final JSONObject jsonItem, final int position) {
            if (jsonItem != null && jsonItem.length() > 0) {
                Iterator<String> keys = jsonItem.keys();
                if (keys.hasNext()) {
                    String key = keys.next();
                    this.mTvFieldHolder1.setText(key);
                    try {
                        this.mTvField1.setText(jsonItem.getString(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (jsonItem.length() > 1) {
                    this.mTvFieldHolder2.setVisibility(View.VISIBLE);
                    this.mTvField2.setVisibility(View.VISIBLE);
                    if (keys.hasNext()) {
                        String key = keys.next();
                        this.mTvFieldHolder2.setText(key);
                        try {
                            this.mTvField2.setText(jsonItem.getString(key));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(jsonItem, position);
                }
            });
        }
    }
}


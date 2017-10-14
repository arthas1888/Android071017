package com.cajalopez.apimapsapplication.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cajalopez.apimapsapplication.R;
import com.cajalopez.apimapsapplication.databases.DBHelper;
import com.cajalopez.apimapsapplication.fragments.MainFragment;
import com.cajalopez.apimapsapplication.models.MyModel;

import java.util.ArrayList;

/**
 * Created by 74 on 07/10/2017.
 */

public class MyCursorRecycler extends RecyclerView.Adapter<MyCursorRecycler.ViewHolder> {

    private Cursor mDataset;
    private final MainFragment.MyModelCallBack mListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyCursorRecycler(Cursor myDataset, MainFragment.MyModelCallBack listener) {
        mDataset = myDataset;
        mListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyCursorRecycler.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        private final TextView mInfoTextView;
        private final TextView mCategoriesTextView;
        private MyModel model;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mInfoTextView = v.findViewById(R.id.info_text);
            mCategoriesTextView = v.findViewById(R.id.info_categories);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        mDataset.moveToPosition(position);

        int idServer = mDataset.getInt(mDataset.getColumnIndex(DBHelper.COLUMN_SERVER_ID));
        String joke = mDataset.getString(mDataset.getColumnIndex(DBHelper.COLUMN_NAME));
        String categories = mDataset.getString(mDataset.getColumnIndex(DBHelper.COLUMN_CAT));

        holder.model = new MyModel(idServer, joke, categories.split(","));
        holder.mInfoTextView.setText(holder.model.joke);
        holder.mCategoriesTextView.setText(categories);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.notify(holder.model, holder.mInfoTextView);
                }
            }
        });
    }

    public void notifyCursor(Cursor cursor){
        mDataset = cursor;
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset != null) return mDataset.getCount();
        return 0;
    }
}
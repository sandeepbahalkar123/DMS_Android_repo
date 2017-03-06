package com.scorg.dms.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.dummy.DummyContent;
import com.scorg.dms.fragment.ItemDetailFragment;
import com.scorg.dms.ui.ItemDetailActivity;
import com.scorg.dms.ui.ItemListActivity;
import com.scorg.dms.ui.activities.PatientList;
import com.scorg.dms.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by riteshpandhurkar on 24/2/17.
 */

public class PatientListAdapter {/*extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> {

    private final ArrayList<DummyContent.DummyItem> mValues;
    private PatientList mActivity;

    public PatientListAdapter(List<DummyContent.DummyItem> items) {
        mValues = items;
    }

    public PatientListAdapter(List<DummyContent.DummyItem> items, PatientList patientList) {
        mValues = items;
        mActivity = patientList;
    }

    @Override
    public PatientListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient_list_header, parent, false);
        return new PatientListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PatientListAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.mMoreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mTwoPane) {
//                    //Sample request
////                        mConnectionFactory = new ConnectionFactory(ItemListActivity.this, ItemListActivity.this, null, true, DmsConstants.REGISTRATION_CODE);
////                        mConnectionFactory.setHeaderParams();
////                        mConnectionFactory.setUrl(Config.URL_REGISTER_USER );
////                        mConnectionFactory.createConnection(DmsConstants.REGISTRATION_CODE);
//
//                    Bundle arguments = new Bundle();
//                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
//                    ItemDetailFragment fragment = new ItemDetailFragment();
//                    fragment.setArguments(arguments);
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.item_detail_container, fragment)
//                            .commit();
//                } else {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, ItemDetailActivity.class);
//                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
//
//                    context.startActivity(intent);
//                }

                View view = CommonMethods.loadView(R.layout.item_patient_content, mActivity);
                holder.mContentLayout.addView(view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

  *//*  public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mMoreOption;
        public final LinearLayout mContentLayout;
        public DummyContent.DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.uhid);
            mContentView = (TextView) view.findViewById(R.id.userName);
            mMoreOption = (TextView) view.findViewById(R.id.moreOption);
            mContentLayout = (LinearLayout) view.findViewById(R.id.contentLayout);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }*//*

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mMoreOption;
        public final LinearLayout mContentLayout;
        public DummyContent.DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.uhid);
            mContentView = (TextView) view.findViewById(R.id.userName);
            mMoreOption = (TextView) view.findViewById(R.id.moreOption);
            mContentLayout = (LinearLayout) view.findViewById(R.id.contentLayout);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
 */
}

package com.scorg.dms.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.adapters.PatientExpandableListAdapter;
import com.scorg.dms.adapters.PatientListAdapter;
import com.scorg.dms.dummy.DummyContent;
import com.scorg.dms.fragment.ItemDetailFragment;
import com.scorg.dms.helpers.patients.PatientsHelper;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.responsemodel.PatientFileData;
import com.scorg.dms.model.responsemodel.SearchResult;
import com.scorg.dms.model.responsemodel.ShowSearchResultResponseModel;
import com.scorg.dms.network.ConnectRequest;
import com.scorg.dms.network.ConnectionFactory;
import com.scorg.dms.ui.ItemDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PatientList extends AppCompatActivity implements HelperResponse {


    @BindView(R.id.expandableListView)
    ExpandableListView mPatientListView;

    private PatientsHelper mPatientsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_listview);
        ButterKnife.bind(this);

        mPatientsHelper = new PatientsHelper(this, this);
        mPatientsHelper.doGetPatientList();
    }


    @Override
    public void onSuccess(int mOldDataTag, CustomResponse customResponse) {
        ShowSearchResultResponseModel showSearchResultResponseModel = (ShowSearchResultResponseModel) customResponse;
        List<SearchResult> searchResult = showSearchResultResponseModel.getSearchResultData().getSearchResult();

        List<String> headerList = new ArrayList<>();
        HashMap<String, ArrayList<PatientFileData>> childList = new HashMap<String, ArrayList<PatientFileData>>();

        for (SearchResult dataObject :
                searchResult) {
            String patientName = dataObject.getPatientName();
            headerList.add(patientName);
            childList.put(patientName, new ArrayList<PatientFileData>(dataObject.getPatientFileData()));
        }

        mPatientListView.setAdapter(new PatientExpandableListAdapter(this, headerList, childList));
        mPatientListView.setGroupIndicator(null);
        mPatientListView.setChildIndicator(null);
        mPatientListView.setChildDivider(getResources().getDrawable(R.color.transparent));
        mPatientListView.setDivider(getResources().getDrawable(R.color.white));
        mPatientListView.setDividerHeight(2);
        //mPatientListView.setDividerHeight(2);
    }

    @Override
    public void onParseError(int mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(int mOldDataTag, String serverErrorMessage) {

    }
}

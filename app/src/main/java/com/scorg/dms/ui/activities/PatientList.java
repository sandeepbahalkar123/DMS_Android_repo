package com.scorg.dms.ui.activities;


import android.app.DatePickerDialog;
import android.content.Context;
import java.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.scorg.dms.R;
import com.scorg.dms.adapters.Custom_Spin_Adapter;
import com.scorg.dms.adapters.PatientExpandableListAdapter;
import com.scorg.dms.adapters.TagAdapter;
import com.scorg.dms.helpers.patients.PatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.requestmodel.showsearchresultrequestmodel.ShowSearchResultRequestModel;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DmsConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PatientList extends AppCompatActivity implements HelperResponse,View.OnClickListener{


    @BindView(R.id.expandableListView)
    ExpandableListView mPatientListView;
    DrawerLayout mDrawer;
    Toolbar mToolbar;
    FloatingActionButton mFab;
    NavigationView mLeftNavigationView;
    NavigationView mRightNavigationView;
    View mHeaderView;
    TextView tvApply;
    TextView tvReset;
    String labelFromDate;
    String labelToDate;
    private EditText et_uhid;
    private EditText et_fromdate;
    private EditText et_todate;
    private EditText et_searchPatientName;
    private EditText et_annotation;
    private EditText et_search_annotation;
    Spinner mSpinSelectedId;
    DatePickerDialog.OnDateSetListener fromDate;
    DatePickerDialog.OnDateSetListener toDate;
    private Spinner mSpinnerAmissionDate;
    private String mSelectedId;
    private String mAdmissionDate;
    private String[] mArrayId;
    private Context mContext;
    Calendar mCalender;
    ArrayList<String> mTagsList = new ArrayList<String>();
    private Custom_Spin_Adapter mCustomSpinAdapter;
    private PatientsHelper mPatientsHelper;
    private TagAdapter mTagsAdapter;
    RecyclerView mRecycleTag;
    private Handler mAddedTagsEventHandler;
    private HashMap<String, String> mAddedTagsForFiltering;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mContext = getApplicationContext();
        mAddedTagsForFiltering = new HashMap<String, String>();
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.TO_DATE, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FROM_DATE, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.DATE_TYPE, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.ID, DmsConstants.BLANK);

        mAddedTagsEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                doGetPatientList();
            }
        };
        init();
        mArrayId = getResources().getStringArray(R.array.ids);
        ButterKnife.bind(this);

        mPatientsHelper = new PatientsHelper(this, this);
        doGetPatientList();

        // setting adapter for spinner in header view of right drawer
        mCustomSpinAdapter = new Custom_Spin_Adapter(this, mArrayId, getResources().getStringArray(R.array.select_id));
        mSpinSelectedId.setAdapter(mCustomSpinAdapter);

        // spinner click listener
        mSpinSelectedId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int indexSselectedId =  adapterView.getSelectedItemPosition();
                mArrayId = getResources().getStringArray(R.array.select_id);
                mSelectedId = mArrayId[indexSselectedId];
                if(mSelectedId.equals(getResources().getString(R.string.Select))){
                    mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.admission_date));
                    mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                    mSpinnerAmissionDate.setEnabled(true);
                }
                else  if(mSelectedId.equals(getResources().getString(R.string._ipd))){
                    mSpinnerAmissionDate.setEnabled(true);
                    mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.IPD));
                    mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                    et_uhid.setHint(getResources().getString(R.string.IPD));
                }else if(mSelectedId.equals(getResources().getString(R.string._opd))){
                    mSpinnerAmissionDate.setEnabled(true);
                    mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.OPD));
                    mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                    et_uhid.setHint(getResources().getString(R.string.OPD));
                }else if(mSelectedId.equals(getResources().getString(R.string._uhid))){
                    mSpinnerAmissionDate.setEnabled(true);
                    mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.admission_date));
                    mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                    et_uhid.setHint(getResources().getString(R.string.UHID));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
     // spinner click listener
        mSpinnerAmissionDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int indexSselectedId =  adapterView.getSelectedItemPosition();
                mArrayId = getResources().getStringArray(R.array.admission_date);
                mAdmissionDate = mArrayId[indexSselectedId];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
     // right navigation drawer clickListener
        mRightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {


                mDrawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });

        // left navigation drawer clickListener
        mLeftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Left navigation view item clicks here.
                /*int id = item.getItemId();

                if (id == R.id.nav_camera) {
                    Toast.makeText(PatientList.this, "Left Drawer - Import", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_gallery) {
                    Toast.makeText(PatientList.this, "Left Drawer - Gallery", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_slideshow) {
                    Toast.makeText(PatientList.this, "Left Drawer - Slideshow", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_manage) {
                    Toast.makeText(PatientList.this, "Left Drawer - Tools", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_share) {
                    Toast.makeText(PatientList.this, "Left Drawer - Share", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_send) {
                    Toast.makeText(PatientList.this, "Left Drawer - Send", Toast.LENGTH_SHORT).show();
                }*/

                mDrawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }
    /// registering all UI components of activity
    private void init() {
        int width = getResources().getDisplayMetrics().widthPixels/2;
            mCalender = Calendar.getInstance();
        fromDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                mCalender.set(Calendar.YEAR, year);
                mCalender.set(Calendar.MONTH, monthOfYear);
                mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                labelFromDate = CommonMethods.updateLabel(mCalender);
                et_fromdate.setText(labelFromDate);
            }

        };
        toDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                mCalender.set(Calendar.YEAR, year);
                mCalender.set(Calendar.MONTH, monthOfYear);
                mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                labelToDate = CommonMethods.updateLabel(mCalender);
                et_todate.setText(labelToDate);
            }

        };

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mRecycleTag = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setReverseLayout(true);
        mRecycleTag.setLayoutManager(layoutManager);
        mLeftNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mRightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);
        mHeaderView = mRightNavigationView.getHeaderView(0);

        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mRightNavigationView.getLayoutParams();
        params.width = width;

        DrawerLayout.LayoutParams leftParams = (DrawerLayout.LayoutParams) mLeftNavigationView.getLayoutParams();
        leftParams.width = width;
        mRightNavigationView.setLayoutParams(params);
        mLeftNavigationView.setLayoutParams(leftParams);

        mSpinSelectedId = (Spinner)mHeaderView.findViewById(R.id.spinner_selectId) ;
        mSpinnerAmissionDate = (Spinner) mHeaderView.findViewById(R.id.spinner_admissionDate);
        et_uhid = (EditText) mHeaderView.findViewById(R.id.et_uhid);
        et_fromdate = (EditText) mHeaderView.findViewById(R.id.et_fromdate);
        et_todate = (EditText) mHeaderView.findViewById(R.id.et_todate);
        et_searchPatientName = (EditText) mHeaderView.findViewById(R.id.et_searchPatientName);
        et_annotation = (EditText) mHeaderView.findViewById(R.id.et_annotation);
        et_search_annotation = (EditText) mHeaderView.findViewById(R.id.et_search_annotation);
        tvApply = (TextView)mHeaderView.findViewById(R.id.apply);
        tvReset = (TextView)mHeaderView.findViewById(R.id.reset);
        mFab.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        tvApply.setOnClickListener(this);
        et_todate.setOnClickListener(this);
        et_fromdate.setOnClickListener(this);
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
    }

    @Override
    public void onParseError(int mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(int mOldDataTag, String serverErrorMessage) {

    }

    //validation for right drawer fields
    public boolean validateForm(String fromDate, String toDate, String patientName, String annotation) {
        boolean valid = false;

        et_todate.setError(null);
        et_fromdate.setError(null);
        et_searchPatientName.setError(null);
        et_annotation.setError(null);

        if (!fromDate.isEmpty() && !fromDate.equals("") && !toDate.isEmpty() && !toDate.equals("") && fromDate.equals(toDate))
            {
                CommonMethods.setErrorMsg(getString(R.string.selectFromDate), et_fromdate, true);
                 valid = false;
             }
        else  if (!patientName.matches("[a-zA-Z. ]*")) {
            CommonMethods.setErrorMsg(getString(R.string.patientName), et_searchPatientName, true);
                 valid = false;
        }else  if (!annotation.matches("[a-zA-Z. ]*")) {
            CommonMethods.setErrorMsg(getString(R.string.annotation), et_annotation, true);
           valid = false;
        }else{
           valid=true;
        }
        return valid;
    }

//onClick listener
    @Override
    public void onClick(View v) {
        //onclick on floating button
        if (v == mFab) {
            mDrawer.openDrawer(GravityCompat.END);
        }

        // on click of fromDate editext in right drawer
        if (v == et_fromdate) {
            new DatePickerDialog(PatientList.this, fromDate, mCalender
                    .get(Calendar.YEAR), mCalender.get(Calendar.MONTH),
                    mCalender.get(Calendar.DAY_OF_MONTH)).show();


        }
        //on click of toDate editext in right drawer
        if (v == et_todate) {
            new DatePickerDialog(PatientList.this, toDate, mCalender
                    .get(Calendar.YEAR), mCalender.get(Calendar.MONTH),
                    mCalender.get(Calendar.DAY_OF_MONTH)).show();

        }
      //  on click of Reset in right drawer
        if (v == tvReset) {
            et_uhid.setText("");
            et_fromdate.setText("");
            et_todate.setText("");
            et_search_annotation.setText("");
            et_searchPatientName.setText("");
            mSpinnerAmissionDate.setSelection(0);
            mSpinSelectedId.setSelection(0);

        }
        //  on click of Apply in right drawer
        if (v == tvApply) {
            mTagsList.clear();
            if (validateForm(et_fromdate.getText().toString(), et_todate.getText().toString(), et_searchPatientName.getText().toString(), et_annotation.getText().toString())) {

                //adding field values in arrayList to generate tags in recycler view
                if (!mSelectedId.equals(getResources().getString(R.string.Select))) {
                    mTagsList.add(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE + "|" + mSelectedId);
                    mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE, mSelectedId);
                }

                if (!et_uhid.getText().toString().equalsIgnoreCase("")) {

                    mTagsList.add(DmsConstants.ID + "|" + et_uhid.getText().toString());
                    mAddedTagsForFiltering.put(DmsConstants.ID, et_uhid.getText().toString());
                }

                if (!mAdmissionDate.equals(getResources().getString(R.string.Select))) {
                    mTagsList.add(DmsConstants.PATIENT_LIST_PARAMS.DOCTYPE_ID + "|" + mAdmissionDate);
                    mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.DOCTYPE_ID, mAdmissionDate);
                }

                if (!et_fromdate.getText().toString().equalsIgnoreCase("")) {
                    mTagsList.add(DmsConstants.PATIENT_LIST_PARAMS.FROM_DATE + "|" + et_fromdate.getText().toString());
                    mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FROM_DATE, et_fromdate.getText().toString());

                }

                if (!et_todate.getText().toString().equalsIgnoreCase("")) {

                    mTagsList.add(DmsConstants.PATIENT_LIST_PARAMS.TO_DATE + "|" + et_todate.getText().toString());
                    mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.TO_DATE, et_todate.getText().toString());
                }

                if (!et_searchPatientName.getText().toString().equalsIgnoreCase("")) {
                    mTagsList.add(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME + "|" + et_searchPatientName.getText().toString());
                    mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, et_searchPatientName.getText().toString());

                }

                if (!et_annotation.getText().toString().equalsIgnoreCase("")) {
                    mTagsList.add(DmsConstants.PATIENT_LIST_PARAMS.annotationText + "|" + et_annotation.getText().toString());
                    mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.annotationText, et_annotation.getText().toString());
                }

                Log.e("TAg========", "gggg" + et_todate.getText().toString());

                mTagsAdapter = new TagAdapter(mContext, mTagsList, mAddedTagsForFiltering, mAddedTagsEventHandler);
                mRecycleTag.setAdapter(mTagsAdapter);
                mDrawer.closeDrawer(GravityCompat.END);
                doGetPatientList();
            }
        }
    }
    private void doGetPatientList() {

        HashMap<String, String> addedTagsForFiltering;
        if (mTagsAdapter == null) {
            addedTagsForFiltering = mAddedTagsForFiltering;
        } else {
            addedTagsForFiltering = mTagsAdapter.getAddedTagsForFiltering();
        }

        ShowSearchResultRequestModel showSearchResultRequestModel = new ShowSearchResultRequestModel();

        String fileType = addedTagsForFiltering.get(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE);
        //----------
        if (getString(R.string.uhid).equalsIgnoreCase(fileType)) {
            showSearchResultRequestModel.setPatientId(addedTagsForFiltering.get(DmsConstants.ID));
            showSearchResultRequestModel.setReferenceId("");
        } else if (getString(R.string.ipd).equalsIgnoreCase(fileType) || getString(R.string.opd).equalsIgnoreCase(fileType)) {
            showSearchResultRequestModel.setPatientId("");
            showSearchResultRequestModel.setReferenceId(addedTagsForFiltering.get(DmsConstants.ID));
        } else {
            showSearchResultRequestModel.setPatientId("");
            showSearchResultRequestModel.setReferenceId("");
        }

        if (fileType != null) {
            showSearchResultRequestModel.setFileType("" + fileType);
        } else {
            showSearchResultRequestModel.setFileType("");
        }

        String data = addedTagsForFiltering.get(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME);
        if (data != null) {
            showSearchResultRequestModel.setPatientName("" + data);
        }

        data = addedTagsForFiltering.get(DmsConstants.PATIENT_LIST_PARAMS.DATE_TYPE);
        if (data != null) {
            showSearchResultRequestModel.setDateType("" + data);
        }

        data = addedTagsForFiltering.get(DmsConstants.PATIENT_LIST_PARAMS.FROM_DATE);
        if (data != null) {
            showSearchResultRequestModel.setFromDate("" + data);
        }
        data = addedTagsForFiltering.get(DmsConstants.PATIENT_LIST_PARAMS.TO_DATE);
        if (data != null) {
            showSearchResultRequestModel.setToDate("" + data);
        }

        showSearchResultRequestModel.setAnnotationText("");
        showSearchResultRequestModel.setDocTypeId(new String[1]);
        mPatientsHelper.doGetPatientList(showSearchResultRequestModel);
    }
}

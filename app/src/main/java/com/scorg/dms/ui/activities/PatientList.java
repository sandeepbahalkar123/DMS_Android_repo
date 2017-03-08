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
import com.scorg.dms.util.DmsConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PatientList extends AppCompatActivity implements HelperResponse,View.OnClickListener{


    @BindView(R.id.expandableListView)
    ExpandableListView mPatientListView;
    DrawerLayout drawer;
    Toolbar toolbar;
    FloatingActionButton fab;
    NavigationView leftNavigationView;
    NavigationView rightNavigationView;
    View headerView;
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
    Spinner spinSelectedID;
    DatePickerDialog.OnDateSetListener fromDate;
    DatePickerDialog.OnDateSetListener toDate;
    private Spinner spinner_admissionDate;
    private String selected_id;
    private String admission_date;
    private String[] array_id;
    private Context mContext;
    Calendar myCalendar;
    ArrayList<String> mTagsList = new ArrayList<String>();
    private Custom_Spin_Adapter custom_spinner_adapter;
    private PatientsHelper mPatientsHelper;
    private TagAdapter mTagsAdapter;
    RecyclerView recycleTag;
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
        array_id = getResources().getStringArray(R.array.ids);
        ButterKnife.bind(this);

        mPatientsHelper = new PatientsHelper(this, this);
        doGetPatientList();

        // setting adapter for spinner in header view of right drawer
        custom_spinner_adapter = new Custom_Spin_Adapter(this, array_id, getResources().getStringArray(R.array.select_id));
        spinSelectedID.setAdapter(custom_spinner_adapter);

        // spinner click listener
        spinSelectedID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int indexSselectedId =  adapterView.getSelectedItemPosition();
                array_id = getResources().getStringArray(R.array.select_id);
                selected_id = array_id[indexSselectedId];
                if(selected_id.equals(getResources().getString(R.string.Select))){
                    custom_spinner_adapter = new Custom_Spin_Adapter(mContext, array_id, getResources().getStringArray(R.array.admission_date));
                    spinner_admissionDate.setAdapter(custom_spinner_adapter);
                    spinner_admissionDate.setEnabled(true);
                }
                else  if(selected_id.equals(getResources().getString(R.string._ipd))){
                    spinner_admissionDate.setEnabled(true);
                    custom_spinner_adapter = new Custom_Spin_Adapter(mContext, array_id, getResources().getStringArray(R.array.IPD));
                    spinner_admissionDate.setAdapter(custom_spinner_adapter);
                    et_uhid.setHint(getResources().getString(R.string.IPD));
                }else if(selected_id.equals(getResources().getString(R.string._opd))){
                    spinner_admissionDate.setEnabled(true);
                    custom_spinner_adapter = new Custom_Spin_Adapter(mContext, array_id, getResources().getStringArray(R.array.OPD));
                    spinner_admissionDate.setAdapter(custom_spinner_adapter);
                    et_uhid.setHint(getResources().getString(R.string.OPD));
                }else if(selected_id.equals(getResources().getString(R.string._uhid))){
                    spinner_admissionDate.setEnabled(true);
                    custom_spinner_adapter = new Custom_Spin_Adapter(mContext, array_id, getResources().getStringArray(R.array.admission_date));
                    spinner_admissionDate.setAdapter(custom_spinner_adapter);
                    et_uhid.setHint(getResources().getString(R.string.UHID));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
     // spinner click listener
        spinner_admissionDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int indexSselectedId =  adapterView.getSelectedItemPosition();
                array_id = getResources().getStringArray(R.array.admission_date);
                admission_date = array_id[indexSselectedId];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
     // right navigation drawer clickListener
        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {


                drawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });

        // left navigation drawer clickListener
        leftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Left navigation view item clicks here.
                int id = item.getItemId();

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
                }

                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }
    /// registering all UI components of activity
    private void init() {
        int width = getResources().getDisplayMetrics().widthPixels/2;
            myCalendar = Calendar.getInstance();
        fromDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                labelFromDate = DmsConstants.updateLabel(myCalendar);
                et_fromdate.setText(labelFromDate);
            }

        };
        toDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                labelToDate = DmsConstants.updateLabel(myCalendar);
                et_todate.setText(labelToDate);
            }

        };

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recycleTag = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setReverseLayout(true);
        recycleTag.setLayoutManager(layoutManager);
        leftNavigationView = (NavigationView) findViewById(R.id.nav_view);
        rightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);
        headerView = rightNavigationView.getHeaderView(0);

        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) rightNavigationView.getLayoutParams();
        params.width = width;

        DrawerLayout.LayoutParams leftParams = (DrawerLayout.LayoutParams) leftNavigationView.getLayoutParams();
        leftParams.width = width;
        rightNavigationView.setLayoutParams(params);
        leftNavigationView.setLayoutParams(leftParams);

        spinSelectedID = (Spinner)headerView.findViewById(R.id.spinner_selectId) ;
        spinner_admissionDate = (Spinner) headerView.findViewById(R.id.spinner_admissionDate);
        et_uhid = (EditText) headerView.findViewById(R.id.et_uhid);
        et_fromdate = (EditText) headerView.findViewById(R.id.et_fromdate);
        et_todate = (EditText) headerView.findViewById(R.id.et_todate);
        et_searchPatientName = (EditText) headerView.findViewById(R.id.et_searchPatientName);
        et_annotation = (EditText) headerView.findViewById(R.id.et_annotation);
        et_search_annotation = (EditText) headerView.findViewById(R.id.et_search_annotation);
        tvApply = (TextView)headerView.findViewById(R.id.apply);
        tvReset = (TextView)headerView.findViewById(R.id.reset);
        fab.setOnClickListener(this);
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
                DmsConstants.setErrorMsg(getString(R.string.selectFromDate), et_fromdate, true);
                 valid = false;
             }
        else  if (!patientName.matches("[a-zA-Z. ]*")) {
                 DmsConstants.setErrorMsg(getString(R.string.patientName), et_searchPatientName, true);
                 valid = false;
        }else  if (!annotation.matches("[a-zA-Z. ]*")) {
         DmsConstants.setErrorMsg(getString(R.string.annotation), et_annotation, true);
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
        if (v == fab) {
            drawer.openDrawer(GravityCompat.END);
        }

        // on click of fromDate editext in right drawer
        if (v == et_fromdate) {
            new DatePickerDialog(PatientList.this, fromDate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();


        }
        //on click of toDate editext in right drawer
        if (v == et_todate) {
            new DatePickerDialog(PatientList.this, toDate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        }
      //  on click of Reset in right drawer
        if (v == tvReset) {
            et_uhid.setText("");
            et_fromdate.setText("");
            et_todate.setText("");
            et_search_annotation.setText("");
            et_searchPatientName.setText("");
            spinner_admissionDate.setSelection(0);
            spinSelectedID.setSelection(0);

        }
        //  on click of Apply in right drawer
        if (v == tvApply) {
            mTagsList.clear();
            if (validateForm(et_fromdate.getText().toString(), et_todate.getText().toString(), et_searchPatientName.getText().toString(), et_annotation.getText().toString())) {

                //adding field values in arrayList to generate tags in recycler view
                if (!selected_id.equals(getResources().getString(R.string.Select))) {
                    mTagsList.add(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE + "|" + selected_id);
                    mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE, selected_id);
                }

                if (!et_uhid.getText().toString().equalsIgnoreCase("")) {

                    mTagsList.add(DmsConstants.ID + "|" + et_uhid.getText().toString());
                    mAddedTagsForFiltering.put(DmsConstants.ID, et_uhid.getText().toString());
                }

                if (!admission_date.equals(getResources().getString(R.string.Select))) {
                    mTagsList.add(DmsConstants.PATIENT_LIST_PARAMS.DOCTYPE_ID + "|" + admission_date);
                    mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.DOCTYPE_ID, admission_date);
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
                recycleTag.setAdapter(mTagsAdapter);
                drawer.closeDrawer(GravityCompat.END);
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

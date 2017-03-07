package com.scorg.dms.ui.activities;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;

import java.text.SimpleDateFormat;;
import java.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;


import com.scorg.dms.R;
import com.scorg.dms.adapters.Custom_Spin_Adapter;
import com.scorg.dms.adapters.PatientExpandableListAdapter;
import com.scorg.dms.fragment.TagAdapter;
import com.scorg.dms.helpers.patients.PatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
import com.scorg.dms.ui.ItemDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    DrawerLayout drawer;
    Toolbar toolbar;
    FloatingActionButton fab;
    NavigationView leftNavigationView;
    NavigationView rightNavigationView;
    View headerView;
    Button setTags;
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
    private RecyclerView.Adapter mAdapter;
    RecyclerView recycleTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        init();

        array_id = getResources().getStringArray(R.array.ids);
        ButterKnife.bind(this);

        mPatientsHelper = new PatientsHelper(this, this);
        mPatientsHelper.doGetPatientList();
  //floating button click listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.END);
            }
        });

// setting adapter for spinner in header view of right drawer
        custom_spinner_adapter = new Custom_Spin_Adapter(this, array_id, getResources().getStringArray(R.array.select_id));
        spinSelectedID.setAdapter(custom_spinner_adapter);
        custom_spinner_adapter = new Custom_Spin_Adapter(this, array_id, getResources().getStringArray(R.array.admission_date));
        spinner_admissionDate.setAdapter(custom_spinner_adapter);


        //
        setTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm(selected_id,et_uhid.getText().toString(),admission_date,et_fromdate.getText().toString(),et_todate.getText().toString(),et_searchPatientName.getText().toString(),et_search_annotation.getText().toString()))
                {
                    mTagsList.add(selected_id);
                    mTagsList.add(et_uhid.getText().toString());
                    mTagsList.add(admission_date);
                    mTagsList.add(et_fromdate.getText().toString());
                    mTagsList.add(et_todate.getText().toString());
                    mTagsList.add(et_searchPatientName.getText().toString());
                    mTagsList.add(et_search_annotation.getText().toString());
                    mAdapter = new TagAdapter(mContext,mTagsList);
                    recycleTag.setAdapter(mAdapter);
                }

            }
        });
     et_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PatientList.this, fromDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        et_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PatientList.this, toDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
// spinner click listener
        spinSelectedID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int indexSselectedId =  adapterView.getSelectedItemPosition();
                array_id = getResources().getStringArray(R.array.select_id);
                selected_id = array_id[indexSselectedId];
                Toast.makeText(PatientList.this, ""+selected_id, Toast.LENGTH_SHORT).show();


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
                Toast.makeText(PatientList.this, ""+admission_date, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//// right navigation drawer clickListener
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
            myCalendar = Calendar.getInstance();
        fromDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelFromdate();
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
                updateLabelToDate();
            }

        };

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        recycleTag = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setReverseLayout(true);
        recycleTag.setLayoutManager(layoutManager);
        rightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);
        headerView = rightNavigationView.getHeaderView(0);
        setTags = (Button) headerView.findViewById(R.id.setTags) ;
        spinSelectedID = (Spinner)headerView.findViewById(R.id.spinner_selectId) ;
        spinner_admissionDate = (Spinner) headerView.findViewById(R.id.spinner_admissionDate);
        et_uhid = (EditText) headerView.findViewById(R.id.et_uhid);
        et_fromdate = (EditText) headerView.findViewById(R.id.et_fromdate);
        et_todate = (EditText) headerView.findViewById(R.id.et_todate);
        et_searchPatientName = (EditText) headerView.findViewById(R.id.et_searchPatientName);
        et_annotation = (EditText) headerView.findViewById(R.id.et_annotation);
        et_search_annotation = (EditText) headerView.findViewById(R.id.et_search_annotation);
        leftNavigationView = (NavigationView) findViewById(R.id.nav_view);


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
    private void updateLabelFromdate() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_fromdate.setText(sdf.format(myCalendar.getTime()));

    }
    private void updateLabelToDate() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        et_todate.setText(sdf.format(myCalendar.getTime()));
    }


    //validation for right drawer fields
    public boolean validateForm(String selectid, String uhid, String admissionDate, String fromDate, String toDate, String patientName, String annotation) {
        boolean valid = false;
        et_uhid.setError(null);
        et_todate.setError(null);
        et_fromdate.setError(null);
        et_searchPatientName.setError(null);
        et_search_annotation.setError(null);
       if(selectid.equals("Select")){
           custom_spinner_adapter = (Custom_Spin_Adapter) spinSelectedID.getAdapter();
           View selectedView = spinSelectedID.getSelectedView();
           custom_spinner_adapter.setError(selectedView,  getResources().getString(R.string.Error));
           showMessageDialog(PatientList.this,  getResources().getString(R.string.Error), getResources().getString(R.string.selectID));
           valid = false;

       }
      else  if (uhid.length() <= 1) {
            setErrorMsg(getResources().getString(R.string.UHID), et_uhid, true);
            valid = false;
       }else if (admissionDate.equals("Select")) {
           custom_spinner_adapter = (Custom_Spin_Adapter) spinner_admissionDate.getAdapter();
           View selectedView = spinner_admissionDate.getSelectedView();
           custom_spinner_adapter.setError(selectedView,  getResources().getString(R.string.Error));
           showMessageDialog(PatientList.this, getResources().getString(R.string.Error), getResources().getString(R.string.DateType));
           valid = false;
        }else  if (fromDate.length() <= 1) {
           setErrorMsg(getResources().getString(R.string.selectFromDate), et_fromdate, true);
           valid = false;
       }else  if (toDate.length() <= 1) {
           setErrorMsg(getResources().getString(R.string.selectToDate), et_todate, true);
           valid = false;
       }else  if (!patientName.matches("[a-zA-Z.? ]*")) {
           setErrorMsg(getResources().getString(R.string.patientName), et_searchPatientName, true);
           valid = false;
       }else  if (!annotation.matches("[a-zA-Z.? ]*")) {
           setErrorMsg(getResources().getString(R.string.annotation), et_search_annotation, true);
           valid = false;
       }else{
           valid=true;
       }
        return valid;
    }

    //set error msg method for validation
    public void setErrorMsg(String msg, EditText et, boolean isRequestFocus) {
        int ecolor = Color.RED; // whatever color you want
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(msg);
        ssbuilder.setSpan(fgcspan, 0, msg.length(), 0);
        if (isRequestFocus) {
            et.requestFocus();
        }

        et.setError(ssbuilder);
    }
 //show dialog  for validation
    public static Dialog showMessageDialog(Activity activity, String dialogHeader, String dialogMessage) {
        final Dialog dialog = new Dialog(activity, R.style.DialogStyle);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ok);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (dialogHeader != null)
            ((TextView) dialog.findViewById(R.id.text_view_dialog_header)).setText(dialogHeader);
        if (dialogMessage != null)
            ((TextView) dialog.findViewById(R.id.text_view_dialog_message)).setText(dialogMessage);

        dialog.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        return dialog;
    }
}

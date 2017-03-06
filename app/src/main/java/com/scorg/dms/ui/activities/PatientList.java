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

import com.scorg.dms.R;
import com.scorg.dms.adapters.Custom_Spin_Adapter;
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
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;
    private Spinner spinner_admissionDate;
    private String selected_id;
    private String admission_date;
    private String[] array_id;
    private Context mContext;
    Calendar myCalendar;
    private Custom_Spin_Adapter custom_spinner_adapter;
    private PatientsHelper mPatientsHelper;

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.END);
            }
        });


        custom_spinner_adapter = new Custom_Spin_Adapter(this, array_id, getResources().getStringArray(R.array.select_id));
        spinSelectedID.setAdapter(custom_spinner_adapter);
        custom_spinner_adapter = new Custom_Spin_Adapter(this, array_id, getResources().getStringArray(R.array.admission_date));
        spinner_admissionDate.setAdapter(custom_spinner_adapter);


        setTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm(selected_id,et_uhid.getText().toString(),admission_date,et_fromdate.getText().toString(),et_todate.getText().toString(),et_searchPatientName.getText().toString(),et_search_annotation.getText().toString()));

            }
        });
     et_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PatientList.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        et_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PatientList.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

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

        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Right navigation view item clicks here.
                //  int id = item.getItemId();

               /* if (id == R.id.nav_settings) {
                    Toast.makeText(MainActivity.this, "Right Drawer - Settings", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    Toast.makeText(MainActivity.this, "Right Drawer - Logout", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_help) {
                    Toast.makeText(MainActivity.this, "Right Drawer - Help", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_about) {
                    Toast.makeText(MainActivity.this, "Right Drawer - About", Toast.LENGTH_SHORT).show();
                }*/

                drawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });
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

    private void init() {
            myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };

        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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

       /* date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };*/
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
    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_fromdate.setText(sdf.format(myCalendar.getTime()));

    }
    private void updateLabel1() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        et_todate.setText(sdf.format(myCalendar.getTime()));
    }
    public boolean validateForm(String selectid, String uhid, String admissionDate, String fromDate, String toDate, String patientName, String annotation) {
        boolean valid = true;
        et_uhid.setError(null);
        et_todate.setError(null);
        et_fromdate.setError(null);
        et_searchPatientName.setError(null);
        et_search_annotation.setError(null);
       if(selectid.equals("Select")){
           custom_spinner_adapter = (Custom_Spin_Adapter) spinSelectedID.getAdapter();
           View selectedView = spinSelectedID.getSelectedView();
           custom_spinner_adapter.setError(selectedView, "Error");
           showMessageDialog(PatientList.this, "Error", "Please select Id.");
           valid = false;

       }
      else  if (uhid.length() <= 1)
       {
            setErrorMsg(getResources().getString(R.string.UHID), et_uhid, true);
            valid = false;
        } else if (admissionDate.equals("Select"))
       {
           custom_spinner_adapter = (Custom_Spin_Adapter) spinner_admissionDate.getAdapter();
           View selectedView = spinner_admissionDate.getSelectedView();
           custom_spinner_adapter.setError(selectedView, "Error");
           showMessageDialog(PatientList.this, "Error", "Select Date Type");
           valid = false;
        }
       else  if (fromDate.length() <= 1)
       {
           setErrorMsg(getResources().getString(R.string.selectFromDate), et_fromdate, true);
           valid = false;
       }
       else  if (toDate.length() <= 1)
       {
           setErrorMsg(getResources().getString(R.string.selectToDate), et_todate, true);
           valid = false;
       }
       else  if (!patientName.matches("[a-zA-Z.? ]*"))
       {
           setErrorMsg(getResources().getString(R.string.patientName), et_searchPatientName, true);
           valid = false;
       }
       else  if (!annotation.matches("[a-zA-Z.? ]*"))
       {
           setErrorMsg(getResources().getString(R.string.annotation), et_search_annotation, true);
           valid = false;
       }
        return valid;
    }
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

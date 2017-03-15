package com.scorg.dms.ui.activities;


import android.content.Context;


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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scorg.dms.R;
import com.scorg.dms.adapters.Custom_Spin_Adapter;
import com.scorg.dms.adapters.PatientExpandableListAdapter;
import com.scorg.dms.adapters.TagAdapter;
import com.scorg.dms.helpers.patients.PatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.DatePickerDialogListener;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.requestmodel.showsearchresultrequestmodel.ShowSearchResultRequestModel;
import com.scorg.dms.model.responsemodel.annotationlistresponsemodel.AnnotationList;
import com.scorg.dms.model.responsemodel.annotationlistresponsemodel.AnnotationListData;
import com.scorg.dms.model.responsemodel.annotationlistresponsemodel.AnnotationListResponseModel;
import com.scorg.dms.model.responsemodel.annotationlistresponsemodel.DocTypeList;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DmsConstants;
import com.scorg.dms.views.treeViewHolder.IconTreeItemHolder;
import com.scorg.dms.views.treeViewHolder.SelectableHeaderHolder;
import com.scorg.dms.views.treeViewHolder.SelectableItemHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import butterknife.Optional;


public class PatientList extends AppCompatActivity implements HelperResponse, View.OnClickListener, AdapterView.OnItemSelectedListener {


    @BindView(R.id.expandableListView)
    ExpandableListView mPatientListView;
    @BindView(R.id.fab)
    FloatingActionButton mOpenFilterViewFAB;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    DrawerLayout mDrawer;

    //--------------

    NavigationView mLeftNavigationView;
    NavigationView mRightNavigationView;
    View mHeaderView;

    //--------------
    private TextView mApplySearchFilter;
    private TextView mResetSearchFilter;
    private EditText mUHIDEditText;
    private EditText mFromDateEditText;
    private EditText mToDateEditText;
    private EditText mSearchPatientNameEditText;
    private EditText mAnnotationEditText;
    private EditText mSearchAnnotationEditText;

    private Spinner mSpinSelectedId;

    private Spinner mSpinnerAmissionDate;
    private String mSelectedId;
    private String mAdmissionDate;
    private String[] mArrayId;
    private Context mContext;

    private Custom_Spin_Adapter mCustomSpinAdapter;
    private PatientsHelper mPatientsHelper;
    private TagAdapter mTagsAdapter;
    private RecyclerView mRecycleTag;
    private Handler mAddedTagsEventHandler;
    private HashMap<String, String> mAddedTagsForFiltering;
    private RelativeLayout mAnnotationTreeViewContainer;
    private AndroidTreeView mAndroidTreeView;
    private AnnotationListData mAnnotationListData;

    private String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        initialize();
    }


    private void initialize() {
        initializeVariables();
        bindView();
        doGetPatientList();
    }


    private void initializeVariables() {
        mContext = getApplicationContext();
        mAddedTagsForFiltering = new HashMap<String, String>();
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.TO_DATE, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FROM_DATE, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.DATE_TYPE, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.ID, DmsConstants.BLANK);
        //-------------
        mArrayId = getResources().getStringArray(R.array.ids);

        mAddedTagsEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                doGetPatientList();
            }
        };
        //-------------
        mPatientsHelper = new PatientsHelper(this, this);
        //------------

    }

    private void bindView() {
        int width = getResources().getDisplayMetrics().widthPixels / 2;

        //---------
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        //---------
        mRecycleTag = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setReverseLayout(true);
        mRecycleTag.setLayoutManager(layoutManager);

        mLeftNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mRightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);
        mHeaderView = mRightNavigationView.getHeaderView(0);

        //---------------
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mRightNavigationView.getLayoutParams();
        params.width = width;

        DrawerLayout.LayoutParams leftParams = (DrawerLayout.LayoutParams) mLeftNavigationView.getLayoutParams();
        leftParams.width = width;
        mRightNavigationView.setLayoutParams(params);
        mLeftNavigationView.setLayoutParams(leftParams);
        //---------------

        mSpinSelectedId = (Spinner) mHeaderView.findViewById(R.id.spinner_selectId);
        mSpinnerAmissionDate = (Spinner) mHeaderView.findViewById(R.id.spinner_admissionDate);
        mUHIDEditText = (EditText) mHeaderView.findViewById(R.id.et_uhid);
        mFromDateEditText = (EditText) mHeaderView.findViewById(R.id.et_fromdate);
        mToDateEditText = (EditText) mHeaderView.findViewById(R.id.et_todate);
        mSearchPatientNameEditText = (EditText) mHeaderView.findViewById(R.id.et_searchPatientName);
        mAnnotationEditText = (EditText) mHeaderView.findViewById(R.id.et_annotation);
        mSearchAnnotationEditText = (EditText) mHeaderView.findViewById(R.id.et_search_annotation);
        mApplySearchFilter = (TextView) mHeaderView.findViewById(R.id.apply);
        mResetSearchFilter = (TextView) mHeaderView.findViewById(R.id.reset);
        mAnnotationTreeViewContainer = (RelativeLayout) mHeaderView.findViewById(R.id.annotationTreeViewContainer);

        //---------
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
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //---------
        mSpinSelectedId.setOnItemSelectedListener(this);
        mSpinnerAmissionDate.setOnItemSelectedListener(this);
        //---------------
        mOpenFilterViewFAB.setOnClickListener(this);
        mResetSearchFilter.setOnClickListener(this);
        mApplySearchFilter.setOnClickListener(this);
        mFromDateEditText.setOnClickListener(this);
        mToDateEditText.setOnClickListener(this);
        //--------
        // setting adapter for spinner in header view of right drawer
        mCustomSpinAdapter = new Custom_Spin_Adapter(this, mArrayId, getResources().getStringArray(R.array.select_id));
        mSpinSelectedId.setAdapter(mCustomSpinAdapter);
        //------
        onTextChanged();
    }

    @Override
    public void onSuccess(int mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag == DmsConstants.TASK_PATIENT_LIST) {
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
        } else if (mOldDataTag == DmsConstants.TASK_ANNOTATIONS_LIST) {
            AnnotationListResponseModel annotationListResponseModel = (AnnotationListResponseModel) customResponse;
            mAnnotationListData = annotationListResponseModel.getAnnotationListData();

            createAnnotationTreeStructure(mAnnotationListData, false);
        }
    }

    @Override
    public void onParseError(int mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(int mOldDataTag, String serverErrorMessage) {

    }

    /**
     * Return true if fields empty/validation failed, else false.
     *
     * @return
     */
    private boolean validate(String fromDate, String toDate) {

        String message = null;
        if (fromDate.equalsIgnoreCase(toDate)) {
            message = getString(R.string.error_date_not_same);
        }

        if (message != null) {
            CommonMethods.showSnack(mFromDateEditText, message);
            return true;
        } else {
            return false;
        }
    }

    //onClick listener
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //onclick on floating button
            case R.id.fab:
                mDrawer.openDrawer(GravityCompat.END);

                if (mAnnotationListData == null) {
                    mPatientsHelper.doGetAllAnnotations();
                } else {
                    createAnnotationTreeStructure(mAnnotationListData, false);
                }

                break;
            // on click of fromDate editext in right drawer
            case R.id.et_fromdate:

                new CommonMethods().datePickerDialog(this, new DatePickerDialogListener() {
                    @Override
                    public void getSelectedDate(String selectedTime) {
                        mFromDateEditText.setText("" + selectedTime);
                    }
                }, null);
                break;
            // on click of endDate ediText in right drawer
            case R.id.et_todate:
                new CommonMethods().datePickerDialog(this, new DatePickerDialogListener() {
                    @Override
                    public void getSelectedDate(String selectedTime) {
                        mToDateEditText.setText("" + selectedTime);
                    }
                }, null);
                break;
            //  on click of Reset in right drawer
            case R.id.reset:
                mAddedTagsForFiltering.clear();
                mUHIDEditText.setText(DmsConstants.BLANK);
                mFromDateEditText.setText(DmsConstants.BLANK);
                mToDateEditText.setText(DmsConstants.BLANK);
                mSearchAnnotationEditText.setText(DmsConstants.BLANK);
                mSearchPatientNameEditText.setText(DmsConstants.BLANK);
                mSpinnerAmissionDate.setSelection(0);
                mSpinSelectedId.setSelection(0);
                break;
            //  on click of Apply in right drawer
            case R.id.apply:

                mAddedTagsForFiltering.clear();
                String fromDate = mFromDateEditText.getText().toString().trim();
                String toDate = mToDateEditText.getText().toString().trim();
                boolean dateValidate = false;
                if ((fromDate.length() != 0 && toDate.length() != 0)) {
                    dateValidate = validate(fromDate, toDate);
                }
                if (!dateValidate) {

                    //adding field values in arrayList to generate tags in recycler view
                    if (!mSelectedId.equalsIgnoreCase(getResources().getString(R.string.Select))) {
                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE, mSelectedId);
                    }

                    if (mUHIDEditText.getText().toString().trim().length() != 0) {
                        mAddedTagsForFiltering.put(DmsConstants.ID, mUHIDEditText.getText().toString());
                    }

                    if (!mAdmissionDate.equalsIgnoreCase(getResources().getString(R.string.Select))) {
                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.DOC_TYPE_ID, mAdmissionDate);
                    }

                    if (!fromDate.equalsIgnoreCase(DmsConstants.BLANK)) {
                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FROM_DATE, mFromDateEditText.getText().toString());
                    }

                    if (!toDate.equalsIgnoreCase(DmsConstants.BLANK)) {
                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.TO_DATE, mToDateEditText.getText().toString());
                    }

                    if (mSearchPatientNameEditText.getText().toString().trim().length() != 0) {
                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, mSearchPatientNameEditText.getText().toString());
                    }

                    if (mAnnotationEditText.getText().toString().trim().length() != 0) {
                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.ANNOTATION_TEXT, mAnnotationEditText.getText().toString());
                    }

                    //----------
                    String[] selectedAnnotations = getSelectedAnnotations();
                    if (selectedAnnotations.length > 0) {
                        for (String dataValue :
                                selectedAnnotations) {
                            //--- hashMap Data : DocTypeId_<childName>, childName|id
                            mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.DOC_TYPE_ID + "_" + dataValue, dataValue);
                        }
                    }
                    //-------------

                    mTagsAdapter = new TagAdapter(mContext, mAddedTagsForFiltering, mAddedTagsEventHandler);
                    mRecycleTag.setAdapter(mTagsAdapter);
                    mDrawer.closeDrawer(GravityCompat.END);
                    doGetPatientList();

                }

                break;
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

        showSearchResultRequestModel.setAnnotationText(DmsConstants.BLANK);
        showSearchResultRequestModel.setDocTypeId(getSelectedAnnotations());
        mPatientsHelper.doGetPatientList(showSearchResultRequestModel);
    }

    private void createAnnotationTreeStructure(AnnotationListData annotationListData, boolean isExpanded) {

        mAnnotationTreeViewContainer.removeAllViews();

        TreeNode root = TreeNode.root();

        List<AnnotationList> annotationLists = annotationListData.getAnnotationLists();

        for (int i = 0; i < annotationLists.size(); i++) {
            AnnotationList annotationCategoryObject = annotationLists.get(i);

            SelectableHeaderHolder selectableHeaderHolder = new SelectableHeaderHolder(this, isExpanded);
            TreeNode folder1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, annotationCategoryObject.getCategoryName() + "|" + DmsConstants.CATEGORY_NAME))
                    .setViewHolder(selectableHeaderHolder);

            List<DocTypeList> docTypeList = annotationCategoryObject.getDocTypeList();

            for (int j = 0; j < docTypeList.size(); j++) {
                DocTypeList docTypeListObject = docTypeList.get(j);
                String dataToShow = docTypeListObject.getTypeName() + "|" + docTypeListObject.getTypeId();

                TreeNode file3 = new TreeNode(dataToShow).setViewHolder(new SelectableItemHolder(this));
                folder1.addChildren(file3);
            }
            root.addChildren(folder1);
        }

        mAndroidTreeView = new AndroidTreeView(this, root);
        mAndroidTreeView.setDefaultAnimation(true);
        mAnnotationTreeViewContainer.addView(mAndroidTreeView.getView());
        mAndroidTreeView.setSelectionModeEnabled(true);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner_selectId) {
            int indexSselectedId = parent.getSelectedItemPosition();
            mArrayId = getResources().getStringArray(R.array.select_id);
            mSelectedId = mArrayId[indexSselectedId];
            if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.Select))) {
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.admission_date));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                mSpinnerAmissionDate.setEnabled(true);
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.ipd))) {
                mSpinnerAmissionDate.setEnabled(true);
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.IPD));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                mUHIDEditText.setHint(getResources().getString(R.string.IPD));
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.opd))) {
                mSpinnerAmissionDate.setEnabled(true);
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.OPD));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                mUHIDEditText.setHint(getResources().getString(R.string.OPD));
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.uhid))) {
                mSpinnerAmissionDate.setEnabled(true);
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.admission_date));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                mUHIDEditText.setHint(getResources().getString(R.string.uhid));
            }
        } else if (parent.getId() == R.id.spinner_admissionDate) {
            int indexSselectedId = parent.getSelectedItemPosition();
            mArrayId = getResources().getStringArray(R.array.admission_date);
            mAdmissionDate = mArrayId[indexSselectedId];
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private String[] getSelectedAnnotations() {
        HashSet<String> annotationList = new HashSet<String>();
        if (mAndroidTreeView != null) {
            List<TreeNode> selected = mAndroidTreeView.getSelected();
            if (selected.size() > 0) {
                for (TreeNode data :
                        selected) {
                    String dataValue = data.getValue().toString();
                    //-- This is done for child only, no parent name will come in the list.
                    if (dataValue.contains("|")) {
                        annotationList.add(dataValue);
                    }
                }
            }
        }
        String[] strings = annotationList.toArray(new String[annotationList.size()]);
        return strings;
    }


    protected void onTextChanged() {

        mSearchAnnotationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = mSearchAnnotationEditText.getText().toString();
                CommonMethods.Log(TAG, "onSearchAnnotationEditor:" + enteredString);
                AnnotationListData annotationListData = new AnnotationListData();
                List<AnnotationList> annotationTempList = new ArrayList<>();
                if (mAnnotationListData != null) {
                    List<AnnotationList> parentAnnotationList = mAnnotationListData.getAnnotationLists();
                    if (parentAnnotationList.size() > 0) {

                        for (AnnotationList tempParentObject : parentAnnotationList) {
                            if (tempParentObject.getCategoryName().toLowerCase().contains(enteredString.toLowerCase())) {
                                annotationTempList.add(tempParentObject);
                            } else {

                                //TODO : THIS IS NOT FIXED, CHECK FOR DOUBLE ENTRY IN TRRE VIEW FOR CHILD SEARCH
                                //-------
                                List<DocTypeList> childDocTypeList = tempParentObject.getDocTypeList();
                                for (DocTypeList tempDocTypeObject : childDocTypeList) {
                                    if (tempDocTypeObject.getTypeName().toLowerCase().contains(enteredString.toLowerCase())) {
                                        annotationTempList.add(tempParentObject);
                                        break;
                                    }
                                }
                                //------
                            }

                        }
                    }
                }
                annotationListData.setAnnotationLists(annotationTempList);
                createAnnotationTreeStructure(annotationListData, true);
            }
        });
    }
}

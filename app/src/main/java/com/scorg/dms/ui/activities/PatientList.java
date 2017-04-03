package com.scorg.dms.ui.activities;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.scorg.dms.preference.DmsPreferencesManager;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DmsConstants;
import com.scorg.dms.views.treeViewHolder.arrow_expand.ArrowExpandIconTreeItemHolder;
import com.scorg.dms.views.treeViewHolder.arrow_expand.ArrowExpandSelectableHeaderHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PatientList extends AppCompatActivity implements HelperResponse, View.OnClickListener, AdapterView.OnItemSelectedListener, PatientExpandableListAdapter.OnPatientListener {


    private static final long ANIMATION_DURATION = 500; // in milliseconds
    private static final int ANIMATION_LAYOUT_MAX_HEIGHT = 270; // in milliseconds
    private static final int ANIMATION_LAYOUT_MIN_HEIGHT = 0; // in milliseconds

    @BindView(R.id.expandableListView)
    ExpandableListView mPatientListView;
    @BindView(R.id.openFilterRightDrawerFAB)
    FloatingActionButton mOpenFilterViewFAB;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    DrawerLayout mDrawer;


    NavigationView mLeftNavigationView;

    //----------------
    @BindView(R.id.nav_right_view)
    FrameLayout mRightNavigationView;

    @BindView(R.id.spinner_selectId)
    Spinner mSpinSelectedId;

    @BindView(R.id.spinner_admissionDate)
    Spinner mSpinnerAmissionDate;

    @BindView(R.id.et_uhid)
    EditText mUHIDEditText;

    @BindView(R.id.et_fromdate)
    EditText mFromDateEditText;

    @BindView(R.id.et_todate)
    EditText mToDateEditText;

    @BindView(R.id.et_searchPatientName)
    EditText mSearchPatientNameEditText;

    @BindView(R.id.et_userEnteredAnnotation)
    EditText mAnnotationEditText;

    @BindView(R.id.et_search_annotation)
    EditText mSearchAnnotationEditText;

    @BindView(R.id.apply)
    TextView mApplySearchFilter;

    @BindView(R.id.reset)
    TextView mResetSearchFilter;

    @BindView(R.id.annotationTreeViewContainer)
    RelativeLayout mAnnotationTreeViewContainer;


    //----------------

    View mLeftHeaderView;
    private ImageView mUserImage;
    private TextView mUserName;


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
    private AndroidTreeView mAndroidTreeView;
    private AnnotationListData mAnnotationListData;
    private String TAG = this.getClass().getName();
    private boolean isCompareDialogCollapsed = true;

    private RelativeLayout mCompareDialogLayout;
    private TextView mCompareLabel;
    private ImageView mFileOneIcon;
    private TextView mFileOneType;
    private TextView mFileOneAdmissionDate;
    //    private TextView mFileOneDischargeDate;
    private ImageView mFileTwoIcon;
    private TextView mFileTwoType;
    private TextView mFileTwoAdmissionDate;
    //    private TextView mFileTwoDischargeDate;
    private Button mCompareButton;

    private boolean isFileType = false;
    private PatientExpandableListAdapter patientExpandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_list_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        initialize();
    }


    private void initialize() {
        initializeVariables();
        bindView();
        doGetPatientList();
    }

    // intialize variables
    private void initializeVariables() {
        mContext = getApplicationContext();
        mAddedTagsForFiltering = new HashMap<String, String>();
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.TO_DATE, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FROM_DATE, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.DATE_TYPE, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.ID, DmsConstants.BLANK);
        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.ANNOTATION_TEXT, DmsConstants.BLANK);
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
        mTagsAdapter = new TagAdapter(mContext, mAddedTagsForFiltering, mAddedTagsEventHandler);

    }

    // register all views
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
        mLeftHeaderView = mLeftNavigationView.getHeaderView(0);
        //---------------

        DrawerLayout.LayoutParams leftParams = (DrawerLayout.LayoutParams) mLeftNavigationView.getLayoutParams();
        leftParams.width = width;
        mLeftNavigationView.setLayoutParams(leftParams);
        //---------------
        mUserImage = (ImageView) mLeftHeaderView.findViewById(R.id.userImage);
        mUserName = (TextView) mLeftHeaderView.findViewById(R.id.userName);

        if (DmsPreferencesManager.getString(DmsPreferencesManager.DMS_PREFERENCES_KEY.USER_GENDER, mContext).equals("M")) {
            mUserImage.setBackground(getResources().getDrawable(R.drawable.image_male));
        } else {
            mUserImage.setBackground(getResources().getDrawable(R.drawable.image_female));
        }
        //---------


        // left navigation drawer clickListener
        mLeftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_logout) {
                    DmsPreferencesManager.clearSharedPref(mContext);
                    Intent intent = new Intent(PatientList.this, SplashScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    // Handle the camera action
                }

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
        mUserName.setText(DmsPreferencesManager.getString(DmsConstants.USERNAME, mContext));
        //--------
        // setting adapter for spinner in header view of right drawer
        mCustomSpinAdapter = new Custom_Spin_Adapter(this, mArrayId, getResources().getStringArray(R.array.select_id));
        mSpinSelectedId.setAdapter(mCustomSpinAdapter);
        //------
        onTextChanged();

        mCompareDialogLayout = (RelativeLayout) findViewById(R.id.compareDialog);
        mCompareLabel = (TextView) findViewById(R.id.compareLabel);
        mFileOneIcon = (ImageView) findViewById(R.id.fileOneIcon);
        mFileOneType = (TextView) findViewById(R.id.fileOneType);
        mFileOneAdmissionDate = (TextView) findViewById(R.id.fileOneAdmissionDate);
//        mFileOneDischargeDate = (TextView) findViewById(R.id.fileOneDischargeDate);
        mFileTwoIcon = (ImageView) findViewById(R.id.fileTwoIcon);
        mFileTwoType = (TextView) findViewById(R.id.fileTwoType);
        mFileTwoAdmissionDate = (TextView) findViewById(R.id.fileTwoAdmissionDate);
//        mFileTwoDischargeDate = (TextView) findViewById(R.id.fileTwoDischargeDate);
        mCompareButton = (Button) findViewById(R.id.compareButton);

        mCompareLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCompareDialogCollapsed)
                    expandCompareDialog();
                else collapseCompareDialog();
            }
        });

        ViewGroup.LayoutParams layoutParams = mRightNavigationView.getLayoutParams();
        layoutParams.width = width;
        mRightNavigationView.setLayoutParams(layoutParams);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag == DmsConstants.TASK_PATIENT_LIST) {
            ShowSearchResultResponseModel showSearchResultResponseModel = (ShowSearchResultResponseModel) customResponse;
            List<SearchResult> searchResult = showSearchResultResponseModel.getSearchResultData().getSearchResult();

            patientExpandableListAdapter = new PatientExpandableListAdapter(this, searchResult);

            mPatientListView.setAdapter(patientExpandableListAdapter);
            mPatientListView.setGroupIndicator(null);
            mPatientListView.setChildIndicator(null);
            mPatientListView.setChildDivider(ContextCompat.getDrawable(this, R.color.transparent));
            mPatientListView.setDivider(ContextCompat.getDrawable(this, R.color.white));
            mPatientListView.setDividerHeight(2);

            //mPatientListView.setDividerHeight(2);
        } else if (mOldDataTag == DmsConstants.TASK_ANNOTATIONS_LIST) {
            AnnotationListResponseModel annotationListResponseModel = (AnnotationListResponseModel) customResponse;
            mAnnotationListData = annotationListResponseModel.getAnnotationListData();

            createAnnotationTreeStructure(mAnnotationListData, true);
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }

    /**
     * Return true if fields empty/validation failed, else false.
     *
     * @return
     */
    private boolean validate(String fromDate, String toDate) throws ParseException {
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        String message = null;
        if (fromDate.equalsIgnoreCase(toDate)) {
            message = getString(R.string.error_date_not_same);
        } else if (dfDate.parse(fromDate).after(dfDate.parse(toDate))) {
            message = getString(R.string.error_previous_date);
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
            case R.id.openFilterRightDrawerFAB:
                mDrawer.openDrawer(GravityCompat.END);

                if (mAnnotationListData == null) {
                    mPatientsHelper.doGetAllAnnotations();
                } else {
                    createAnnotationTreeStructure(mAnnotationListData, true);
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
                mAnnotationEditText.setText(DmsConstants.BLANK);
                mSearchPatientNameEditText.setText(DmsConstants.BLANK);
                mSpinnerAmissionDate.setSelection(0);
                mSpinSelectedId.setSelection(0);
                break;
            //  on click of Apply in right drawer
            case R.id.apply:

                onCompareDialogShow(null, null, null, null, false);

                mAddedTagsForFiltering.clear();
                String fromDate = mFromDateEditText.getText().toString().trim();
                String toDate = mToDateEditText.getText().toString().trim();
                boolean dateValidate = false;
                if ((fromDate.length() != 0 && toDate.length() != 0)) {
                    try {
                        dateValidate = validate(fromDate, toDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (!dateValidate) {

                    //*********adding field values in arrayList to generate tags in recycler view
                    //we are adding refrence id and file type value in FILE_TYPE parameter
                    //Reference id = UHID or OPD or IPD number *********//
                    String enteredUHIDValue = mUHIDEditText.getText().toString().trim();
//                    if (!mSelectedId.equalsIgnoreCase(getResources().getString(R.string.Select)) && enteredUHIDValue.length() != 0) {
//                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE, mSelectedId + ":" + enteredUHIDValue);
//                    } else if (!mSelectedId.equalsIgnoreCase(getResources().getString(R.string.Select))) {
//                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE, mSelectedId);
//                    } else if (enteredUHIDValue.length() != 0) {
//                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE, enteredUHIDValue);
//                    }

                    if (enteredUHIDValue.length() != 0) {
                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE, mSelectedId + ":" + enteredUHIDValue);
                    }

                    if (!mAdmissionDate.equalsIgnoreCase(getResources().getString(R.string.Select))) {
                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.DATE_TYPE, getString(R.string.date_type) + mAdmissionDate);
                    }

                    if (!fromDate.equalsIgnoreCase(DmsConstants.BLANK)) {
                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.FROM_DATE, getString(R.string.from_date) + mFromDateEditText.getText().toString());
                    }

                    if (!toDate.equalsIgnoreCase(DmsConstants.BLANK)) {
                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.TO_DATE, getString(R.string.to_date) + mToDateEditText.getText().toString());
                    }

                    if (mSearchPatientNameEditText.getText().toString().trim().length() != 0) {
                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, getString(R.string.patient_name) + mSearchPatientNameEditText.getText().toString());
                    }

                    if (mAnnotationEditText.getText().toString().trim().length() != 0) {
                        mAddedTagsForFiltering.put(DmsConstants.PATIENT_LIST_PARAMS.ANNOTATION_TEXT, getString(R.string.enter_annotation) + mAnnotationEditText.getText().toString());
                    }

                    //----------
                    String[] selectedAnnotations = getSelectedAnnotations();
                    if (selectedAnnotations.length > 0) {
                        for (String dataValue :
                                selectedAnnotations) {
                            //--- hashMap Data : childName|id
                            mAddedTagsForFiltering.put(dataValue, dataValue);
                        }
                    }

                    mTagsAdapter = new TagAdapter(mContext, mAddedTagsForFiltering, mAddedTagsEventHandler);
                    mRecycleTag.setAdapter(mTagsAdapter);
                    mDrawer.closeDrawer(GravityCompat.END);
                    doGetPatientList();

                }

                break;
        }
    }


    private void doGetPatientList() {

        ShowSearchResultRequestModel showSearchResultRequestModel = new ShowSearchResultRequestModel();

        String selectedFileType = mTagsAdapter.getUpdatedTagValues(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE, "0");

        //THIS IS DONE BCAZ, FILETYPE contains (IPD/OPD/UHID:enteredID)
        String enteredID = mTagsAdapter.getUpdatedTagValues(DmsConstants.PATIENT_LIST_PARAMS.FILE_TYPE, null);

        showSearchResultRequestModel.setFileType(selectedFileType);

        if (getString(R.string.uhid).equalsIgnoreCase(selectedFileType)) {
            showSearchResultRequestModel.setPatientId(enteredID);
            showSearchResultRequestModel.setReferenceId(DmsConstants.BLANK);
            showSearchResultRequestModel.setFileType(DmsConstants.BLANK);
        } else {
            showSearchResultRequestModel.setPatientId(DmsConstants.BLANK);
            showSearchResultRequestModel.setReferenceId(enteredID);
            if (selectedFileType.equalsIgnoreCase(getResources().getString(R.string.Select))) {
                showSearchResultRequestModel.setFileType(DmsConstants.BLANK);
            }
        }

        showSearchResultRequestModel.setDateType(mTagsAdapter.getUpdatedTagValues(DmsConstants.PATIENT_LIST_PARAMS.DATE_TYPE, null));
        showSearchResultRequestModel.setFromDate(mTagsAdapter.getUpdatedTagValues(DmsConstants.PATIENT_LIST_PARAMS.FROM_DATE, null));
        showSearchResultRequestModel.setToDate(mTagsAdapter.getUpdatedTagValues(DmsConstants.PATIENT_LIST_PARAMS.TO_DATE, null));
        showSearchResultRequestModel.setPatientName(mTagsAdapter.getUpdatedTagValues(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, null));
        showSearchResultRequestModel.setAnnotationText(mTagsAdapter.getUpdatedTagValues(DmsConstants.PATIENT_LIST_PARAMS.ANNOTATION_TEXT, null));

        showSearchResultRequestModel.setDocTypeId(new String[]{mTagsAdapter.getUpdatedTagValues(getString(R.string.documenttype), null)});

        mPatientsHelper.doGetPatientList(showSearchResultRequestModel);
    }

    private void createAnnotationTreeStructure(AnnotationListData annotationListData, boolean isExpanded) {

        mAnnotationTreeViewContainer.removeAllViews();

        TreeNode root = TreeNode.root();
        int lstDocCategoryObjectLeftPadding = (int) (getResources().getDimension(R.dimen.dp30) / getResources().getDisplayMetrics().density);
        int lstDocTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.dp50) / getResources().getDisplayMetrics().density);
        int textColor = ContextCompat.getColor(this, R.color.black);

        List<AnnotationList> annotationLists = annotationListData.getAnnotationLists();

        for (int i = 0; i < annotationLists.size(); i++) {
            AnnotationList annotationCategoryObject = annotationLists.get(i);
            if (i % 2 == 0)
                annotationCategoryObject.setSelected(true);
            ArrowExpandSelectableHeaderHolder selectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded);
            TreeNode folder1 = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, annotationCategoryObject.getCategoryName(), annotationCategoryObject, i))
                    .setViewHolder(selectableHeaderHolder);

            List<DocTypeList> docTypeList = annotationCategoryObject.getDocTypeList();

            for (int j = 0; j < docTypeList.size(); j++) {
                DocTypeList docTypeListObject = docTypeList.get(j);
                if (j % 2 == 0)
                    docTypeListObject.setSelected(true);
                String dataToShow = docTypeListObject.getTypeName() + "|" + docTypeListObject.getTypeId();

                ArrowExpandSelectableHeaderHolder lstDocTypeChildSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDocTypeChildLeftPadding);

                TreeNode lstDocTypeChildFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, docTypeListObject, i))
                        .setViewHolder(lstDocTypeChildSelectableHeaderHolder);

                folder1.addChildren(lstDocTypeChildFolder);
            }
            root.addChildren(folder1);
        }

        mAndroidTreeView = new AndroidTreeView(this, root);
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
                mUHIDEditText.setHint(getResources().getString(R.string.error_enter_uhid));
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
        String parent = "";
        HashSet<String> annotationList = new HashSet<String>();
        if (mAndroidTreeView != null) {
            List<TreeNode> selected = mAndroidTreeView.getSelected();

            if (selected.size() > 0) {
                for (TreeNode data :
                        selected) {

                    String dataValue = data.getValue().toString();
                    //-- This is done for child only, no parent name will come in the list.
                    if (dataValue.contains("|")) {
                        annotationList.add(getString(R.string.documenttype) + dataValue);
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
                                //-------
                                List<DocTypeList> childDocTypeList = tempParentObject.getDocTypeList();
                                for (DocTypeList tempDocTypeObject : childDocTypeList) {
                                    if (tempDocTypeObject.getTypeName().toLowerCase().startsWith(enteredString.toLowerCase())) {
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

    @Override
    public void onCompareDialogShow(final PatientFileData patientFileData1, final PatientFileData patientFileData2, String mCheckedBoxGroupName, final String tempName, boolean isChecked) {

        if (patientFileData2 == null && patientFileData1 == null) {
            mFileOneAdmissionDate.setText("");
            mFileOneType.setText(getString(R.string.adddocument));
            mFileOneIcon.setImageResource(R.drawable.ic_unselected_document);
            mFileTwoAdmissionDate.setText("");
            mFileTwoType.setText(getString(R.string.adddocument));
            mFileTwoIcon.setImageResource(R.drawable.ic_unselected_document);
            mCompareButton.setTextColor(getResources().getColor(R.color.grey_700));
            mCompareButton.setBackground(getResources().getDrawable(R.drawable.compare_button_grey_background));
            mCompareButton.setEnabled(false);

//            mFileOneDischargeDate.setVisibility(View.GONE);
            mFileOneAdmissionDate.setVisibility(View.GONE);

//            mFileTwoDischargeDate.setVisibility(View.GONE);
            mFileTwoAdmissionDate.setVisibility(View.GONE);

            if (!isCompareDialogCollapsed && !isChecked) {
                collapseCompareDialog();
            }
        } else if (patientFileData2 == null && patientFileData1 != null) {

            if (getString(R.string.opd).equals(patientFileData1.getFileType())) {
                String visitOneDate = CommonMethods.formatDateTime(patientFileData1.getAdmissionDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
                mFileOneAdmissionDate.setText("V: " + visitOneDate);
//                mFileOneDischargeDate.setVisibility(View.GONE);
                mFileOneAdmissionDate.setVisibility(View.VISIBLE);
            } else if (getString(R.string.ipd).equals(patientFileData1.getFileType())) {
                String admissionOneDate = CommonMethods.formatDateTime(patientFileData1.getAdmissionDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
                String dischargeOneDate = CommonMethods.formatDateTime(patientFileData1.getDischargeDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
                mFileOneAdmissionDate.setText("A: " + admissionOneDate + " - D: " + dischargeOneDate);
//                mFileOneDischargeDate.setText("D: " + dischargeOneDate);
//                mFileOneDischargeDate.setVisibility(View.VISIBLE);
                mFileOneAdmissionDate.setVisibility(View.VISIBLE);
            }

            mFileOneType.setText(patientFileData1.getFileType() + ":" + patientFileData1.getReferenceId().toString());
            mFileOneIcon.setImageResource(R.drawable.ic_selected_document);
            mCompareButton.setTextColor(getResources().getColor(R.color.grey_700));
            mCompareButton.setBackground(getResources().getDrawable(R.drawable.compare_button_grey_background));
            mCompareButton.setEnabled(false);
            if (isCompareDialogCollapsed)
                expandCompareDialog();
        } else if (patientFileData2 != null && patientFileData1 == null) {

            if (getString(R.string.opd).equals(patientFileData2.getFileType())) {
                String visitTwoDate = CommonMethods.formatDateTime(patientFileData2.getAdmissionDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
                mFileTwoAdmissionDate.setText("V: " + visitTwoDate);

//                mFileTwoDischargeDate.setVisibility(View.GONE);
                mFileTwoAdmissionDate.setVisibility(View.VISIBLE);

            } else if (getString(R.string.ipd).equals(patientFileData2.getFileType())) {
                String admissionTwoDate = CommonMethods.formatDateTime(patientFileData2.getAdmissionDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
                String dischargeTwoDate = CommonMethods.formatDateTime(patientFileData2.getDischargeDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
                mFileTwoAdmissionDate.setText("A: " + admissionTwoDate + " - D: " + dischargeTwoDate);
//                mFileTwoDischargeDate.setText("D: " + dischargeTwoDate);

//                mFileTwoDischargeDate.setVisibility(View.VISIBLE);
                mFileTwoAdmissionDate.setVisibility(View.VISIBLE);
            }

            mFileTwoType.setText(patientFileData2.getFileType() + ":" + patientFileData2.getReferenceId().toString());
            mFileTwoIcon.setImageResource(R.drawable.ic_selected_document);
            mCompareButton.setTextColor(getResources().getColor(R.color.grey_700));
            mCompareButton.setBackground(getResources().getDrawable(R.drawable.compare_button_grey_background));
            mCompareButton.setEnabled(false);
            if (isCompareDialogCollapsed)
                expandCompareDialog();
        } else {

            if (getString(R.string.opd).equals(patientFileData1.getFileType())) {
                String visitOneDate = CommonMethods.formatDateTime(patientFileData1.getAdmissionDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
                mFileOneAdmissionDate.setText("V: " + visitOneDate);

//                mFileOneDischargeDate.setVisibility(View.GONE);
                mFileOneAdmissionDate.setVisibility(View.VISIBLE);

            } else if (getString(R.string.ipd).equals(patientFileData1.getFileType())) {
                String admissionOneDate = CommonMethods.formatDateTime(patientFileData1.getAdmissionDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
                String dischargeOneDate = CommonMethods.formatDateTime(patientFileData1.getDischargeDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
                mFileOneAdmissionDate.setText("A: " + admissionOneDate + " - D: " + dischargeOneDate);
//                mFileOneDischargeDate.setText("D: " + dischargeOneDate);

//                mFileOneDischargeDate.setVisibility(View.VISIBLE);
                mFileOneAdmissionDate.setVisibility(View.VISIBLE);
            }

            if (getString(R.string.opd).equals(patientFileData2.getFileType())) {
                String visitTwoDate = CommonMethods.formatDateTime(patientFileData2.getAdmissionDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
                mFileTwoAdmissionDate.setText("V: " + visitTwoDate);

//                mFileTwoDischargeDate.setVisibility(View.GONE);
                mFileTwoAdmissionDate.setVisibility(View.VISIBLE);

            } else if (getString(R.string.ipd).equals(patientFileData2.getFileType())) {
                String admissionTwoDate = CommonMethods.formatDateTime(patientFileData2.getAdmissionDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
                String dischargeTwoDate = CommonMethods.formatDateTime(patientFileData2.getDischargeDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
                mFileTwoAdmissionDate.setText("A: " + admissionTwoDate + " - D: " + dischargeTwoDate);
//                mFileTwoDischargeDate.setText("D: " + dischargeTwoDate);

//                mFileTwoDischargeDate.setVisibility(View.VISIBLE);
                mFileTwoAdmissionDate.setVisibility(View.VISIBLE);
            }

            mFileOneType.setText(patientFileData1.getFileType() + ":" + patientFileData1.getReferenceId().toString());

            mFileTwoType.setText(patientFileData2.getFileType() + ":" + patientFileData2.getReferenceId().toString());

            mFileOneIcon.setImageResource(R.drawable.ic_selected_document);
            mFileTwoIcon.setImageResource(R.drawable.ic_selected_document);
            mCompareButton.setTextColor(getResources().getColor(R.color.Red));
            mCompareButton.setBackground(getResources().getDrawable(R.drawable.compare_button_red_background));
            mCompareButton.setEnabled(true);
            mCompareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FileTypeViewerActivity.class);
                    Bundle extra = new Bundle();
                    ArrayList<PatientFileData> dataToSend = new ArrayList<PatientFileData>();
                    dataToSend.add(patientFileData1);
                    dataToSend.add(patientFileData2);
                    SearchResult searchPatientInformation = patientExpandableListAdapter.searchPatientInfo(patientFileData1.getRespectiveParentPatientID());
                    extra.putSerializable(getString(R.string.compare), dataToSend);
                    extra.putString(DmsConstants.PATIENT_ADDRESS, searchPatientInformation.getPatientAddress());
                    extra.putString(DmsConstants.DOCTOR_NAME, searchPatientInformation.getDoctorName());
                    extra.putString(DmsConstants.ID, patientFileData1.getRespectiveParentPatientID());
                    extra.putString(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, tempName);
                    intent.putExtra(DmsConstants.DATA, extra);
                    startActivity(intent);
                }
            });
            if (isCompareDialogCollapsed && isChecked)
                expandCompareDialog();
        }
    }

    public void collapseCompareDialog() {
        isCompareDialogCollapsed = true;

        ValueAnimator valueAnimator = ValueAnimator.ofInt(ANIMATION_LAYOUT_MAX_HEIGHT, ANIMATION_LAYOUT_MIN_HEIGHT);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCompareDialogLayout.getLayoutParams();
                params.height = CommonMethods.convertDpToPixel(value.intValue());
                mCompareDialogLayout.setLayoutParams(params);

            }
        });

        valueAnimator.start();

    }

    public void expandCompareDialog() {

        isCompareDialogCollapsed = false;

        ValueAnimator valueAnimator = ValueAnimator.ofInt(ANIMATION_LAYOUT_MIN_HEIGHT, ANIMATION_LAYOUT_MAX_HEIGHT);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCompareDialogLayout.getLayoutParams();
                params.height = CommonMethods.convertDpToPixel(value.intValue());
                mCompareDialogLayout.setLayoutParams(params);

            }
        });

        valueAnimator.start();
    }

    @Override
    public void onPatientListItemClick(PatientFileData childElement, String patientName) {
        Intent intent = new Intent(mContext, FileTypeViewerActivity.class);
        Bundle extra = new Bundle();
        ArrayList<PatientFileData> dataToSend = new ArrayList<PatientFileData>();
        dataToSend.add(childElement);
        SearchResult searchPatientInformation = patientExpandableListAdapter.searchPatientInfo(childElement.getRespectiveParentPatientID());
        extra.putSerializable(getString(R.string.compare), dataToSend);
        extra.putString(DmsConstants.PATIENT_ADDRESS, searchPatientInformation.getPatientAddress());
        extra.putString(DmsConstants.DOCTOR_NAME, searchPatientInformation.getDoctorName());
        extra.putString(DmsConstants.ID, childElement.getRespectiveParentPatientID());
        extra.putString(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, "" + patientName);
        intent.putExtra(DmsConstants.DATA, extra);
        startActivity(intent);
    }

    @Override
    public void smoothScrollToPosition(int previousPosition) {
        mPatientListView.smoothScrollToPosition(previousPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        super.onConfigurationChanged(newConfig);
        ViewGroup.LayoutParams layoutParams = mRightNavigationView.getLayoutParams();
        layoutParams.width = width;
        mRightNavigationView.setLayoutParams(layoutParams);
    }
}

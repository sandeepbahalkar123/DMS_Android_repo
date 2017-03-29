package com.scorg.dms.ui.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.scorg.dms.R;
import com.scorg.dms.helpers.patients.PatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.requestmodel.filetreerequestmodel.FileTreeRequestModel;
import com.scorg.dms.model.requestmodel.filetreerequestmodel.LstSearchParam;
import com.scorg.dms.model.requestmodel.getpdfdatarequestmodel.GetPdfDataRequestModel;
import com.scorg.dms.model.requestmodel.getpdfdatarequestmodel.LstDocTypeRequest;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.ArchiveDatum;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.FileTreeResponseData;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.FileTreeResponseModel;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.LstDocCategory;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.LstDocType;
import com.scorg.dms.model.responsemodel.getpdfdataresponsemodel.GetPdfDataResponseModel;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DmsConstants;
import com.scorg.dms.views.treeViewHolder.arrow_expand.ArrowExpandIconTreeItemHolder;
import com.scorg.dms.views.treeViewHolder.arrow_expand.ArrowExpandSelectableHeaderHolder;
import com.shockwave.pdfium.PdfDocument;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/3/17.
 */


public class FileTypeViewerActivity extends AppCompatActivity implements View.OnClickListener, HelperResponse, OnLoadCompleteListener, OnErrorListener, OnDrawListener, TreeNode.TreeNodeClickListener {

    private static final String TAG = FileTypeViewerActivity.class.getName();
    private Integer mPageNumber = 0;
    private boolean isFirstPdf = true;
    private float mCurrentXOffset = -1;
    private float mCurrentYOffset = -1;
    // End

    private Context mContext;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    //---------
    @BindView(R.id.firstPdfView)
    PDFView mFirstPdfView;
    @BindView(R.id.secondPdfView)
    PDFView mSecondPdfView;

    @BindView(R.id.firstPdfViewFrameLayout)
    FrameLayout mFirstFileTypePdfViewLayout;
    @BindView(R.id.secondPdfViewFrameLayout)
    FrameLayout mSecondFileTypePdfViewLayout;
    //---------

    @BindView(R.id.messageForFirstFile)
    TextView mMessageForFirstFile;
    @BindView(R.id.messageForSecondFile)
    TextView mMessageForSecondFile;
    @BindView(R.id.openRightDrawer)
    ImageView mOpenRightDrawer;
    // End
    TextView mDoctorNameOne;
    TextView mDoctorNameTwo;
    TextView mPatientAddress;
    TextView mFileOneRefId;
    TextView mFileTwoRefId;
    TextView mAdmissionDateOne;
    TextView mAdmissionDateTwo;
    TextView mDischargeDateOne;
    TextView mDischargeDateTwo;
    TextView mPatientId;
    TextView mPatientName;
    TextView mFileTypeOne;
    TextView mFileTypeTwo;
    DrawerLayout mDrawer;

    NavigationView mRightNavigationView;
    View mHeaderView;
    private PatientsHelper mPatientsHelper;

    private RelativeLayout mFileTypeOneTreeViewContainer;

    private Switch mCompareSwitch;
    private TableRow mRowScrollBoth;

    private LinearLayout mFileOneDrawerLayout;
    private LinearLayout mFileTwoDrawerLayout;

    private boolean isCompareChecked = false;

    //---------
    ArrayList<PatientFileData> mSelectedFileTypeDataToCompare;
    String respectivePatientID;
    String patientName;
    String doctorName;
    String patientAddress;
    //---------
    private int mClickedTreeStructureLevel;
    private boolean isTreeInMultipleRootMode = true; // true--> if tree contains two main root like (IPD,OPD) , false--> (IPD OR OPD only)
    private FileTreeResponseData mFileTreeResponseData;

    private RelativeLayout mFirstFileTypeProgressDialogLayout;
    private RelativeLayout mSecondFileTypeProgressDialogLayout;
    private HashMap<Integer, String> mPreviousClickedTreeElement = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_file_type_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {

        Bundle extra = getIntent().getBundleExtra(DmsConstants.DATA);

        if (extra != null) {
            mSelectedFileTypeDataToCompare = (ArrayList<PatientFileData>) extra.getSerializable(getString(R.string.compare));
            respectivePatientID = extra.getString(DmsConstants.ID);
            patientName = extra.getString(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME);
            patientAddress = extra.getString(DmsConstants.PATIENT_ADDRESS);
            doctorName = extra.getString(DmsConstants.DOCTOR_NAME);
        }

        mContext = getApplicationContext();
        //-------------
        mPatientsHelper = new PatientsHelper(this, this);
        mPreviousClickedTreeElement = new HashMap<>();

        bindView();

        //-----------
        mDrawer.openDrawer(GravityCompat.END);
        if (mFileTreeResponseData == null)
            getLoadArchivedList();
        else
            createAnnotationTreeStructure(mFileTreeResponseData, true);
        //-----------
    }

    private void bindView() {
        int width = getResources().getDisplayMetrics().widthPixels / 2;

        //---------
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        toggle.syncState();

        //------RightNavigationView initialize---------
        mRightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);

        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mRightNavigationView.getLayoutParams();
        params.width = width;

        mRightNavigationView.setLayoutParams(params);

        mHeaderView = mRightNavigationView.getHeaderView(0);
        doBindHeaderViews();

        //-------Listeners-----
        mCompareSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCompareChecked = isChecked;
            }
        });
        mRightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });
        mOpenRightDrawer.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //-----------
        mFirstFileTypePdfViewLayout.addView(CommonMethods.loadView(R.layout.mydialog, this));
        mFirstFileTypeProgressDialogLayout = (RelativeLayout) mFirstFileTypePdfViewLayout.findViewById(R.id.progressBarContainerLayout);
        mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
        mSecondFileTypePdfViewLayout.addView(CommonMethods.loadView(R.layout.mydialog, this));
        mSecondFileTypeProgressDialogLayout = (RelativeLayout) mSecondFileTypePdfViewLayout.findViewById(R.id.progressBarContainerLayout);
        mSecondFileTypeProgressDialogLayout.setVisibility(View.GONE);

    }

    private void doBindHeaderViews() {

        mFileOneRefId = (TextView) mHeaderView.findViewById(R.id.fileTypeOneRefID);
        mFileTwoRefId = (TextView) mHeaderView.findViewById(R.id.fileTypeTwoRefID);
        mAdmissionDateOne = (TextView) mHeaderView.findViewById(R.id.fileTypeOneAdmissionDate);
        mAdmissionDateTwo = (TextView) mHeaderView.findViewById(R.id.fileTypeTwoAdmissionDate);
        mDischargeDateOne = (TextView) mHeaderView.findViewById(R.id.fileTypeOneDischargeDate);
        mDischargeDateTwo = (TextView) mHeaderView.findViewById(R.id.fileTypeTwoDischargeDate);
        mPatientId = (TextView) mHeaderView.findViewById(R.id.tvPatientUHID);
        mPatientName = (TextView) mHeaderView.findViewById(R.id.tvPatientName);
        mFileTypeOne = (TextView) mHeaderView.findViewById(R.id.fileTypeOneFileTypeName);
        mFileTypeTwo = (TextView) mHeaderView.findViewById(R.id.fileTypeTwoFileTypeName);
        mDoctorNameOne = (TextView) mHeaderView.findViewById(R.id.fileTypeOneDoctorName);
        mDoctorNameTwo = (TextView) mHeaderView.findViewById(R.id.fileTypeTwoDoctorName);
        mPatientAddress = (TextView) mHeaderView.findViewById(R.id.tvPatientLocation);

        mFileTypeOneTreeViewContainer = (RelativeLayout) mHeaderView.findViewById(R.id.fileTypeTreeViewContainer);
        mCompareSwitch = (Switch) mHeaderView.findViewById(R.id.comparePdfOnOFF);
        mRowScrollBoth = (TableRow) mHeaderView.findViewById(R.id.rowScrollBoth);
        mFileOneDrawerLayout = (LinearLayout) mHeaderView.findViewById(R.id.fileOneLay);
        mFileTwoDrawerLayout = (LinearLayout) mHeaderView.findViewById(R.id.fileTwoLay);

        mPatientName.setText(patientName);
        mDoctorNameTwo.setText(doctorName);
        mDoctorNameOne.setText(doctorName);
        mPatientAddress.setText(patientAddress);

        //----------
        PatientFileData patientFileData = mSelectedFileTypeDataToCompare.get(0);
        mFileOneRefId.setText(String.valueOf(patientFileData.getReferenceId()));
        mAdmissionDateOne.setText(String.valueOf(patientFileData.getAdmissionDate()));
        mDischargeDateOne.setText(String.valueOf(patientFileData.getDischargeDate()));
        mFileTypeOne.setText(String.valueOf(patientFileData.getFileType()));
        mPatientId.setText(String.valueOf(patientFileData.getRespectiveParentPatientID()));
        //----------

        if (mSelectedFileTypeDataToCompare.size() == 2) {

            mSecondFileTypePdfViewLayout.setVisibility(View.VISIBLE);
            mRowScrollBoth.setVisibility(View.VISIBLE);
            mFileTwoDrawerLayout.setVisibility(View.VISIBLE);

            patientFileData = mSelectedFileTypeDataToCompare.get(1);
            mFileTwoRefId.setText(String.valueOf(patientFileData.getReferenceId()));
            mAdmissionDateTwo.setText(String.valueOf(patientFileData.getAdmissionDate()));
            mDischargeDateTwo.setText(String.valueOf(patientFileData.getDischargeDate()));
            mFileTypeTwo.setText(String.valueOf(patientFileData.getFileType()));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openRightDrawer:
                mDrawer.openDrawer(GravityCompat.END);
                if (mFileTreeResponseData == null)
                    getLoadArchivedList();
                else
                    createAnnotationTreeStructure(mFileTreeResponseData, true);

                break;
        }
    }

    // To create JSON and get archived list of data.
    private void getLoadArchivedList() {
        //---------------
        FileTreeRequestModel fileTreeRequestModel = new FileTreeRequestModel();

        List<LstSearchParam> lstSearchParamList = new ArrayList<>();
        if (mSelectedFileTypeDataToCompare != null) {
            for (PatientFileData tempObject :
                    mSelectedFileTypeDataToCompare) {
                LstSearchParam lstSearchParam = new LstSearchParam();
                lstSearchParam.setPatientId(tempObject.getRespectiveParentPatientID());
                lstSearchParam.setFileType(tempObject.getFileType());
                lstSearchParam.setFileTypeRefId("" + tempObject.getReferenceId());
                lstSearchParamList.add(lstSearchParam);
            }
            fileTreeRequestModel.setLstSearchParam(lstSearchParamList);
            mPatientsHelper.doGetArchivedList(fileTreeRequestModel);
        }
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        //Changed from switch to if..else, due to dynamically comparing received mOldDataTag.
        if (mOldDataTag.startsWith(DmsConstants.TASK_GET_PDF_DATA + mClickedTreeStructureLevel)) {
            GetPdfDataResponseModel getPdfDataResponseModel = (GetPdfDataResponseModel) customResponse;
            if (getPdfDataResponseModel.getCommon().getStatusCode().equals(DmsConstants.SUCCESS)) {
                String fileData = getPdfDataResponseModel.getGetPdfDataResponseData().getFileData();
                if (fileData != null) {
                    switch (mClickedTreeStructureLevel) {
                        case 0:
                            mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
                            loadPDFFromServer(mFirstPdfView, fileData, "file1", "pdf");
                            break;
                        case 1:
                            mSecondFileTypeProgressDialogLayout.setVisibility(View.GONE);
                            loadPDFFromServer(mSecondPdfView, fileData, "file2", "pdf");
                            break;
                    }
                } else
                    Toast.makeText(mContext, "Document not available", Toast.LENGTH_SHORT).show();
            }

        } else if (String.valueOf(mOldDataTag).equalsIgnoreCase("" + DmsConstants.TASK_GET_ARCHIVED_LIST)) {
            FileTreeResponseModel fileTreeResponseModel = (FileTreeResponseModel) customResponse;
            mFileTreeResponseData = fileTreeResponseModel.getFileTreeResponseData();
            createAnnotationTreeStructure(mFileTreeResponseData, true);
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


    private void createAnnotationTreeStructure(FileTreeResponseData fileTreeResponseData, boolean isExpanded) {

        mFileTypeOneTreeViewContainer.removeAllViews();

        TreeNode treeRoot = TreeNode.root();

        int lstDocCategoryObjectLeftPadding = (int) (getResources().getDimension(R.dimen.dp30) / getResources().getDisplayMetrics().density);
        int lstDocTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.dp50) / getResources().getDisplayMetrics().density);
        int textColor = ContextCompat.getColor(this, R.color.black);

        List<ArchiveDatum> archiveData = fileTreeResponseData.getArchiveData();
        int size = archiveData.size();

        // To set mode of opening tree.
        isTreeInMultipleRootMode = (size > 0) ? true : false;

        // For archived data list
        for (int i = 0; i < size; i++) {
            ArchiveDatum archiveDatumObject = archiveData.get(i);

            ArrowExpandSelectableHeaderHolder selectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded);
            selectableHeaderHolder.setNodeValueColor(textColor);

            //---- To bold clicked text in tree
            if (archiveDatumObject.getFileType().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                selectableHeaderHolder.setTreeLabelBold(true);

            // Label(pageCount)|NA
            String dataToShow = archiveDatumObject.getFileType() + " (" + archiveDatumObject.getTotalDocCategoryPageCount() + ")" + "|NA";
            TreeNode archiveDatumObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, archiveDatumObject, i))
                    .setViewHolder(selectableHeaderHolder);

            //---- For list categories loop
            List<LstDocCategory> lstDocCategories = archiveDatumObject.getLstDocCategories();

            for (int j = 0; j < lstDocCategories.size(); j++) {
                LstDocCategory lstDocCategoryObject = lstDocCategories.get(j);

                // Label(pageCount)|id
                dataToShow = lstDocCategoryObject.getCategoryName() + " (" + lstDocCategoryObject.getTotalDocTypePageCount() + ")" + "|" + lstDocCategoryObject.getCategoryId();

                ArrowExpandSelectableHeaderHolder docCatSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDocCategoryObjectLeftPadding);
                docCatSelectableHeaderHolder.setNodeValueColor(textColor);

                //---- To bold clicked text in tree
                if (lstDocCategoryObject.getCategoryName().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                    docCatSelectableHeaderHolder.setTreeLabelBold(true);

                TreeNode lstDocCategoryObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDocCategoryObject, i))
                        .setViewHolder(docCatSelectableHeaderHolder);
                //---

                //for lstDocTypes loop
                List<LstDocType> lstDocTypesCategoriesChildList = lstDocCategoryObject.getLstDocTypes();
                for (int k = 0; k < lstDocTypesCategoriesChildList.size(); k++) {
                    LstDocType lstDocTypeChild = lstDocTypesCategoriesChildList.get(k);

                    // Label(pageCount)|id
                    dataToShow = lstDocTypeChild.getTypeName() + " (" + lstDocTypeChild.getPageCount() + ")" + "|" + lstDocTypeChild.getTypeId();

                    //-------
                    ArrowExpandSelectableHeaderHolder lstDocTypeChildSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDocTypeChildLeftPadding);
                    lstDocTypeChildSelectableHeaderHolder.setNodeValueColor(textColor);

                    //---- To bold clicked text in tree
                    if (lstDocTypeChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                        lstDocTypeChildSelectableHeaderHolder.setTreeLabelBold(true);

                    TreeNode lstDocTypeChildFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDocTypeChild, i))
                            .setViewHolder(lstDocTypeChildSelectableHeaderHolder);
                    //-------
                    lstDocCategoryObjectFolder.addChildren(lstDocTypeChildFolder);
                }
                archiveDatumObjectFolder.addChildren(lstDocCategoryObjectFolder);
            }
            treeRoot.addChildren(archiveDatumObjectFolder);
        }

        AndroidTreeView mAndroidTreeView = new AndroidTreeView(this, treeRoot);
        mAndroidTreeView.setDefaultAnimation(true);
        mAndroidTreeView.setUse2dScroll(true);
        mAndroidTreeView.setDefaultNodeClickListener(this);
        mAndroidTreeView.setUseAutoToggle(false);
        mFileTypeOneTreeViewContainer.addView(mAndroidTreeView.getView());

    }


    @Override
    public void onLayerDrawn(PDFView pdfView, Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

        if (isCompareChecked) {

            if (pdfView.getCurrentXOffset() == mCurrentXOffset && pdfView.getCurrentYOffset() == mCurrentYOffset) {
                Log.d(TAG + " onLayerDrawnSame", displayedPage + " " + pdfView.getCurrentXOffset() + " " + pdfView.getCurrentYOffset());
            } else {

                Log.d(TAG + " onLayerDrawnDifferent", displayedPage + " " + pdfView.getCurrentXOffset() + " " + pdfView.getCurrentYOffset());

                if (isFirstPdf && mFirstPdfView == pdfView) {
                    mSecondPdfView.jumpTo(displayedPage);
                    mSecondPdfView.zoomWithAnimation(mFirstPdfView.getZoom());
                    mSecondPdfView.moveTo(mFirstPdfView.getCurrentXOffset(), mFirstPdfView.getCurrentYOffset());

                } else if (!isFirstPdf && mSecondPdfView == pdfView) {
                    mFirstPdfView.jumpTo(displayedPage);
                    mFirstPdfView.zoomWithAnimation(mSecondPdfView.getZoom());
                    mFirstPdfView.moveTo(mSecondPdfView.getCurrentXOffset(), mSecondPdfView.getCurrentYOffset());
                }
            }

            mCurrentXOffset = pdfView.getCurrentXOffset();
            mCurrentYOffset = pdfView.getCurrentYOffset();

        }
    }

    @Override
    public void onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
            if (v == mFirstPdfView) {
                isFirstPdf = true;
            } else {
                isFirstPdf = false;
            }
        }
    }

    @Override
    public void onError(PDFView pdfView, Throwable t) {
        if (mFirstPdfView == pdfView) {
            mMessageForFirstFile.setVisibility(View.VISIBLE);
            mMessageForFirstFile.setText(getString(R.string.filenotfound));
        } else if (mSecondPdfView == pdfView) {
            mMessageForSecondFile.setVisibility(View.VISIBLE);
            mMessageForSecondFile.setText(getString(R.string.filenotfound));
        }
    }

    @Override
    public void loadComplete(PDFView pdfView, int nbPages) {
        try {
            PdfDocument.Meta meta = null;
            if (pdfView == mFirstPdfView) {
                mMessageForFirstFile.setVisibility(View.GONE);
                meta = mFirstPdfView.getDocumentMeta();
                printBookmarksTree(mFirstPdfView.getTableOfContents(), "-");
            } else if (pdfView == mSecondPdfView) {
                mMessageForSecondFile.setVisibility(View.GONE);
                meta = mSecondPdfView.getDocumentMeta();
                printBookmarksTree(mSecondPdfView.getTableOfContents(), "-");
            }
            if (meta != null) {
                String dataToShow = "title = " + meta.getTitle()
                        + "author = " + meta.getAuthor()
                        + "subject = " + meta.getSubject()
                        + "keywords = " + meta.getKeywords()
                        + "creator = " + meta.getCreator()
                        + "producer = " + meta.getProducer()
                        + "creationDate = " + meta.getCreationDate()
                        + "modDate = " + meta.getModDate();
                CommonMethods.Log(TAG, "title = " + dataToShow);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            CommonMethods.Log(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    // End

    //-- For treeview annotations
    @Override
    public void onClick(TreeNode node, Object value) {

        mDrawer.closeDrawer(GravityCompat.END);


        GetPdfDataRequestModel getPdfDataRequestModel = new GetPdfDataRequestModel();
        getPdfDataRequestModel.setPatientId(respectivePatientID);

        if (value instanceof ArrowExpandIconTreeItemHolder.IconTreeItem) {
            ArrowExpandIconTreeItemHolder.IconTreeItem value1 = (ArrowExpandIconTreeItemHolder.IconTreeItem) value;

            //----- THIS IS TO FIND OUT, WHICH level of treeView clicked
            mClickedTreeStructureLevel = value1.level;

            //--- Set fileType and refID based on mClickedTreeStructureLevel
            getPdfDataRequestModel.setFileType(mSelectedFileTypeDataToCompare.get(mClickedTreeStructureLevel).getFileType());
            getPdfDataRequestModel.setFileTypeRefId("" + mSelectedFileTypeDataToCompare.get(mClickedTreeStructureLevel).getReferenceId());

            //----- Get Object of clicked Element and create map to send  : Start------
            if (value1.objectData instanceof ArchiveDatum) {
                ArchiveDatum tempData = (ArchiveDatum) value1.objectData;
                List<LstDocCategory> lstDocCategories = tempData.getLstDocCategories();
                mPreviousClickedTreeElement.put(mClickedTreeStructureLevel, tempData.getFileType());
                getPdfDataRequestModel.setLstDocTypeRequests(createLstDocTypeRequest(lstDocCategories));

            } else if (value1.objectData instanceof LstDocCategory) {
                LstDocCategory objectData = (LstDocCategory) value1.objectData;
                List<LstDocCategory> lstDocCategories = new ArrayList<>();
                lstDocCategories.add(objectData);
                mPreviousClickedTreeElement.put(mClickedTreeStructureLevel, objectData.getCategoryName());
                getPdfDataRequestModel.setLstDocTypeRequests(createLstDocTypeRequest(lstDocCategories));
            } else if (value1.objectData instanceof LstDocType) {
                LstDocType tempData = (LstDocType) value1.objectData;
                mPreviousClickedTreeElement.put(mClickedTreeStructureLevel, tempData.getTypeName());
                //----
                List<LstDocType> lstDocType = new ArrayList<>();
                lstDocType.add(tempData);
                //-----
                List<LstDocCategory> lstDocCategories = new ArrayList<>();
                LstDocCategory temp = new LstDocCategory();
                temp.setLstDocTypes(lstDocType);
                lstDocCategories.add(temp);
                //---------
                getPdfDataRequestModel.setLstDocTypeRequests(createLstDocTypeRequest(lstDocCategories));
            }
            //----- Get Object of clicked Element and create map to send  : End------

            List<LstDocTypeRequest> lstDocTypeRequestsToFetchFromServer = getPdfDataRequestModel.getLstDocTypeRequests();

            if (isTreeInMultipleRootMode) {
                switch (mClickedTreeStructureLevel) {
                    case 0:
                        doCallPDFDataService(getPdfDataRequestModel, lstDocTypeRequestsToFetchFromServer.size(), mFirstPdfView, mFirstFileTypeProgressDialogLayout, mFirstFileTypePdfViewLayout);
                        break;
                    case 1:
                        doCallPDFDataService(getPdfDataRequestModel, lstDocTypeRequestsToFetchFromServer.size(), mSecondPdfView, mSecondFileTypeProgressDialogLayout, mSecondFileTypePdfViewLayout);
                        break;
                }
            } else {
                doCallPDFDataService(getPdfDataRequestModel, lstDocTypeRequestsToFetchFromServer.size(), mFirstPdfView, mFirstFileTypeProgressDialogLayout, mFirstFileTypePdfViewLayout);
                doCallPDFDataService(getPdfDataRequestModel, lstDocTypeRequestsToFetchFromServer.size(), mSecondPdfView, mSecondFileTypeProgressDialogLayout, mSecondFileTypePdfViewLayout);
            }
        }
    }

    // TO create object to pass to helper
    private List<LstDocTypeRequest> createLstDocTypeRequest(List<LstDocCategory> lstDocCategories) {

        List<LstDocTypeRequest> docList = new ArrayList<>();

        for (LstDocCategory tempCat :
                lstDocCategories) {
            List<LstDocType> lstDocTypes = tempCat.getLstDocTypes();

            for (LstDocType tempDocObject :
                    lstDocTypes) {
                LstDocTypeRequest lstDocTypeRequest = new LstDocTypeRequest();
                lstDocTypeRequest.setTypeId(tempDocObject.getTypeId());
                lstDocTypeRequest.setTypeName(tempDocObject.getTypeName());
                lstDocTypeRequest.setAbbreviation(tempDocObject.getAbbreviation());
                lstDocTypeRequest.setCreatedDate(tempDocObject.getCreatedDate());
                lstDocTypeRequest.setPageCount(tempDocObject.getPageCount());
                lstDocTypeRequest.setPageNumber(tempDocObject.getPageNumber());
                docList.add(lstDocTypeRequest);
            }
        }
        return docList;
    }

    private void loadPDFFromServer(PDFView pdfViewToLoad, String base64Pdf, String fileName, String extension) {
        pdfViewToLoad.fromFile(new File(CommonMethods.getCachePath(this, base64Pdf, fileName, extension)))
                .defaultPage(mPageNumber)
                .onError(this)
                .onDraw(this)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }


    private void doCallPDFDataService(GetPdfDataRequestModel getPdfDataRequestModel, int size, PDFView pdfView, RelativeLayout progressBarLayout, FrameLayout pdfContainerLayout) {
        //-----TO grayed out pdfview based on no element in that view -----
        if (size != 0) {
            pdfView.setVisibility(View.VISIBLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            pdfContainerLayout.setBackgroundResource(R.drawable.pdfdecoration);
        } else {
            pdfView.setVisibility(View.GONE);
            pdfContainerLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.Gray));
        }
        mPatientsHelper.getPdfData(getPdfDataRequestModel, (DmsConstants.TASK_GET_PDF_DATA + mClickedTreeStructureLevel));
    }
}

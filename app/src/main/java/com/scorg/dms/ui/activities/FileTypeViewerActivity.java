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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
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
import com.scorg.dms.views.treeViewHolder.IconTreeItemHolder;
import com.shockwave.pdfium.PdfDocument;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/3/17.
 */

public class FileTypeViewerActivity extends AppCompatActivity implements View.OnClickListener, HelperResponse, OnPageChangeListener, OnLoadCompleteListener, OnDrawListener, TreeNode.TreeNodeClickListener {

    private static final String TAG = FileTypeViewerActivity.class.getName();
    private static final String SAMPLE_FILE_1 = "sample.pdf";
    private Integer pageNumber = 0;
    // End

    private Context mContext;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    // Ganesh Added
    @BindView(R.id.firstPdfView)
    PDFView mFirstFileTypePdfView;
    @BindView(R.id.secondPdfView)
    PDFView mSecondFileTypePdfView;
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

    private AndroidTreeView mAndroidTreeView;

    //---------
    ArrayList<PatientFileData> mSelectedFileTypeDataToCompare;
    String respectivePatientID;
    String patientName;
    String doctorName;
    String patientAddress;
    private TreeNode mTreeRoot;
    //---------
    private boolean mLoadPDFInFirstPDFView = true; // false for second pdfview.

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

        initializeVariables();
        bindView();


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

        mRightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);
        mHeaderView = mRightNavigationView.getHeaderView(0);


        //---------------
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mRightNavigationView.getLayoutParams();
        params.width = width;

        mRightNavigationView.setLayoutParams(params);

        mRightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });

        //------------
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

        mFileOneRefId.setText(mSelectedFileTypeDataToCompare.get(0).getReferenceId().toString());
        mAdmissionDateOne.setText(mSelectedFileTypeDataToCompare.get(0).getAdmissionDate().toString());
        mDischargeDateOne.setText(mSelectedFileTypeDataToCompare.get(0).getDischargeDate().toString());
        mFileTypeOne.setText(mSelectedFileTypeDataToCompare.get(0).getFileType().toString());

        mFileTwoRefId.setText(mSelectedFileTypeDataToCompare.get(1).getReferenceId().toString());
        mAdmissionDateTwo.setText(mSelectedFileTypeDataToCompare.get(1).getAdmissionDate().toString());
        mDischargeDateTwo.setText(mSelectedFileTypeDataToCompare.get(1).getDischargeDate().toString());
        mFileTypeTwo.setText(mSelectedFileTypeDataToCompare.get(1).getFileType().toString());
        mPatientId.setText(mSelectedFileTypeDataToCompare.get(0).getRespectiveParentPatientID().toString());
        mPatientName.setText(patientName);
        mDoctorNameTwo.setText(doctorName);
        mDoctorNameOne.setText(doctorName);
        mPatientAddress.setText(patientAddress);

        mFileTypeOneTreeViewContainer = (RelativeLayout) mHeaderView.findViewById(R.id.fileTypeTreeViewContainer);

        //------------
        mOpenRightDrawer.setOnClickListener(this);
        //-----------

    }

    private void initializeVariables() {
        mContext = getApplicationContext();
        //-------------
        mPatientsHelper = new PatientsHelper(this, this);
        //------------
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openRightDrawer:
                mDrawer.openDrawer(GravityCompat.END);
                getLoadArchivedList();
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
    public void onSuccess(int mOldDataTag, CustomResponse customResponse) {
        switch (mOldDataTag) {
            case DmsConstants.TASK_GET_PDF_DATA:
                GetPdfDataResponseModel getPdfDataResponseModel = (GetPdfDataResponseModel) customResponse;
                if (getPdfDataResponseModel.getCommon().getStatusCode().equals(DmsConstants.SUCCESS)) {
                    String fileData = getPdfDataResponseModel.getGetPdfDataResponseData().getFileData();
                    if (fileData != null) {
                        if (mLoadPDFInFirstPDFView) {
                            loadPDFFromServer(mFirstFileTypePdfView, fileData, "file1", "pdf");
                        } else {
                            loadPDFFromServer(mSecondFileTypePdfView, fileData, "file2", "pdf");
                        }
                    } else
                        Toast.makeText(mContext, "Document not available", Toast.LENGTH_SHORT).show();
                }
                break;
            case DmsConstants.TASK_GET_ARCHIVED_LIST:
                FileTreeResponseModel fileTreeResponseModel = (FileTreeResponseModel) customResponse;
                FileTreeResponseData fileTreeResponseData = fileTreeResponseModel.getFileTreeResponseData();
                createAnnotationTreeStructure(fileTreeResponseData, true);

                break;
        }
    }

    @Override
    public void onParseError(int mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(int mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(int mOldDataTag, String serverErrorMessage) {

    }


    private void createAnnotationTreeStructure(FileTreeResponseData fileTreeResponseData, boolean isExpanded) {

        mFileTypeOneTreeViewContainer.removeAllViews();

        mTreeRoot = TreeNode.root();

        int lstDocCategoryObjectLeftPadding = (int) (getResources().getDimension(R.dimen.dp30) / getResources().getDisplayMetrics().density);
        int lstDocTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.dp50) / getResources().getDisplayMetrics().density);
        int textColor = ContextCompat.getColor(this, R.color.white);

        List<ArchiveDatum> archiveData = fileTreeResponseData.getArchiveData();

        // For archived data list
        for (int i = 0; i < archiveData.size(); i++) {
            ArchiveDatum archiveDatumObject = archiveData.get(i);

            ArrowExpandSelectableHeaderHolder selectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded);
            selectableHeaderHolder.setNodeValueColor(textColor);

            // Label|NA @ fileOne Or fileTwo count id
            String dataToShow = archiveDatumObject.getFileType() + "|NA" + "@" + (i + 1);
            TreeNode archiveDatumObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, archiveDatumObject))
                    .setViewHolder(selectableHeaderHolder);

            //---- For list categories loop
            List<LstDocCategory> lstDocCategories = archiveDatumObject.getLstDocCategories();

            for (int j = 0; j < lstDocCategories.size(); j++) {
                LstDocCategory lstDocCategoryObject = lstDocCategories.get(j);

                // Label|id @ fileOne Or fileTwo count id
                dataToShow = lstDocCategoryObject.getCategoryName() + "|" + lstDocCategoryObject.getCategoryId() + "@" + (i + 1);

                ArrowExpandSelectableHeaderHolder docCatSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDocCategoryObjectLeftPadding);
                docCatSelectableHeaderHolder.setNodeValueColor(textColor);

                TreeNode lstDocCategoryObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDocCategoryObject))
                        .setViewHolder(docCatSelectableHeaderHolder);
                //---

                //for lstDocTypes loop
                List<LstDocType> lstDocTypesCategoriesChildList = lstDocCategoryObject.getLstDocTypes();
                for (int k = 0; k < lstDocTypesCategoriesChildList.size(); k++) {
                    LstDocType lstDocTypeChild = lstDocTypesCategoriesChildList.get(k);

                    // Label|id @ fileOne Or fileTwo count id
                    dataToShow = lstDocTypeChild.getTypeName() + "|" + lstDocTypeChild.getTypeId() + "@" + (i + 1);

                    //-------
                    ArrowExpandSelectableHeaderHolder lstDocTypeChildSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDocTypeChildLeftPadding);
                    lstDocTypeChildSelectableHeaderHolder.setNodeValueColor(textColor);

                    TreeNode lstDocTypeChildFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDocTypeChild))
                            .setViewHolder(lstDocTypeChildSelectableHeaderHolder);
                    //-------
                    lstDocCategoryObjectFolder.addChildren(lstDocTypeChildFolder);
                }
                archiveDatumObjectFolder.addChildren(lstDocCategoryObjectFolder);
            }
            mTreeRoot.addChildren(archiveDatumObjectFolder);
        }

        mAndroidTreeView = new AndroidTreeView(this, mTreeRoot);
        mAndroidTreeView.setDefaultAnimation(true);
        mAndroidTreeView.setUse2dScroll(true);
        mAndroidTreeView.setDefaultNodeClickListener(this);
        mAndroidTreeView.setUseAutoToggle(false);
        mFileTypeOneTreeViewContainer.addView(mAndroidTreeView.getView());
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
//        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
        mSecondFileTypePdfView.jumpTo(page);

    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

        mSecondFileTypePdfView.zoomWithAnimation(mFirstFileTypePdfView.getZoom());
        mSecondFileTypePdfView.moveTo(mFirstFileTypePdfView.getCurrentXOffset(), mFirstFileTypePdfView.getCurrentYOffset());

    }

    @Override
    public void loadComplete(int nbPages) {
        try {
            PdfDocument.Meta meta = mFirstFileTypePdfView.getDocumentMeta();
            Log.e(TAG, "title = " + meta.getTitle());
            Log.e(TAG, "author = " + meta.getAuthor());
            Log.e(TAG, "subject = " + meta.getSubject());
            Log.e(TAG, "keywords = " + meta.getKeywords());
            Log.e(TAG, "creator = " + meta.getCreator());
            Log.e(TAG, "producer = " + meta.getProducer());
            Log.e(TAG, "creationDate = " + meta.getCreationDate());
            Log.e(TAG, "modDate = " + meta.getModDate());

            PdfDocument.Meta meta1 = mSecondFileTypePdfView.getDocumentMeta();
            Log.e(TAG, "title = " + meta1.getTitle());
            Log.e(TAG, "author = " + meta1.getAuthor());
            Log.e(TAG, "subject = " + meta1.getSubject());
            Log.e(TAG, "keywords = " + meta1.getKeywords());
            Log.e(TAG, "creator = " + meta1.getCreator());
            Log.e(TAG, "producer = " + meta1.getProducer());
            Log.e(TAG, "creationDate = " + meta1.getCreationDate());
            Log.e(TAG, "modDate = " + meta1.getModDate());

            printBookmarksTree(mFirstFileTypePdfView.getTableOfContents(), "-");
            printBookmarksTree(mSecondFileTypePdfView.getTableOfContents(), "-");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

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
        getPdfDataRequestModel.setFileType(mSelectedFileTypeDataToCompare.get(0).getFileType());
        getPdfDataRequestModel.setFileTypeRefId("" + mSelectedFileTypeDataToCompare.get(0).getReferenceId());

        if (value instanceof ArrowExpandIconTreeItemHolder.IconTreeItem) {
            ArrowExpandIconTreeItemHolder.IconTreeItem value1 = (ArrowExpandIconTreeItemHolder.IconTreeItem) value;

            //----- THIS IS TO FIND OUT, WHICH ITEM OF TREEVIEW IS CLICKED (EX. FileONE OR FileTWO PDF VIEW)
            String nodeValue = value1.text.toString();
            if (nodeValue.contains("@")) {
                String[] split = nodeValue.split("@");
                if (split[1].equalsIgnoreCase("1")) {
                    mLoadPDFInFirstPDFView = true;
                } else {
                    mLoadPDFInFirstPDFView = false;
                }
            }
            //-----------

            if (value1.objectData instanceof ArchiveDatum) {
                ArchiveDatum tempData = (ArchiveDatum) value1.objectData;
                List<LstDocCategory> lstDocCategories = tempData.getLstDocCategories();
                getPdfDataRequestModel.setLstDocTypeRequests(createLstDocTypeRequest(lstDocCategories));

            } else if (value1.objectData instanceof LstDocCategory) {
                List<LstDocCategory> lstDocCategories = new ArrayList<>();
                lstDocCategories.add((LstDocCategory) value1.objectData);
                getPdfDataRequestModel.setLstDocTypeRequests(createLstDocTypeRequest(lstDocCategories));
            } else if (value1.objectData instanceof LstDocType) {
                LstDocType tempData = (LstDocType) value1.objectData;

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
            // call api
            mPatientsHelper.getPdfData(getPdfDataRequestModel);
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
                .defaultPage(pageNumber)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }
}
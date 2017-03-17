package com.scorg.dms.ui.activities;

import android.content.Context;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.scorg.dms.model.requestmodel.getpdfdatarequestmodel.DocTypeRequest;
import com.scorg.dms.model.requestmodel.getpdfdatarequestmodel.GetPdfDataRequestModel;
import com.scorg.dms.model.requestmodel.getpdfdatarequestmodel.LstDocTypeRequest;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.ArchiveDatum;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.FileTreeResponseData;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.FileTreeResponseModel;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.LstDocCategory;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.LstDocType;
import com.scorg.dms.model.responsemodel.getpdfdataresponsemodel.GetPdfDataResponseModel;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.scorg.dms.util.DmsConstants;
import com.scorg.dms.views.treeViewHolder.IconTreeItemHolder;
import com.scorg.dms.views.treeViewHolder.SelectableHeaderHolder;
import com.scorg.dms.views.treeViewHolder.SelectableItemHolder;
import com.shockwave.pdfium.PdfDocument;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 14/3/17.
 */

public class FileTypeViewerActivity extends AppCompatActivity implements View.OnClickListener, HelperResponse, OnPageChangeListener, OnLoadCompleteListener, OnDrawListener {

    // Ganesh Added

    private static final String TAG = FileTypeViewerActivity.class.getName();
    private static final String SAMPLE_FILE_1 = "sample.pdf";
    private Integer pageNumber = 0;

    GetPdfDataRequestModel getPdfDataRequestModel = new GetPdfDataRequestModel();

    // End

    private Context mContext;

    /*@BindView(R.id.openCompareFileTypeRightDrawerFAB)
    FloatingActionButton mOpenCompareFileTypeRightDrawerFAB;*/
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    // Ganesh Added
    @BindView(R.id.firstPdfView)
    PDFView firstPdfView;
    @BindView(R.id.secondPdfView)
    PDFView secondPdfView;
    // End

    DrawerLayout mDrawer;
    NavigationView mRightNavigationView;
    View mHeaderView;
    private PatientsHelper mPatientsHelper;

    private RelativeLayout mFileTypeOneTreeViewContainer;

    //TODO: This is not using currently
    private RelativeLayout mFileTypeTwoTreeViewContainer;
    private AndroidTreeView mAndroidTreeView;

    //---------
    ArrayList<PatientFileData> mSelectedFileTypeDataToCompare;
    String respectivePatientID;
    private Button mApplyFileTypeDataLoading;
    private TreeNode mTreeRoot;
    //---------

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
        mFileTypeOneTreeViewContainer = (RelativeLayout) mHeaderView.findViewById(R.id.fileTypeOneTreeViewContainer);
        mFileTypeTwoTreeViewContainer = (RelativeLayout) mHeaderView.findViewById(R.id.fileTypeTwoTreeViewContainer);
        mApplyFileTypeDataLoading = (Button) mHeaderView.findViewById(R.id.applyFileTypeDataLoading);

        //------------
        mApplyFileTypeDataLoading.setOnClickListener(this);
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
            case R.id.applyFileTypeDataLoading:
                applySelectedArchived();
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    //open right drawer onclick of settings icon
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mDrawer.openDrawer(GravityCompat.END);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSuccess(int mOldDataTag, CustomResponse customResponse) {
        switch (mOldDataTag) {
            case DmsConstants.TASK_GET_PDF_DATA:
                GetPdfDataResponseModel getPdfDataResponseModel = (GetPdfDataResponseModel) customResponse;
                if (getPdfDataResponseModel.getCommon().getStatusCode().equals(DmsConstants.SUCCESS)) {
                    if (getPdfDataResponseModel.getGetPdfDataResponseData().getFileData() != null)
                        displayFromUrl(getPdfDataResponseModel.getGetPdfDataResponseData().getFileData());
                    else
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
        mFileTypeTwoTreeViewContainer.removeAllViews();

        mTreeRoot = TreeNode.root();

        int lstDocCategoryObjectLeftPadding = (int) (getResources().getDimension(R.dimen.dp30) / getResources().getDisplayMetrics().density);
        int lstDocTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.dp50) / getResources().getDisplayMetrics().density);

        List<ArchiveDatum> archiveData = fileTreeResponseData.getArchiveData();

        // For archived data list
        for (int i = 0; i < archiveData.size(); i++) {
            ArchiveDatum archiveDatumObject = archiveData.get(i);

            SelectableHeaderHolder selectableHeaderHolder = new SelectableHeaderHolder(this, isExpanded);
            TreeNode archiveDatumObjectFolder = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, archiveDatumObject.getFileType()))
                    .setViewHolder(selectableHeaderHolder);

            //---- For list categories loop
            List<LstDocCategory> lstDocCategories = archiveDatumObject.getLstDocCategories();

            for (int j = 0; j < lstDocCategories.size(); j++) {
                LstDocCategory lstDocCategoryObject = lstDocCategories.get(j);
                String dataToShow = lstDocCategoryObject.getCategoryName() + "|" + lstDocCategoryObject.getCategoryId();

                SelectableHeaderHolder docCatSelectableHeaderHolder = new SelectableHeaderHolder(this, isExpanded, lstDocCategoryObjectLeftPadding);
                TreeNode lstDocCategoryObjectFolder = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow))
                        .setViewHolder(docCatSelectableHeaderHolder);
                //---

                //for lstDocTypes loop
                List<LstDocType> lstDocTypesCategoriesChildList = lstDocCategoryObject.getLstDocTypes();
                for (int k = 0; k < lstDocTypesCategoriesChildList.size(); k++) {
                    LstDocType lstDocTypeChild = lstDocTypesCategoriesChildList.get(k);
                    dataToShow = lstDocTypeChild.getTypeName() + "|" + lstDocTypeChild.getTypeId();

                    TreeNode lstDocTypeChildFolder = new TreeNode(dataToShow).setViewHolder(new SelectableItemHolder(this, lstDocTypeChildLeftPadding));
                    lstDocCategoryObjectFolder.addChildren(lstDocTypeChildFolder);
                }
                archiveDatumObjectFolder.addChildren(lstDocCategoryObjectFolder);
            }
            mTreeRoot.addChildren(archiveDatumObjectFolder);
        }

        mAndroidTreeView = new AndroidTreeView(this, mTreeRoot);
        mAndroidTreeView.setDefaultAnimation(true);
        mFileTypeOneTreeViewContainer.addView(mAndroidTreeView.getView());
        mAndroidTreeView.setSelectionModeEnabled(true);
    }

    // Ganesh Added

    public String getCachePath(String base64Pdf, String filename, String extension) {
        // Create a file in the Internal Storage

        byte[] pdfAsBytes = Base64.decode(base64Pdf, 0);

        File file = null;
        FileOutputStream outputStream;
        try {

            file = new File(getCacheDir(), filename + "." + extension);

            outputStream = new FileOutputStream(file);
            outputStream.write(pdfAsBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private void displayFromUrl(String base64Pdf) {

        firstPdfView.fromFile(new File(getCachePath(base64Pdf, "file1", "pdf")))
                .defaultPage(pageNumber)
                .onPageChange(this)
                .onDraw(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();

        secondPdfView.fromAsset(SAMPLE_FILE_1)
                .defaultPage(pageNumber)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
//        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
        secondPdfView.jumpTo(page);

    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

        secondPdfView.zoomWithAnimation(firstPdfView.getZoom());
        secondPdfView.moveTo(firstPdfView.getCurrentXOffset(), firstPdfView.getCurrentYOffset());

    }

    @Override
    public void loadComplete(int nbPages) {
        try {
            PdfDocument.Meta meta = firstPdfView.getDocumentMeta();
            Log.e(TAG, "title = " + meta.getTitle());
            Log.e(TAG, "author = " + meta.getAuthor());
            Log.e(TAG, "subject = " + meta.getSubject());
            Log.e(TAG, "keywords = " + meta.getKeywords());
            Log.e(TAG, "creator = " + meta.getCreator());
            Log.e(TAG, "producer = " + meta.getProducer());
            Log.e(TAG, "creationDate = " + meta.getCreationDate());
            Log.e(TAG, "modDate = " + meta.getModDate());

            PdfDocument.Meta meta1 = secondPdfView.getDocumentMeta();
            Log.e(TAG, "title = " + meta1.getTitle());
            Log.e(TAG, "author = " + meta1.getAuthor());
            Log.e(TAG, "subject = " + meta1.getSubject());
            Log.e(TAG, "keywords = " + meta1.getKeywords());
            Log.e(TAG, "creator = " + meta1.getCreator());
            Log.e(TAG, "producer = " + meta1.getProducer());
            Log.e(TAG, "creationDate = " + meta1.getCreationDate());
            Log.e(TAG, "modDate = " + meta1.getModDate());

            printBookmarksTree(firstPdfView.getTableOfContents(), "-");
            printBookmarksTree(secondPdfView.getTableOfContents(), "-");
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

    private void applySelectedArchived() {

        getPdfDataRequestModel.setPatientId(mSelectedFileTypeDataToCompare.get(0).getRespectiveParentPatientID());
        getPdfDataRequestModel.setFileType(mSelectedFileTypeDataToCompare.get(0).getFileType());
        getPdfDataRequestModel.setFileTypeRefId("9574");

        ArrayList<LstDocTypeRequest> lstDocTypeRequests = new ArrayList<>();

        if (mAndroidTreeView != null) {
            List<String> selectedValues = mAndroidTreeView.getSelectedValues(String.class);

            // TODO : THIS IS HACK, PLZ FIX IT
            for (String data :
                    selectedValues) {

                String[] seperateData = data.split("\\|");

                LstDocTypeRequest lstDocTypeRequest = new LstDocTypeRequest();
                lstDocTypeRequest.setPageCount(4402);
                lstDocTypeRequest.setCreatedDate("2017-03-10T17:22:49.273");

                DocTypeRequest docTypeRequest = new DocTypeRequest();
                docTypeRequest.setTypeId(Integer.parseInt(seperateData[1]));
                docTypeRequest.setTypeName(seperateData[0]);
                docTypeRequest.setAbbreviation("IADM48");

                lstDocTypeRequest.setDocTypeRequest(docTypeRequest);

                lstDocTypeRequests.add(lstDocTypeRequest);
            }
        }

        // call api
        mPatientsHelper.getPdfData(getPdfDataRequestModel);

    }

}

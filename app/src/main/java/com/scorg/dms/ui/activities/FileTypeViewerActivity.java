package com.scorg.dms.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.scorg.dms.R;
import com.scorg.dms.helpers.patients.PatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.requestmodel.filetreerequestmodel.FileTreeRequestModel;
import com.scorg.dms.model.requestmodel.filetreerequestmodel.LstSearchParam;
import com.scorg.dms.model.responsemodel.annotationlistresponsemodel.AnnotationList;
import com.scorg.dms.model.responsemodel.annotationlistresponsemodel.DocTypeList;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.ArchiveDatum;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.FileTreeResponseData;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.FileTreeResponseModel;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.LstDocCategory;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.LstDocType;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.scorg.dms.util.DmsConstants;
import com.scorg.dms.views.treeViewHolder.IconTreeItemHolder;
import com.scorg.dms.views.treeViewHolder.SelectableHeaderHolder;
import com.scorg.dms.views.treeViewHolder.SelectableItemHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 14/3/17.
 */

public class FileTypeViewerActivity extends AppCompatActivity implements View.OnClickListener, HelperResponse {
    private Context mContext;

    @BindView(R.id.openCompareFileTypeRightDrawerFAB)
    FloatingActionButton mOpenCompareFileTypeRightDrawerFAB;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    DrawerLayout mDrawer;
    NavigationView mRightNavigationView;
    View mHeaderView;
    private PatientsHelper mPatientsHelper;

    private RelativeLayout mFileTypeOneTreeViewContainer;
    private RelativeLayout mFileTypeTwoTreeViewContainer;
    private AndroidTreeView mAndroidTreeView;

    //---------
    ArrayList<PatientFileData> mSelectedFileTypeDataToCompare;
    String respectivePatientID;
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

        //------------


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
            //onclick on floating button
        }
    }


    @OnClick(R.id.openCompareFileTypeRightDrawerFAB)
    public void openCompareFileTypeDrawer(View v) {
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

    }

    @Override
    public void onSuccess(int mOldDataTag, CustomResponse customResponse) {
        FileTreeResponseModel fileTreeResponseModel = (FileTreeResponseModel) customResponse;
        FileTreeResponseData fileTreeResponseData = fileTreeResponseModel.getFileTreeResponseData();
        createAnnotationTreeStructure(fileTreeResponseData, false);
    }

    @Override
    public void onParseError(int mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(int mOldDataTag, String serverErrorMessage) {

    }


    //--- TODO : FIX THIS TREE STRUCTUR  , ALSO CHANGE model of getFileDATA PDF
    private void createAnnotationTreeStructure(FileTreeResponseData fileTreeResponseData, boolean isExpanded) {

        mFileTypeOneTreeViewContainer.removeAllViews();
        mFileTypeTwoTreeViewContainer.removeAllViews();

        TreeNode root = TreeNode.root();

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
            root.addChildren(archiveDatumObjectFolder);
        }

        mAndroidTreeView = new AndroidTreeView(this, root);
        mAndroidTreeView.setDefaultAnimation(true);
        mFileTypeOneTreeViewContainer.addView(mAndroidTreeView.getView());
        mAndroidTreeView.setSelectionModeEnabled(true);
    }
}

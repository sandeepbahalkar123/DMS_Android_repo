package com.scorg.dms.helpers.patients;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.scorg.dms.R;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.requestmodel.filetreerequestmodel.FileTreeRequestModel;
import com.scorg.dms.model.requestmodel.filetreerequestmodel.LstSearchParam;
import com.scorg.dms.model.requestmodel.getpdfdatarequestmodel.GetPdfDataRequestModel;
import com.scorg.dms.model.requestmodel.showsearchresultrequestmodel.ShowSearchResultRequestModel;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.ArchiveDatum;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.FileTreeResponseData;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.FileTreeResponseModel;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.LstDocCategory;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.LstDocType;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
import com.scorg.dms.network.ConnectRequest;
import com.scorg.dms.network.ConnectionFactory;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DmsConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class PatientsHelper implements ConnectionListener {

    String TAG = this.getClass().getSimpleName();
    Context mContext;
    HelperResponse mHelperResponseManager;

    public PatientsHelper(Context context, HelperResponse loginActivity1) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity1;
    }


    //-- TO get Patient list from server
    public void doGetPatientList(ShowSearchResultRequestModel showSearchResultRequestModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DmsConstants.TASK_PATIENT_LIST, Request.Method.POST);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(showSearchResultRequestModel);
        mConnectionFactory.setUrl(Config.URL_PATIENT_LIST);
        mConnectionFactory.createConnection(DmsConstants.TASK_PATIENT_LIST);
    }

    public void doGetAllAnnotations() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DmsConstants.TASK_ANNOTATIONS_LIST, Request.Method.GET);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.URL_ANNOTATIONS_LIST);
        mConnectionFactory.createConnection(DmsConstants.TASK_ANNOTATIONS_LIST);
    }

    public void doGetArchivedList(FileTreeRequestModel fileTreeRequestModel) {

        //---------------

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DmsConstants.TASK_GET_ARCHIVED_LIST, Request.Method.POST);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(fileTreeRequestModel);

        mConnectionFactory.setUrl(Config.URL_GET_ARCHIVED_LIST);
        mConnectionFactory.createConnection(DmsConstants.TASK_GET_ARCHIVED_LIST);
    }

    public void getPdfData(GetPdfDataRequestModel getPdfDataRequestModel, String taskID) {

        //---------------
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, taskID, Request.Method.POST);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(getPdfDataRequestModel);
        mConnectionFactory.setUrl(Config.URL_GET_PDF_DATA);
        mConnectionFactory.createConnection(taskID);
    }


    //-------------------------
    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:

                if (String.valueOf(mOldDataTag).equalsIgnoreCase("" + DmsConstants.TASK_GET_ARCHIVED_LIST)) {
                    //-- THIS IS DONE FOR MERGING OF TREE.
                    FileTreeResponseModel fileTreeResponseModel = (FileTreeResponseModel) customResponse;
                    FileTreeResponseData fileTreeResponseData = fileTreeResponseModel.getFileTreeResponseData();

                    String tempList[] = {"0", "1"};

                    for (int k = 0; k < fileTreeResponseData.getArchiveData().size(); k++) {
                        ArchiveDatum dataTemp = fileTreeResponseData.getArchiveData().get(k);
                        dataTemp.setMergedFileCompareCustomID(new String[]{tempList[k]});
                        List<LstDocCategory> lstDocCategories = dataTemp.getLstDocCategories();
                        for (int i = 0; i < lstDocCategories.size(); i++) {
                            LstDocCategory dataTempLstDocCategory = lstDocCategories.get(i);
                            dataTempLstDocCategory.setMergedFileCompareCustomID(new String[]{tempList[k]});
                            List<LstDocType> lstDocTypeList = dataTempLstDocCategory.getLstDocTypes();
                            for (int j = 0; j < lstDocTypeList.size(); j++) {
                                LstDocType lstDocType = lstDocTypeList.get(j);
                                lstDocType.setMergedFileCompareCustomID(new String[]{tempList[k]});
                            }
                        }
                    }
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);

                } else {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                }
                break;

            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, "parse error");
                mHelperResponseManager.onParseError(mOldDataTag, "parse error");
                break;

            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, "server error");
                mHelperResponseManager.onServerError(mOldDataTag, "server error");

                break;

            default:
                CommonMethods.Log(TAG, "default error");
                break;

        }

    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    public FileTreeResponseModel loadJSONFromAsset() {
        FileTreeResponseModel showSearchResultResponseModel;
        try {
            InputStream is = mContext.getAssets().open("temp.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            showSearchResultResponseModel = new Gson().fromJson(json, FileTreeResponseModel.class);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return showSearchResultResponseModel;
    }
}

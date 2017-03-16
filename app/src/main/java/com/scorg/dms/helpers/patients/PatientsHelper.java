package com.scorg.dms.helpers.patients;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.requestmodel.filetreerequestmodel.FileTreeRequestModel;
import com.scorg.dms.model.requestmodel.filetreerequestmodel.LstSearchParam;
import com.scorg.dms.model.requestmodel.showsearchresultrequestmodel.ShowSearchResultRequestModel;
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

public class PatientsHelper implements HelperResponse, ConnectionListener {

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


    //-------------------------
    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, int mOldDataTag) {

        CommonMethods.Log(TAG, customResponse.toString());

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == DmsConstants.TASK_PATIENT_LIST) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == DmsConstants.TASK_ANNOTATIONS_LIST) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == DmsConstants.TASK_GET_ARCHIVED_LIST) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                }
                break;

            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, "parse error");
                mHelperResponseManager.onParseError(mOldDataTag, "parse error");
                break;

            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, "server error");
                mHelperResponseManager.onParseError(mOldDataTag, "server error");

                break;

            default:
                CommonMethods.Log(TAG, "default error");
                break;

        }

    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    //-------------------------
    @Override
    public void onSuccess(int mOldDataTag, CustomResponse customResponse) {

    }

    @Override
    public void onParseError(int mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(int mOldDataTag, String serverErrorMessage) {

    }
    //-------------------------

    public ShowSearchResultResponseModel loadJSONFromAsset() {
        ShowSearchResultResponseModel showSearchResultResponseModel;
        try {
            InputStream is = mContext.getAssets().open("searchresult.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            showSearchResultResponseModel = new Gson().fromJson(json, ShowSearchResultResponseModel.class);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return showSearchResultResponseModel;
    }
}


package com.scorg.dms.network;

/**
 * @author Sandeep Bahalkar
 */

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.Connector;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.preference.DmsPreferencesManager;
import com.scorg.dms.singleton.Device;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DmsConstants;

import java.util.HashMap;
import java.util.Map;

public class ConnectionFactory extends ConnectRequest {

    private static final String TAG = "ConnectionFactory";
    Connector connector = null;
    private Device device;

    public ConnectionFactory(Context context, ConnectionListener connectionListener, View viewById, boolean isProgressBarShown, int mOldDataTag, int reqPostOrGet) {
        super();
        this.mConnectionListener = connectionListener;
        this.mContext = context;
        this.mViewById = viewById;
        this.isProgressBarShown = isProgressBarShown;
        this.mOldDataTag = mOldDataTag;
        this.reqPostOrGet = reqPostOrGet;

        device = Device.getInstance(mContext);
    }

    public void setHeaderParams(Map<String, String> headerParams) {
        this.mHeaderParams = headerParams;
    }

    public void setHeaderParams() {

        Map<String, String> headerParams = new HashMap<>();

        String authorizationString = "";
        String contentType = DmsPreferencesManager.getString(DmsConstants.LOGIN_SUCCESS, mContext);

        if (contentType.equalsIgnoreCase(DmsConstants.TRUE)) {
            authorizationString = DmsPreferencesManager.getString(DmsConstants.TOKEN_TYPE, mContext)
                    + " " + DmsPreferencesManager.getString(DmsConstants.ACCESS_TOKEN, mContext);
            headerParams.put(DmsConstants.CONTENT_TYPE, DmsConstants.APPLICATION_JSON);
        } else {
            headerParams.put(DmsConstants.CONTENT_TYPE, DmsConstants.APPLICATION_URL_ENCODED);
        }

        headerParams.put(DmsConstants.AUTHORIZATION, authorizationString);
        headerParams.put(DmsConstants.DEVICEID, device.getDeviceId());

        headerParams.put(DmsConstants.OS, device.getOS());
        headerParams.put(DmsConstants.OSVERSION, device.getOSVersion());
        //  headerParams.put(DmsConstants.DEVICETYPE, device.getDeviceType());
//        headerParams.put(DmsConstants.ACCESS_TOKEN, "");
        CommonMethods.Log(TAG, "setHeaderParams:" + headerParams.toString());
        this.mHeaderParams = headerParams;
    }

    public void setPostParams(CustomResponse customResponse) {
        this.customResponse = customResponse;
    }

    public void setPostParams(Map<String, String> postParams) {
        this.mPostParams = postParams;
    }


    public void setUrl(String url) {
        this.mURL = url;
    }

    public Connector createConnection(int type) {
        /*switch (type) {

            //write cases for different APIS
            case DmsConstants.REGISTRATION_CODE://This is sample code
                connector = new RequestManager(mContext, mConnectionListener, DmsConstants.REGISTRATION_CODE, mViewById, isProgressBarShown, mOldDataTag, Request.Method.POST);
                break;
            case DmsConstants.LOGIN_CODE://This is for login
                //  connector = new RequestManager(mContext, mConnectionListener, DmsConstants.LOGIN_CODE, mViewById, isProgressBarShown, mOldDataTag, Request.Method.POST);
                connector = new RequestManager(mContext, mConnectionListener, type, mViewById, isProgressBarShown, mOldDataTag, reqPostOrGet);
                break;
            case DmsConstants.PATIENT_LIST://This is for patient list
                connector = new RequestManager(mContext, mConnectionListener, type, mViewById, isProgressBarShown, mOldDataTag, reqPostOrGet);
                break;
            default:
                Log.d(TAG, "default_circle " + type);
                break;
        }*/

        connector = new RequestManager(mContext, mConnectionListener, type, mViewById, isProgressBarShown, mOldDataTag, reqPostOrGet);

        if (customResponse != null) connector.setPostParams(customResponse);

        if (mPostParams != null) connector.setPostParams(mPostParams);

        if (mHeaderParams != null) connector.setHeaderParams(mHeaderParams);

        if (mURL != null) connector.setUrl(mURL);

        connector.connect();

        return connector;
    }

    public void cancel() {
        connector.abort();
    }
}
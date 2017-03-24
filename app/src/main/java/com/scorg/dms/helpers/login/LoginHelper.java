package com.scorg.dms.helpers.login;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.responsemodel.loginresponsemodel.LoginResponseModel;
import com.scorg.dms.network.ConnectRequest;
import com.scorg.dms.network.ConnectionFactory;
import com.scorg.dms.preference.DmsPreferencesManager;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DmsConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class LoginHelper implements ConnectionListener {

    String TAG = this.getClass().getSimpleName();
    Context mContext;
    HelperResponse mHelperResponseManager;
    private String userName;
    private String mServerPath;
    private String password;
    private String userGender;

    public LoginHelper(Context context, HelperResponse loginActivity1) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity1;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, int mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == DmsConstants.TASK_LOGIN_CODE) {
                    LoginResponseModel model = (LoginResponseModel) customResponse;
                    DmsPreferencesManager.putString(DmsConstants.LOGIN_SUCCESS, DmsConstants.TRUE, mContext);
                    DmsPreferencesManager.putString(DmsConstants.ACCESS_TOKEN, model.getAccessToken(), mContext);
                    DmsPreferencesManager.putString(DmsConstants.TOKEN_TYPE, model.getTokenType(), mContext);
                    DmsPreferencesManager.putString(DmsConstants.REFRESH_TOKEN, model.getRefreshToken(), mContext);
                    DmsPreferencesManager.putString(DmsConstants.USERNAME, userName, mContext);
                    DmsPreferencesManager.putString(DmsConstants.PASSWORD, password, mContext);
                    DmsPreferencesManager.putString(DmsPreferencesManager.DMS_PREFERENCES_KEY.USER_GENDER, userGender, mContext);
                    mHelperResponseManager.onSuccess(mOldDataTag, model);
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

            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, "no connection error");
                mHelperResponseManager.onNoConnectionError(mOldDataTag, "no connection error");

                break;


            default:
                CommonMethods.Log(TAG, "default error");
                break;

        }

    }


    @Override
    public void onTimeout(ConnectRequest request) {

    }


    public void doAppLogin(String userName, String password) {
        this.userName = userName;
        this.password = password;
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DmsConstants.TASK_LOGIN_CODE, Request.Method.POST);
        mConnectionFactory.setHeaderParams();
        Map<String, String> testParams = new HashMap<String, String>();
        testParams.put(DmsConstants.GRANT_TYPE_KEY, DmsConstants.PASSWORD);
        testParams.put(DmsConstants.USERNAME, userName);
        testParams.put(DmsConstants.PASSWORD, password);
        testParams.put(DmsConstants.CLIENT_ID_KEY, DmsConstants.CLIENT_ID_VALUE);
        mConnectionFactory.setPostParams(testParams);
        Log.e(TAG,"Config.URL_LOGIN: "+Config.URL_LOGIN);
        mConnectionFactory.setUrl(Config.URL_LOGIN);
        mConnectionFactory.createConnection(DmsConstants.TASK_LOGIN_CODE);
    }
    public void checkConnectionToServer(String serverPath) {
        this.mServerPath = serverPath;
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DmsConstants.TASK_CHECK_SERVER_CONNECTION, Request.Method.POST);
        Log.e(TAG,"Config.URL_LOGIN: "+Config.URL_CHECK_SERVER_CONNECTION);
        mConnectionFactory.setUrl(Config.URL_CHECK_SERVER_CONNECTION);
        mConnectionFactory.createConnection(DmsConstants.TASK_CHECK_SERVER_CONNECTION);
    }
}

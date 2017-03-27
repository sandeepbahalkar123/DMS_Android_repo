package com.scorg.dms.ui.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.scorg.dms.R;
import com.scorg.dms.helpers.login.LoginHelper;
import com.scorg.dms.interfaces.CheckIpConnection;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.responsemodel.Common;
import com.scorg.dms.model.responsemodel.iptestresponsemodel.IpTestResponseModel;
import com.scorg.dms.preference.DmsPreferencesManager;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DmsConstants;


public class SplashScreenActivity extends AppCompatActivity implements HelperResponse {

    private static final String TAG = "SplashScreenActivity";

    private Context mContext;
    Boolean isEnteredServerPath;
    Dialog mDialog;
    private LoginHelper mLoginHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        mContext = this;
        mLoginHelper = new LoginHelper(this, this);

        //handler to close the splash activity after the set time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String userName = DmsPreferencesManager.getString(DmsConstants.USERNAME, mContext);
                String password = DmsPreferencesManager.getString(DmsConstants.PASSWORD, mContext);

                Intent intentObj = null;

                if (DmsConstants.BLANK.equalsIgnoreCase(userName) || DmsConstants.BLANK.equalsIgnoreCase(password)) {
                    if (!DmsPreferencesManager.getString(DmsPreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, mContext).equals(DmsConstants.TRUE)) {
                        //alert dialog for serverpath
                        CommonMethods.showAlertDialog(SplashScreenActivity.this, getString(R.string.server_path) + "\n" + getString(R.string.for_example_server_path), new CheckIpConnection() {
                            @Override
                            public void onOkButtonClickListner(String serverPath, Context context, Dialog dialog) {
                                mDialog = dialog;
                                mContext = context;
                                DmsPreferencesManager.putString(DmsPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, serverPath, context);
                                mLoginHelper.checkConnectionToServer(serverPath);


                            }
                        });
                    } else {
                        intentObj = new Intent(mContext, LoginActivity.class);
                        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentObj);

                        finish();
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                    }
                } else {
                    //------Check Remember ME first , then only move on next screen.
                    intentObj = new Intent(mContext, PatientList.class);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentObj);

                    finish();
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }


            }
        }, DmsConstants.TIME_STAMPS.THREE_SECONDS);

    }

    @Override

    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        mDialog.dismiss();
        IpTestResponseModel ipTestResponseModel = (IpTestResponseModel) customResponse;
        if (ipTestResponseModel.getCommon().getStatusCode().equals(DmsConstants.SUCCESS)) {
            DmsPreferencesManager.putString(DmsPreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, DmsConstants.TRUE, mContext);
            Intent intentObj = new Intent(mContext, LoginActivity.class);
            intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentObj);
            finish();

        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        DmsPreferencesManager.putString(DmsConstants.LOGIN_SUCCESS, DmsConstants.FALSE, mContext);
        CommonMethods.showAlertDialog(SplashScreenActivity.this, getString(R.string.wrong_server_path) + "\n" + getString(R.string.for_example_server_path), new CheckIpConnection() {
            @Override
            public void onOkButtonClickListner(String serverPath, Context context, Dialog dialog) {
                DmsPreferencesManager.putString(DmsPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, serverPath, context);
                mLoginHelper.checkConnectionToServer(serverPath);
            }
        });

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        DmsPreferencesManager.putString(DmsConstants.LOGIN_SUCCESS, DmsConstants.FALSE, mContext);
        CommonMethods.showAlertDialog(SplashScreenActivity.this, getString(R.string.wrong_server_path) + "\n" + getString(R.string.for_example_server_path), new CheckIpConnection() {
            @Override
            public void onOkButtonClickListner(String serverPath, Context context, Dialog dialog) {

                DmsPreferencesManager.putString(DmsPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, serverPath, context);
                mLoginHelper.checkConnectionToServer(serverPath);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
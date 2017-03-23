package com.scorg.dms.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.scorg.dms.R;
import com.scorg.dms.preference.DmsPreferencesManager;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DmsConstants;


public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        mContext = this;

        //handler to close the splash activity after the set time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String userName = DmsPreferencesManager.getString(DmsConstants.USERNAME, mContext);
                String password = DmsPreferencesManager.getString(DmsConstants.PASSWORD, mContext);

                Intent intentObj = null;

                if (DmsConstants.BLANK.equalsIgnoreCase(userName) || DmsConstants.BLANK.equalsIgnoreCase(password)) {
                  //alert dialog for serverpath
                    CommonMethods.showAlertDialog(SplashScreenActivity.this,getString(R.string.server_path)+"\n"+getString(R.string.for_example_server_path),false);
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


}

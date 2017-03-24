package com.scorg.dms.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.scorg.dms.R;
import com.scorg.dms.helpers.login.LoginHelper;
import com.scorg.dms.interfaces.CheckIpConnection;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.preference.DmsPreferencesManager;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DmsConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements HelperResponse {

    String TAG = this.getClass().getSimpleName();
    Context mContext;
    String mServerPath;
    @BindView(R.id.userName)
    EditText mUserName;

    @BindView(R.id.password)
    EditText mPassword;

    private LoginHelper mLoginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = getApplicationContext();
        // Log.d("Server PAth",mServerPath);

        ButterKnife.bind(this);
        mLoginHelper = new LoginHelper(this, this);

    }


    @OnClick(R.id.loginButton)
    public void doLogin() {
        if (!validate()) {
            mLoginHelper.doAppLogin(mUserName.getText().toString(), mPassword.getText().toString());
        }
    }

    /**
     * Return true if fields empty/validation failed, else false.
     *
     * @return
     */
    private boolean validate() {
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        String message = null;
        if (userName.isEmpty() || password.isEmpty()) {
            message = getString(R.string.error_empty_fields);
        }

        if (message != null) {
            CommonMethods.showSnack(mUserName, message);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        Intent intent = new Intent(this, PatientList.class);
        startActivity(intent);

    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(this, errorMessage);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(this, serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(this, serverErrorMessage);

    }
}


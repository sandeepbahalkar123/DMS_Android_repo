package com.scorg.dms.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scorg.dms.R;
import com.scorg.dms.helpers.login.LoginHelper;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.responsemodel.Common;
import com.scorg.dms.model.responsemodel.LoginResponseModel;
import com.scorg.dms.network.ConnectRequest;
import com.scorg.dms.network.ConnectionFactory;
import com.scorg.dms.preference.DmsPreferencesManager;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DmsConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements HelperResponse {

    String TAG = this.getClass().getSimpleName();

    @BindView(R.id.userName)
    EditText mUserName;

    @BindView(R.id.password)
    EditText mPassword;

    private LoginHelper mLoginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
    public void onSuccess(int mOldDataTag, CustomResponse customResponse) {

        Intent intent = new Intent(this, PatientList.class);
        startActivity(intent);

    }

    @Override
    public void onParseError(int mOldDataTag, String errorMessage) {
        CommonMethods.showToast(this, errorMessage);
    }

    @Override
    public void onServerError(int mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(this, serverErrorMessage);
    }
}


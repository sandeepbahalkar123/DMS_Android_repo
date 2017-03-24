package com.scorg.dms.network;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.scorg.dms.views.CustomProgressDialog;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.CustomResponse;

import java.util.Map;

public class ConnectRequest {
    protected Context mContext;
    protected CustomResponse customResponse;
    protected Map<String, String> mHeaderParams;
    protected Map<String, String> mPostParams;
    protected ConnectionListener mConnectionListener;
    protected CustomProgressDialog mProgressDialog;
    protected View mViewById;
    protected boolean isProgressBarShown;
    protected int mOldDataTag;
    protected String mURL;
    protected int reqPostOrGet;
}

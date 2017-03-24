package com.scorg.dms.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.scorg.dms.R;

public class CustomProgressDialog extends Dialog {

    ProgressBar mProgressBar;

    public CustomProgressDialog(Context context) {
        super(context, R.style.TransparentProgressDialog);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);

        setContentView(R.layout.mydialog);
        mProgressBar = (ProgressBar) this.findViewById(R.id.customProgressBar);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
    }

}

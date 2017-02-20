package com.scorg.dms.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ProgressBar;

import com.scorg.dms.R;

public class CustomProgressDialog extends Dialog {

    ProgressBar mProgressBar;

    public CustomProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.mydialog);
        mProgressBar = (ProgressBar) this.findViewById(R.id.customProgressBar);
        setCancelable(true);

    }
}

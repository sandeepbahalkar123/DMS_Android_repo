package com.scorg.dms.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import com.scorg.dms.R;
import com.scorg.dms.util.DmsConstants;
import com.scorg.dms.singleton.DmsApplication;

/**
 * Created by Sandeep Bahalkar
 */
public class CustomEditText extends EditText {
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        setTextColor(Color.parseColor(DmsConstants.TEXT_COLOR));
        setHintTextColor(context.getResources().getColor(R.color.placeHolder));
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextView);
        String customFont = a.getString(R.styleable.TextView_fontName);

        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public void setCustomFont(Context ctx, String asset) {
        Typeface typeface = DmsApplication.get(ctx, asset);
        setTypeface(typeface);
    }
}
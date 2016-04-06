package com.jannick.oslinux.utils;

import android.graphics.Bitmap;

import com.commit451.nativestackblur.NativeStackBlur;
import com.squareup.picasso.Transformation;

/**
 * Created by Jannick on 6-4-2016.
 */
public class BlurTransformation implements Transformation {

    private int mBlurRadius;

    public BlurTransformation(int blurRadius) {
        mBlurRadius = blurRadius;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap bm = NativeStackBlur.process(source, mBlurRadius);
        source.recycle();
        return bm;
    }

    @Override
    public String key() {
        return getClass().getCanonicalName() + "-" + mBlurRadius;
    }
}

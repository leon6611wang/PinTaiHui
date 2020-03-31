package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.xutils.image.AsyncDrawable;

public class WrapImageView extends AppCompatImageView {
    private int defaultWidth, defaultHeight;
    private float scale;

    public WrapImageView(Context context) {
        super(context);
    }

    public WrapImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }


        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        this.measure(0, 0);

        if (drawable.getClass() == NinePatchDrawable.class) {
            return;
        } else if (drawable instanceof AsyncDrawable) {
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();

        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        if (bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {

            return;
        }


        if (defaultWidth == 0) {
            defaultWidth = getWidth();

        }

        if (defaultHeight == 0) {
            defaultHeight = getHeight();

        }

        float scaleHeght = (float) defaultHeight / (float) bitmap.getHeight();
        float scaleWight = (float) defaultWidth / (float) bitmap.getWidth();
        if (scaleHeght >= scaleWight) {
            scale = scaleWight;
        } else {
            scale = scaleHeght;
        }
//        scale = (float) defaultHeight / (float) bitmap.getHeight();

        defaultWidth = Math.round(bitmap.getWidth() * scale);
        defaultHeight = Math.round(bitmap.getHeight() * scale);

        ViewGroup.LayoutParams params = this.getLayoutParams();

        params.width = defaultWidth;

        params.height = defaultHeight;

        this.setLayoutParams(params);


        super.onDraw(canvas);


    }
}

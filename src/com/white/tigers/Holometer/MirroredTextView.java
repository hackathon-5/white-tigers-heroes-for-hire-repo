package com.white.tigers.Holometer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by jamesflesher on 8/29/15.
 */
public class MirroredTextView extends TextView
{
    public MirroredTextView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.translate(getWidth(), 0);
        canvas.scale(-1, 1);
        super.onDraw(canvas);
    }
}
package com.view.kang.frame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.view.kang.sensor.R;

/**
 * Created by kang on 2015/10/20.
 * 图片跟随手指移动的View
 */
public class FollowTouchView extends View{
    //定义相关变量,依次是图片显示位置的X,Y坐标
    public float bitmapX;
    public float bitmapY;


    public FollowTouchView(Context context) {
        this(context, null);
    }

    public FollowTouchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FollowTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置图片的起始坐标
        bitmapX = 0;
        bitmapY = 200;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //创建,并且实例化Paint的对象
        Paint paint = new Paint();
        //根据图片生成位图对象
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        //绘制图片
        canvas.drawBitmap(bitmap,bitmapX,bitmapY,paint);
        //判断图片是否回收,木有回收的话强制收回图片
        if(bitmap.isRecycled())
        {
            bitmap.recycle();
        }
    }
}

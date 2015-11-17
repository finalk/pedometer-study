package com.view.kang.frame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

import com.view.kang.sensor.R;

/**
 * Created by kang on 2015/10/20.
 */
public class FrameActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        final FollowTouchView followTouchView = new FollowTouchView(this);
        followTouchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //设置妹子显示的位置
                followTouchView.bitmapX = event.getX();
                followTouchView.bitmapY = event.getY();
                //调用重绘方法
                followTouchView.invalidate();
//                if(event.getX()>followTouchView.getMeasuredWidth()-200 &&  event.getY()>followTouchView.getMeasuredHeight()-200){
                    Log.i("开始动", "开始动");
                    //第一个参数fromDegrees为动画起始时的旋转角度
                    //第二个参数toDegrees为动画旋转到的角度
                    //第三个参数pivotXType为动画在X轴相对于物件位置类型
                    //第四个参数pivotXValue为动画相对于物件的X坐标的开始位置
                    //第五个参数pivotXType为动画在Y轴相对于物件位置类型
                    //第六个参数pivotYValue为动画相对于物件的Y坐标的开始位置
                    Animation anim = new RotateAnimation(
                            0f
                            ,360f
                            ,RotateAnimation.RELATIVE_TO_SELF
                            , 0.5f
                            ,RotateAnimation.RELATIVE_TO_SELF
                            , 0.6f
                    );
                    anim.setRepeatCount(-1);
                    anim.setDuration(500);
                    followTouchView.startAnimation(anim);
//                }
                return true;
            }
        });
        frameLayout.addView(followTouchView);
    }
}

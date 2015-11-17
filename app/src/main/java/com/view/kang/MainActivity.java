package com.view.kang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.view.kang.frame.FrameActivity;
import com.view.kang.sensor.R;
import com.view.kang.sensor.SensorActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends Activity {
    public static final String TAG = "logTest";
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mIntent = new Intent();
    }

    @OnClick(R.id.sensor_demo)
    void Go2Sensor(){
        mIntent.setClass(this,SensorActivity.class);
        startActivity(mIntent);
    }
    @OnClick(R.id.frame_demo)
    void Go2Frame(){
        mIntent.setClass(this,FrameActivity.class);
        startActivity(mIntent);
    }
}

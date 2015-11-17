package com.view.kang.sensor;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.view.kang.sensor.service.StepService;
import com.view.kang.sensor.util.SensorUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by kang on 2015/10/20.
 */
public class SensorActivity extends Activity {
    public static final String TAG = "logTest";

    @InjectView(R.id.show_info)
    TextView mShowInfo;
    @InjectView(R.id.show_step)
    TextView mShowStep;
    private SensorManager mSensorManager;
    private ServiceConnection mStepConnection;
    private Intent mIntent;

    private static final int STEP_CHANGE = 0;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STEP_CHANGE:
                    mShowStep.setText("打我打我打我啊");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        ButterKnife.inject(this);
        Message msg = new Message();
        msg.what = STEP_CHANGE;
        mHandler.sendMessage(msg);
        mIntent = new Intent(this, StepService.class);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                Log.i(TAG, "onServiceConnected");
                StepService.StepBinder stepBinder = (StepService.StepBinder) iBinder;
                StepService stepService = stepBinder.getService();
                stepService.registerCallback(new StepService.callBack() {
                    @Override
                    public void stepsChanged(int value) {
                        mShowStep.setText(""+ value);
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindStepService();
    }

    /**
     * 获取本机所有传感器的信息
     */
    @OnClick(R.id.get_sensor_info)
    public void getSensorInfo() {
        mShowInfo.setText(SensorUtil.getSensor(this));

    }

    /**
     * 接收加速度传感器的信息
     */
    @OnClick(R.id.receive_accelerometer)
    public void receive_accelerometer() {
        SensorEventListener sensorEventListener = new SensorEventListener() {
            /**
             * 当传感器的值发生改变时回调该方法
             */
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] values = event.values;
                StringBuilder sb = new StringBuilder();
                sb.append("X方向上的加速度:\n");
                sb.append(values[SensorManager.DATA_X]);
                sb.append("Y方向上的加速度:\n");
                sb.append(values[SensorManager.DATA_Y]);
                sb.append("Z方向上的加速度:\n");
                sb.append(values[SensorManager.DATA_Z]);
                mShowInfo.setText(sb.toString());
            }

            /**
             *  当传感器精度改变时回调该方法。
             */
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        mSensorManager
                .registerListener(sensorEventListener
                        , mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_GAME);

    }


    private void bindStepService() {
        Log.i(TAG, "bindStepService");
        startService(mIntent);
        bindService(mIntent, mStepConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    private void unBindStepService() {
        Log.i(TAG, "unBindStepService");
        unbindService(mStepConnection);
        stopService(mIntent);
    }

    @Override
    protected void onDestroy() {
        unBindStepService();
        super.onDestroy();
    }


}

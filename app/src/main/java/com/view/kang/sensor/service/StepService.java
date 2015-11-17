package com.view.kang.sensor.service;

/*
*  Pedometer - Android App
*  Copyright (C) 2009 Levente Bagi
*
*  This program is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.view.kang.MainActivity;
import com.view.kang.sensor.util.StepDetector;



/**
 *
 */
public class StepService extends Service {
    private static final String TAG = "StepService";
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private StepDetector mStepDetector;
    // private StepBuzzer mStepBuzzer; // used for debugging

    private int mSteps;
    private StepChangeListener mStepChangeListener;

    @Override
    public void onCreate() {
        Log.i(MainActivity.TAG, "[SERVICE] onCreate");
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mStepDetector = new StepDetector();
        //和步数检测器的绑定
        mStepDetector.onStepChangeListener(new StepChangeListener() {
            @Override
            public void stepChanged(int value) {

                if(mCallBack != null){
//                    通知activity步数发生改变
                    mCallBack.stepsChanged(value);
                }
            }
        });
        registerDetector();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i(MainActivity.TAG, "[SERVICE] onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(MainActivity.TAG, "[SERVICE] onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(MainActivity.TAG, "[SERVICE] onDestroy");
        // Unregister our receiver.
        unregisterDetector();
        super.onDestroy();
        // Stop detecting
        mSensorManager.unregisterListener(mStepDetector);
    }

    private void registerDetector() {
        mSensor = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER /*|
            Sensor.TYPE_MAGNETIC_FIELD |
            Sensor.TYPE_ORIENTATION*/);
        mSensorManager.registerListener(
                mStepDetector,
                mSensor,
                SensorManager.SENSOR_DELAY_FASTEST);

    }

    private void unregisterDetector() {
        mSensorManager.unregisterListener(mStepDetector);
    }

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class StepBinder extends Binder {
        public StepService getService() {
            return StepService.this;
        }
    }

    /**
     * Receives messages from activity.
     */
    private final IBinder mBinder = new StepBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(MainActivity.TAG, "[SERVICE] onBind");
        return mBinder;
    }

    /**
     * 通知activity步数发生改变
     */
    public interface callBack{
        public void stepsChanged(int value);
    }

    private callBack mCallBack;

    /**
     * 用于让activity注册回调
     */
    public void registerCallback(callBack callBack){
        this.mCallBack = callBack;
    }


}



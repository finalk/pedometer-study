package com.view.kang.sensor.util;

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

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.view.kang.MainActivity;
import com.view.kang.sensor.service.StepChangeListener;

/**
 * Detects steps and notifies all listeners (that implement StepListener).
 * @author Levente Bagi
 * @todo REFACTOR: SensorListener is deprecated
 * 步数检测
 */
public class StepDetector implements SensorEventListener {
    private final static String TAG = "StepDetector";
    private float mLimit = 10;
    private float mLastValues[] = new float[3 * 2];
    private float mScale[] = new float[2];
    private float mYOffset;

    private float mLastDirections[] = new float[3 * 2];
    private float mLastExtremes[][] = {new float[3 * 2], new float[3 * 2]};
    private float mLastDiff[] = new float[3 * 2];
    private int mLastMatch = -1;

    private int countStep = 0;

    private StepChangeListener mStepListeners;

    public void onStepChangeListener(StepChangeListener listener){
        this.mStepListeners = listener;
    }

    public StepDetector() {
        int h = 480; // TODO: remove this constant
        mYOffset = h * 0.5f;
        mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }
    /**
     * @param sensitivity 敏感度
     */
    public void setSensitivity(float sensitivity) {
        mLimit = sensitivity; // 1.97  2.96  4.44  6.66  10.00  15.00  22.50  33.75  50.62
    }

//    public void addStepListener(StepListener sl) {
//        mStepListeners.add(sl);
//    }

    //此处处理获得的加速度值
//    public void onSensorChanged(int sensor, float[] values) {
    public void onSensorChanged(SensorEvent event) {
        Log.i(MainActivity.TAG,"onSensorChanged");
        Sensor sensor = event.sensor;
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            } else {
                int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                if (j == 1) {
                    float vSum = 0;
                    for (int i = 0; i < 3; i++) {
                        final float v = mYOffset + event.values[i] * mScale[j];
                        vSum += v;
                    }
                    int k = 0;
                    float v = vSum / 3;
                    Log.i(MainActivity.TAG,"速度 = "+v);
                    float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                    Log.i(MainActivity.TAG," mLastValues[k] = "+ mLastValues[k]);
                    Log.i(MainActivity.TAG," -mLastDirections[k] = "+ -mLastDirections[k]);
                    if (direction == -mLastDirections[k]) {
                        // 方向改变
                        int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
                        mLastExtremes[extType][k] = mLastValues[k];
                        float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);
                        Log.i(MainActivity.TAG,"else if diff = "+diff);
                        Log.i(MainActivity.TAG,"else if mLastDiff[k] = "+mLastDiff[k]);
                        Log.i(MainActivity.TAG,"else if mLastMatch = "+mLastMatch);
                        Log.i(MainActivity.TAG,"else if extType = "+extType);
                        if (diff > 3 &&diff<20) {

                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
                            boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
                            boolean isNotContra = (mLastMatch != 1 - extType);

                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                //此处满足条件，步数加1
                                countStep++;
                                Log.i(MainActivity.TAG,"总步数 = "+countStep);
                                if(mStepListeners!=null){
                                    mStepListeners.stepChanged(countStep);
                                }
                                mLastMatch = extType;
                            } else {
                                mLastMatch = -1;
                            }
                        }
                        mLastDiff[k] = diff;
                    }
                    mLastDirections[k] = direction;
                    mLastValues[k] = v;
                }
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

}
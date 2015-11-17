package com.view.kang.sensor.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.List;

/**
 * Created by kang on 2015/10/14.
 */
public class SensorUtil {

    public static String getSensor(Context context) {
        //从系统服务中获得传感器管理器
        SensorManager sm=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        //从传感器管理器中获得全部的传感器列表
        List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
        //有多少个传感器
        StringBuffer allSensorInfo = new StringBuffer()
                .append("经检测该手机有")
                .append(allSensors.size())
                .append("个传感器，他们分别是：\n");

        //显示每个传感器的具体信息
        for (Sensor s : allSensors) {

            String tempString = "\n" + "  设备名称：" + s.getName() + "\n" + "  设备版本：" + s.getVersion() + "\n" + "  供应商："
                    + s.getVendor() + "\n";
            switch (s.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    allSensorInfo.append(s.getType())
                                 .append(" 加速度传感器accelerometer")
                                 .append(tempString);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    allSensorInfo.append(s.getType())
                            .append(" 陀螺仪传感器gyroscope")
                            .append(tempString);
                    break;
                case Sensor.TYPE_LIGHT:
                    allSensorInfo.append(s.getType())
                            .append(" 环境光线传感器light")
                            .append(tempString);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    allSensorInfo.append(s.getType())
                            .append(" 电磁场传感器magnetic field")
                            .append(tempString);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    allSensorInfo.append(s.getType())
                            .append(" 方向传感器orientation")
                            .append(tempString);
                    break;
                case Sensor.TYPE_PRESSURE:
                    allSensorInfo.append(s.getType())
                            .append(" 压力传感器pressure")
                            .append(tempString);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    allSensorInfo.append(s.getType())
                            .append(" 距离传感器proximity")
                            .append(tempString);
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    allSensorInfo.append(s.getType())
                            .append(" 温度传感器temperature")
                            .append(tempString);
                    break;
                default:
                    allSensorInfo.append(s.getType())
                            .append(" 未知传感器")
                            .append(tempString);
                    break;
            }
        }
        return allSensorInfo.toString();

    }
}



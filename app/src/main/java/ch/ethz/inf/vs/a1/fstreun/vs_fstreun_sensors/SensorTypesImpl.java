package ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors;

import android.hardware.Sensor;

/**
 * Created by fabio on 10/3/17.
 *
 */

@SuppressWarnings("deprecation")
class SensorTypesImpl implements SensorTypes {

    @Override
    public int getNumberValues(int sensorType) {

        switch (sensorType){
            case Sensor.TYPE_ACCELEROMETER:
                return 3;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return 1;
            case Sensor.TYPE_GRAVITY:
                return 3;
            case Sensor.TYPE_GYROSCOPE:
                return 3;
            case Sensor.TYPE_LIGHT:
                return 1;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                return 3;
            case Sensor.TYPE_MAGNETIC_FIELD:
                return 3;
            case Sensor.TYPE_ORIENTATION:
                return 3;
            case Sensor.TYPE_PRESSURE:
                return 1;
            case Sensor.TYPE_PROXIMITY:
                return 1;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return 1;
            case Sensor.TYPE_ROTATION_VECTOR:
                return 3;
            case Sensor.TYPE_TEMPERATURE:
                return 1;

        }
        return -1;
    }

    @Override
    public String getUnitString(int sensorType) {

        switch (sensorType){
            case Sensor.TYPE_ACCELEROMETER:
                return "m/s^2";
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return "°C";
            case Sensor.TYPE_GRAVITY:
                return "m/s^2";
            case Sensor.TYPE_GYROSCOPE:
                return "rad/s";
            case Sensor.TYPE_LIGHT:
                return "lx";
            case Sensor.TYPE_LINEAR_ACCELERATION:
                return "m/s^2";
            case Sensor.TYPE_MAGNETIC_FIELD:
                return "microT";
            case Sensor.TYPE_ORIENTATION:
                return "degree";
            case Sensor.TYPE_PRESSURE:
                return "hPa"; // mBar is the same
            case Sensor.TYPE_PROXIMITY:
                return "cm";
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return "%";
            case Sensor.TYPE_ROTATION_VECTOR:
                return "no unit";
            case Sensor.TYPE_TEMPERATURE:
                return "°C";

        }

        return null;
    }
}

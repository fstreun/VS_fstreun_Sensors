package ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors;


import android.hardware.Sensor;

import org.junit.Before;
import org.junit.Test;

import ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors.SensorTypes;
//import ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors.SensorTypesImpl;

import static org.junit.Assert.assertEquals;

/**
 * Created by fabio on 10/2/17.
 * copied from the Code_skeleton file
 * https://www.vs.inf.ethz.ch/edu/VS/
 *
 * To work on unit tests, switch the SensorValuesDetector Artifact in the Build Variants view.
 */

public class UnitStringsTest {

    @SuppressWarnings("WeakerAccess")
    SensorTypes mSensorTypes;

    @Before
    public void setupBeforeClass(){
        //mSensorTypes = new SensorTypesImpl();
    }

    @Test
    public void accelerometerTest() throws Exception {
        assertEquals(mSensorTypes.getUnitString(Sensor.TYPE_ACCELEROMETER), "m/s^2");
    }

    @Test
    public void ambientTemperatureTest() throws Exception {
        assertEquals(mSensorTypes.getUnitString(Sensor.TYPE_AMBIENT_TEMPERATURE), "°C");
    }

    @Test
    public void gravityTest() throws Exception {
        assertEquals(mSensorTypes.getUnitString(Sensor.TYPE_GRAVITY), "m/s^2");
    }

    @Test
    public void gyroscopeTest() throws Exception {
        assertEquals(mSensorTypes.getUnitString(Sensor.TYPE_GYROSCOPE), "rad/s");
    }

    @Test
    public void lightTest() throws Exception {
        assertEquals(mSensorTypes.getUnitString(Sensor.TYPE_LIGHT), "lx");
    }

    @Test
    public void linearAccelerationTest() throws Exception {
        assertEquals(mSensorTypes.getUnitString(Sensor.TYPE_LINEAR_ACCELERATION), "m/s^2");
    }

    @Test
    public void magneticFieldTest() throws Exception {
        assertEquals(mSensorTypes.getUnitString(Sensor.TYPE_MAGNETIC_FIELD), "microT");
    }

    @Test
    public void PressureTest() throws Exception {
        assertEquals(mSensorTypes.getUnitString(Sensor.TYPE_PRESSURE), "hPa");
    }

    @Test
    public void RelativeHumidityTest() throws Exception {
        assertEquals(mSensorTypes.getUnitString(Sensor.TYPE_RELATIVE_HUMIDITY), "%");
    }

    @Test
    public void RotationVectorTest() throws Exception {
        assertEquals(mSensorTypes.getUnitString(Sensor.TYPE_ROTATION_VECTOR), "no unit");
    }

}

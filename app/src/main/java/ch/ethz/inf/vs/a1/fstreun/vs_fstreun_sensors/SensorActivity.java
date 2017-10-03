package ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    public static final String EXTRA_SENSOR_NUMBER = "sensor_id";
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Intent intent = getIntent();
        int sensor_number = intent.getIntExtra(EXTRA_SENSOR_NUMBER, -1);

        if (sensor_number < 0){
            // show error message and close activity
            Toast.makeText(this, "failed to load sensor", Toast.LENGTH_SHORT).show();
            finish();
        } else {

            SensorManager sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
            List<Sensor> sensors = sensorMgr.getSensorList(Sensor.TYPE_ALL);

            sensor = sensors.get(sensor_number);

            setTitle(sensor.getName());
        }

    }
}

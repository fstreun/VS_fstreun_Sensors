package ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SensorActivity extends AppCompatActivity implements SensorEventListener{

    public static final String EXTRA_SENSOR_NUMBER = "sensor_id";

    private SensorManager sensorMgr;
    private Sensor sensor;

    private String unit;

    private List<TextView> textViews;

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
        }

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sensorMgr.getSensorList(Sensor.TYPE_ALL);
        sensor = sensors.get(sensor_number);
        setTitle(sensor.getName());

        SensorTypes sensorTypes = new SensorTypesImpl();

        int numberOfValues = sensorTypes.getNumberValues(sensor.getType());
        unit = sensorTypes.getUnitString(sensor.getType());

        textViews = new ArrayList<>(numberOfValues);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout_sensor);

        for (int i = 0; i < numberOfValues; i++){
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setPadding(20, 20, 20, 20); // in pixels (left, top, right, bottom)
            linearLayout.addView(textView);
            textViews.add(textView);
        }


    }

    /**
     * Used by the android test
     * @return TODO
     */
    public GraphContainer getGraphContainer() {
        return null;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;

        if (values.length != textViews.size()){
            Toast.makeText(this, "failed to load new sensor data", Toast.LENGTH_SHORT).show();
        }else {
            for (int i = 0; i < values.length; i++) {
                textViews.get(i).setText(values[i] + " " + unit);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register SensorChangeListener on every start of the activity
        sensorMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMgr.unregisterListener(this);
    }

}

package ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    private static SensorListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView) findViewById(R.id.listview_sensors);

        // TODO: use a async task or loader to load the list
        SensorManager sensorMgr;
        List<Sensor> sensors;

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensors = sensorMgr.getSensorList(Sensor.TYPE_ALL);

        adapter = new SensorListAdapter(getApplicationContext(), sensors);
        listView.setAdapter(adapter);

    }
}

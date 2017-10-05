package ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class SensorActivity extends AppCompatActivity implements SensorEventListener{

    public static final String EXTRA_SENSOR_TYP = "sensor_typ";

    private SensorManager sensorMgr;
    private Sensor sensor;
    private int numberOfValues;

    private String unit;

    private List<TextView> textViews;

    GraphContainerImpl graphContainer = new GraphContainerImpl();
    GraphView graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Intent intent = getIntent();
        int sensorTyp = intent.getIntExtra(EXTRA_SENSOR_TYP, -1); //-1 is type all.

        if (sensorTyp < 0){
            // show error message and close activity
            Toast.makeText(this, "failed to load sensor", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorMgr.getDefaultSensor(sensorTyp);
        setTitle(sensor.getName());

        SensorTypes sensorTypes = new SensorTypesImpl();

        numberOfValues = sensorTypes.getNumberValues(sensor.getType());
        unit = sensorTypes.getUnitString(sensor.getType());

        Log.d("SensorActivity", numberOfValues+"");

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

        graphView = (GraphView) findViewById(R.id.graph);
    }

    /**
     * TODO
     * Used by the android test
     * @return graph container of this activity
     */
    public GraphContainer getGraphContainer() {
        return graphContainer;
    }

    private void updateGraphView(){

        graphView.removeAllSeries();
        float[][] values = graphContainer.getValues();
        double [] xIndex = graphContainer.getxIndexs();


        DataPoint[][] dataPoints = new DataPoint[numberOfValues][values.length];

        for (int i = 0; i < values.length; i++){
            // i is the xIndex
            for (int j = 0; j < numberOfValues; j++){
                // j goes over the different values at one xIndex
                double index = xIndex[i];
                float val = values[i][j];
                dataPoints[j][i] = new DataPoint(index, val);
                //dataPoints[j][i] = new DataPoint(xIndex[i],values[i][j]);
            }
        }

        for (int i = 0; i < numberOfValues; i++){
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints[i]);
            graphView.addSeries(series);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;

        float[] realValues = new float[numberOfValues];

        for (int i = 0; i < numberOfValues; i++) {
            textViews.get(i).setText(values[i] + " " + unit);

            realValues[i] = values[i];
        }

        double time = System.currentTimeMillis()/1000.0;

        graphContainer.addValues(time, values);
        updateGraphView();
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

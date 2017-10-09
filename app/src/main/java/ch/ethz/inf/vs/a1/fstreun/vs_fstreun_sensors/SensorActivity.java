package ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private static final String SAVED_STATE_START_TIME = "start_time";
    private static final String SAVED_STATE_GRAPH_CONTAINER = "graph_container";

    private SensorManager sensorMgr;
    private Sensor sensor;
    private int numberOfValues;

    private String unit;

    private List<TextView> textViews;

    private GraphContainerImpl graphContainer = new GraphContainerImpl();
    private GraphView graphView;
    private int[] colors = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA};

    double timeAtStart;

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


        if (sensor == null){
            // show error message and close activity
            Toast.makeText(this, "failed to load sensor", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setTitle(sensor.getName());

        SensorTypes sensorTypes = new SensorTypesImpl();

        numberOfValues = sensorTypes.getNumberValues(sensor.getType());
        unit = sensorTypes.getUnitString(sensor.getType());

        if (numberOfValues == -1 || unit == null){
            // show error message and close activity
            Toast.makeText(this, "failed to load sensor", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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

        timeAtStart = System.currentTimeMillis()/1000.0;

        graphView = (GraphView) findViewById(R.id.graph);

    }

    /**
     * Used by the android test
     * @return returns the current graphContainer.
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
                dataPoints[j][i] = new DataPoint(xIndex[i],values[i][j]);
            }
        }

        for (int i = 0; i < numberOfValues; i++){
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints[i]);
            series.setColor(colors[i%colors.length]);
            graphView.addSeries(series);
        }
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(xIndex[0]);
        graphView.getViewport().setMaxX(xIndex[xIndex.length-1]);

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float[] values = sensorEvent.values;

        float[] realValues = new float[numberOfValues];

        for (int i = 0; i < numberOfValues; i++) {
            textViews.get(i).setText(values[i] + " " + unit);

            realValues[i] = values[i];
        }

        double time = (System.currentTimeMillis()/1000.0) - timeAtStart;

        graphContainer.addValues(time, realValues);
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timeAtStart = savedInstanceState.getDouble(SAVED_STATE_START_TIME);
        graphContainer = (GraphContainerImpl) savedInstanceState.getSerializable(SAVED_STATE_GRAPH_CONTAINER);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putDouble(SAVED_STATE_START_TIME, timeAtStart);
        outState.putSerializable(SAVED_STATE_GRAPH_CONTAINER, graphContainer);
        super.onSaveInstanceState(outState);
    }
}

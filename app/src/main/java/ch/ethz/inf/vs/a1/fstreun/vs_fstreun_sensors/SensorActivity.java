package ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
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

    private static final String SAVED_STATE_START_TIME = "start_time";
    private static final String SAVED_STATE_GRAPH_CONTAINER = "graph_container";

    private SensorManager sensorMgr;
    private Sensor sensor;
    private int numberOfValues;

    private String unit;

    private List<TextView> textViews;

    private GraphContainerThreadSave graphContainer;
    private GraphView graphView;
    private int[] colors = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA};

    double timeAtStart;

    long delayGraphViewUpdate = 200;
    long delayMaxDataUpdate = 50;

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

        graphContainer = new GraphContainerThreadSave();
        graphView = (GraphView) findViewById(R.id.graph);


        GraphViewUpdater gu = new GraphViewUpdater();
        DataUpdater du = new DataUpdater();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            gu.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, delayGraphViewUpdate);
            du.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, delayMaxDataUpdate);
        } else {
            gu.execute(delayGraphViewUpdate);
            du.execute(delayMaxDataUpdate);
        }

    }

    /**
     * Used by the android test
     * @return a new graph container
     */
    public GraphContainer getGraphContainer() {
        return new GraphContainerThreadSave();
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

        graphContainer.addValuesSafe(time, realValues);
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
        graphContainer = (GraphContainerThreadSave) savedInstanceState.getSerializable(SAVED_STATE_GRAPH_CONTAINER);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putDouble(SAVED_STATE_START_TIME, timeAtStart);
        outState.putSerializable(SAVED_STATE_GRAPH_CONTAINER, graphContainer);
        super.onSaveInstanceState(outState);
    }



    class GraphViewUpdater extends AsyncTask<Long, Void, Void>{


        private GraphViewUpdater(){
            super();
        }


        long delay;
        @Override
        protected Void doInBackground(Long... longs) {
            delay = longs[0];
            Log.d("GRAPHVIEWUPDATER", "delay: " + delay);

            while (true){
                updateGraphView();
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void updateGraphView(){

            graphView.removeAllSeries();
            GraphContainerThreadSave.ValueContainer container = graphContainer.getAllValues();
            float[][] values = container.yValues;
            double [] xIndex = container.xValues;

            if (xIndex.length > 0) {

                DataPoint[][] dataPoints = new DataPoint[numberOfValues][values.length];

                for (int i = 0; i < values.length; i++) {
                    // i is the xIndex
                    for (int j = 0; j < numberOfValues; j++) {
                        // j goes over the different values at one xIndex
                        dataPoints[j][i] = new DataPoint(xIndex[i], values[i][j]);
                    }
                }

                try {

                    graphView.getViewport().setXAxisBoundsManual(true);
                    graphView.getViewport().setMinX(xIndex[0]);
                    graphView.getViewport().setMaxX(xIndex[xIndex.length - 1]);

                    for (int i = 0; i < numberOfValues; i++) {
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints[i]);
                        series.setColor(colors[i % colors.length]);
                        graphView.addSeries(series);
                    }
                }catch (NullPointerException e){
                    Log.e("UPDATEGRAPHVIEW", "NULL POINTEREXCEPTION");
                }

            }

        }
    }

    class DataUpdater extends AsyncTask<Long, Void, Void>{

        long delay;
        @Override
        protected Void doInBackground(Long... longs) {
            delay = longs[0];
            Log.d("DATAUPDATE", "delay: " + delay);

            while (true){
                updateData();
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void updateData(){
            double time = (System.currentTimeMillis()/1000.0) - timeAtStart;
            boolean b = graphContainer.addXValueSafe(time, numberOfValues);

        }

    }

}

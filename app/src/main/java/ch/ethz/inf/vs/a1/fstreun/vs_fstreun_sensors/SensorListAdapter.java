package ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fabio on 10/2/17.
 * Adapter which defines the layout of one item in the sensor list
 */

@SuppressWarnings("WeakerAccess")
public class SensorListAdapter extends ArrayAdapter<Sensor>{

    private static class ViewHolder{
        TextView SensorName;
    }

    public SensorListAdapter(@NonNull Context context, @NonNull List<Sensor> objects) {
        // super call stores objects, which can be access through getItem(position)
        super(context, R.layout.row_item_sensors, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Sensor sensor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_sensors, parent, false);
            viewHolder.SensorName = convertView.findViewById(R.id.textview_name);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (sensor != null) {
            viewHolder.SensorName.setText(sensor.getName());
        }else{
            viewHolder.SensorName.setText(R.string.unknown_sensor_name);
        }
        return convertView;
    }

}

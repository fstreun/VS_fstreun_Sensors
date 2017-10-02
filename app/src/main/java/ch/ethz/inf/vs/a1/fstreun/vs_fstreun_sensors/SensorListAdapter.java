package ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fabio on 10/2/17.
 */

public class SensorListAdapter extends ArrayAdapter<Sensor> implements AdapterView.OnItemClickListener{

    private List<Sensor> dataSet;
    Context mContext;

    private static class ViewHolder{
        TextView SensorName;
    }

    public SensorListAdapter(@NonNull Context context, @NonNull List<Sensor> objects) {
        super(context, R.layout.row_item_sensors, objects);
        this.dataSet = objects;
        this.mContext = context;
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
            viewHolder.SensorName = (TextView) convertView.findViewById(R.id.textview_name);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.SensorName.setText(sensor.getName());
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}

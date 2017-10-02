package ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors;

/**
 * Created by fabio on 10/2/17.
 * copied from the Code_skeleton file
 * https://www.vs.inf.ethz.ch/edu/VS/
 */

@SuppressWarnings("WeakerAccess")
public interface SensorTypes {

    /**
     * Returns the number of values for a specific sensor type, e.g. Sensor.ACCELERATION
     * @param sensorType the sensor type id
     * @return the number of vales for the given sensor type
     */
    int getNumberValues(int sensorType);

    /**
     * Returns the unit for a specific sensor type, e.g. "m/s^2" for Sensor.ACCELERATION
     * @param sensorType the sensor type id
     * @return A string containing the unit for the given sensor type
     */
    String getUnitString(int sensorType);
}

# VS_fstreun_Sensors

<b>Team</b>

streuseli (owner)<br>
brotobia
zenovin

copied from https://www.vs.inf.ethz.ch/edu/VS/exercises/A1/VS_HS2017_Assignment1.pdf

<b>Distributed Systems - Assignment 1.1 Sensing with Android</b>

Every Android application must provide an Activity as an interface for user interaction. In this exercise,
you will let the user access sensors and the corresponding values.

1. Create a new Android project called VS_nethz_Sensors. Set the application name to
VS_nethz_Sensors and the package name to ch.ethz.inf.vs.a1.nethz.sensors
(nethz here and also later means the group leader’s nethz ID). Create the first Activity and name
it MainActivity.
2. In the MainActivity, design a user interface to list all available sensors of the smartphone.
The sensors should be contained in a ListView which automatically resizes with different input
sizes.
3. Create a second Activity called SensorActivity. When the user clicks on an entry in the
ListView, the SensorActivity should be started through an intent. The intent should carry
information about the sensor. Display at least the name of the sensor in the SensorActivity.
4. In the SensorActivity, the sensor’s values should be displayed. First, create a simple
TextView, to show the current values. Note, that you should handle and display the values
you obtain from the sensor correctly according to the type of the sensor, in terms of number
of values (e.g. one for light intensity, three for acceleration) and the corresponding units. For
that reason, create a class which implements the interface SensorTypes we provide. This
class should offer the number of values and the unit for a specific sensor type. Moreover, be
careful to copy the values before using them (see https://developer.android.com/
reference/android/hardware/SensorEventListener.html).
5. Next, you should use a graph to display the history of the sensor values. Place the graph below
the text view. Use the library Graph View (http://www.android-graphview.org/).
The library can be imported very easily by modifying the Gradle scripts as shown in the documentation
and in the listing below.
The graph should show the latest 100 values from the sensor (i.e. a moving window over the
sensor values). If a sensor generates multiple values (e.g. the acceleration sensor), all of them
should be shown in different series (and do use a different colour for each series). The y-axis
should be labelled with the sensor’s corresponding unit, the x-axis with the time of the sensor
readings since starting the SensorActivity (i.e. these time points are changing as well). Please
create a class which implements the interface GraphContainer we provide, which holds the
GraphView and applies all modifications to it. The graph should not restart when the orientation
of the device is changed.
6. We will run automated JUnit and Android instrumentation tests on your code and also provide
some of them to you. To do so it is necessary that you implemented the two aforementioned
interfaces. To run them make sure to add the following lines to your app gradle script in the
according sections.


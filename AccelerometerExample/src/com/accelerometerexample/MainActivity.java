package com.accelerometerexample;

import android.app.Activity;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
import org.openintents.sensorsimulator.hardware.Sensor;
import org.openintents.sensorsimulator.hardware.SensorEvent;
import org.openintents.sensorsimulator.hardware.SensorEventListener;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
	// Accelerometer X, Y, and Z values

	private TextView accelXValue;

	private TextView accelYValue;

	private TextView accelZValue;

	// Orientation X, Y, and Z values

	private TextView orientXValue;

	private TextView orientYValue;

	private TextView orientZValue;

	private SensorManagerSimulator sensorManager = null;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Get a reference to a SensorManager

		setContentView(R.layout.main);

		// Capture accelerometer related view elements

		accelXValue = (TextView) findViewById(R.id.accel_x_value);

		accelYValue = (TextView) findViewById(R.id.accel_y_value);

		accelZValue = (TextView) findViewById(R.id.accel_z_value);

		// Capture orientation related view elements

		orientXValue = (TextView) findViewById(R.id.orient_x_value);

		orientYValue = (TextView) findViewById(R.id.orient_y_value);

		orientZValue = (TextView) findViewById(R.id.orient_z_value);

		// Initialize accelerometer related view elements

		accelXValue.setText("0.00");

		accelYValue.setText("0.00");

		accelZValue.setText("0.00");

		// Initialize orientation related view elements

		orientXValue.setText("0.00");

		orientYValue.setText("0.00");

		orientZValue.setText("0.00");

		sensorManager = SensorManagerSimulator.getSystemService(this, SENSOR_SERVICE);
		sensorManager.connectSimulator();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		sensorManager.registerListener(this, 
//				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//				sensorManager.SENSOR_DELAY_FASTEST);
//		
//		sensorManager.registerListener(this, 
//				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
//				sensorManager.SENSOR_DELAY_FASTEST);
		
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManagerSimulator.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManagerSimulator.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManagerSimulator.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE),SensorManagerSimulator.SENSOR_DELAY_FASTEST);

	}

	@Override
	protected void onStop() {
		sensorManager.unregisterListener(this);
		super.onStop();

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		synchronized(this){
			if(sensorEvent.type == Sensor.TYPE_ACCELEROMETER){
				accelXValue.setText(Float.toString(sensorEvent.values[0]));
				accelYValue.setText(Float.toString(sensorEvent.values[1]));
				accelZValue.setText(Float.toString(sensorEvent.values[2]));
			}
			if(sensorEvent.type == Sensor.TYPE_ORIENTATION){
				orientXValue.setText(Float.toString(sensorEvent.values[0]));
				orientYValue.setText(Float.toString(sensorEvent.values[1]));
				orientZValue.setText(Float.toString(sensorEvent.values[2]));
			}
		}
	}

}
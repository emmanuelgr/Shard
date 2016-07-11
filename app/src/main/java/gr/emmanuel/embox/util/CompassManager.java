package gr.emmanuel.embox.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 Created by Emmanuel on 23/02/14.
 */
public class CompassManager implements SensorEventListener{
private Context context;
private SensorManager sensorManager;
private Sensor accelerometer;
private Sensor magnetometer;
float[] mGravity;
float[] mGeomagnetic;
private float azimuth;
private float pitch;
private float roll;
private static CompassManager instance;

public static CompassManager getInstance( Context context ){
	if ( instance == null ) {
		instance = new CompassManager( context.getApplicationContext() );
	}
	return instance;
}

private CompassManager( Context context ){
	this.context = context;

	sensorManager = (SensorManager) context.getSystemService( Context.SENSOR_SERVICE );
	accelerometer = sensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
	magnetometer = sensorManager.getDefaultSensor( Sensor.TYPE_MAGNETIC_FIELD );
}

public void onResume(){
	sensorManager.registerListener( this, accelerometer, SensorManager.SENSOR_DELAY_UI );
	sensorManager.registerListener( this, magnetometer, SensorManager.SENSOR_DELAY_UI );
}

public void onPause(){
	sensorManager.unregisterListener( this );
}

@Override
public void onSensorChanged( SensorEvent sensorEvent ){

	switch ( sensorEvent.sensor.getType() ) {
	case Sensor.TYPE_ACCELEROMETER:
		mGravity = sensorEvent.values.clone();
		break;
	case Sensor.TYPE_MAGNETIC_FIELD:
		mGeomagnetic = sensorEvent.values.clone();
		break;
	}

	if ( mGravity != null && mGeomagnetic != null ) {
		float[] temp = new float[ 9 ];
		float[] R = new float[ 9 ];
		//Load rotation matrix into R
		SensorManager.getRotationMatrix( temp, null, mGravity, mGeomagnetic );
		//Map to camera's point of view
		SensorManager.remapCoordinateSystem( temp, SensorManager.AXIS_X, SensorManager.AXIS_Z, R );
		//Return the orientation values
		float[] values = new float[ 3 ];
		SensorManager.getOrientation( R, values );
		for ( int i = 0; i < values.length; i++ ) {
			Double degrees = Math.toDegrees( values[ i ] );
			values[ i ] = degrees.floatValue();
		}
//    Log.e(Logs.TAG, String.format("Raw Azimuth: %1$1.2f, Raw Pitch: %2$1.2f, Raw  Roll: %3$1.2f", values[0], values[1], values[2]));
		azimuth = values[ 0 ] * 0.0055556f; //  1 / 180f
		pitch = values[ 1 ] * 0.0055556f; //  1 / 180f
		roll = values[ 2 ] * 0.0055556f; //  1 / 180f
	}
}


@Override
public void onAccuracyChanged( Sensor sensor, int i ){

}

/** @return -1..1 */
public float getAzimuth(){
	return azimuth;
}

public float getPitch(){
	return pitch;
}


public float getRoll(){
	return roll;
}
}

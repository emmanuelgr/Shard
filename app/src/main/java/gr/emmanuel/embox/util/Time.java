package gr.emmanuel.embox.util;

import android.os.SystemClock;
import android.util.Log;

/**
 Created by Emmanuel on 31/01/14.
 Utility class returns time in seconds
 Have to call update in the loop
 */

public class Time{
/** Current time */
public static volatile float ct;
/** Delta Time */
public static volatile float dt;
private static float prevTime;

public static void update(){
	ct = SystemClock.uptimeMillis() * 0.001f;
	if ( prevTime != 0 ) {
		dt = ct - prevTime ;
	}
	prevTime = ct;
//	Log.i(Logs.TAG, "dt: " + String.valueOf( dt ) + " ct: " + String.valueOf( ct ));
}

}

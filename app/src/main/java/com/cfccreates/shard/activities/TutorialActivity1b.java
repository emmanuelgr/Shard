package com.cfccreates.shard.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.cfccreates.shard.R;
import com.cfccreates.shard.SoundsManager;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

/**
 Created by Emmanuel on 17/03/14.
 */
public class TutorialActivity1b extends BaseTutorialActivity{

@Override
protected void onCreate( Bundle savedInstanceState ){
	super.onCreate( savedInstanceState );
	gestureDetector.setScrollListener( scrollListener );
}


@Override
boolean isLoopable(){
	return true;
}

@Override
Uri getVideoUri(){
	return uri = Uri.parse( "android.resource://" + getPackageName() + "/" + R.raw.swipe_loop );
}

@Override
GestureDetector.BaseListener getBaseListener(){
	return new GestureDetector.BaseListener(){
		@Override
		public boolean onGesture( Gesture gesture ){
//			if ( gesture == Gesture.TAP ) {
//				nextActivity();
//				return true;
//		} else if (gesture == Gesture.TWO_TAP) {
//			return true;
//		} else if (gesture == Gesture.SWIPE_RIGHT) {
//			return true;
//		} else if (gesture == Gesture.SWIPE_LEFT) {
//			return true;
//			}
			return false;
		}
	};
}

private GestureDetector.ScrollListener scrollListener = new GestureDetector.ScrollListener(){
	@Override
	public boolean onScroll( float displacement, float delta, float velocity ){
//		Log.i( Logs.TAG, "displacement: " + displacement + " delta: " + delta + " velocity: " + velocity );
//		Log.i( Logs.TAG, "downtime: " + downTime );
		if ( Math.abs( velocity ) < GLActivity.MIN_SWIPE_SCROLL_MOVEMENT ) return true;
		if ( delta > GLActivity.SWIPE_DELTA_THRESHOLD_FOR || delta < GLActivity.SWIPE_DELTA_THRESHOLD_BAC ) {
			soundsManager.playSwipe();
			nextActivity();
		} else {

		}
		return true;
	}
};

@Override
Intent getNextActivity(){
	Intent intent = new Intent( this, TutorialActivity2.class );
	intent.addFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
	return intent;
}
}

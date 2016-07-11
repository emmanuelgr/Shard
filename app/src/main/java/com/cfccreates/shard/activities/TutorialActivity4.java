package com.cfccreates.shard.activities;

import android.content.Intent;
import android.net.Uri;

import com.cfccreates.shard.R;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

/**
 Created by Emmanuel on 17/03/14.
 */
public class TutorialActivity4 extends BaseTutorialActivity{

@Override
boolean isLoopable(){
	return false;
}

@Override
Uri getVideoUri(){
	return uri = Uri.parse( "android.resource://" + getPackageName() + "/" + R.raw.end );
}

@Override
GestureDetector.BaseListener getBaseListener(){
	return new GestureDetector.BaseListener(){
		@Override
		public boolean onGesture( Gesture gesture ){
			if ( gesture == Gesture.TAP ) {
				nextActivity();
				return true;
//		} else if (gesture == Gesture.TWO_TAP) {
//			return true;
//		} else if (gesture == Gesture.SWIPE_RIGHT) {
//			return true;
//		} else if (gesture == Gesture.SWIPE_LEFT) {
//			return true;
			}
			return false;
		}
	};
}


@Override
Intent getNextActivity(){
	Intent intent = new Intent( this, GLActivity.class );
	intent.addFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
	return intent;
}
}

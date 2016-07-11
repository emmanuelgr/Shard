package com.cfccreates.shard.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.cfccreates.shard.LocalStoreKeys;
import com.cfccreates.shard.Model;
import com.cfccreates.shard.R;
import com.cfccreates.shard.SoundsManager;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import gr.emmanuel.embox.util.LocalStore;

/**
 Created by Emmanuel on 17/03/14.
 */
public class TutorialActivity3b extends BaseTutorialActivity{

@Override
protected void onCreate( Bundle savedInstanceState ){
	super.onCreate( savedInstanceState );
}


@Override
boolean isLoopable(){
	return true;
}

@Override
Uri getVideoUri(){
	return uri = Uri.parse( "android.resource://" + getPackageName() + "/" + R.raw.tap_loop );
}

@Override
GestureDetector.BaseListener getBaseListener(){
	return new GestureDetector.BaseListener(){
		@Override
		public boolean onGesture( Gesture gesture ){
			if ( gesture == Gesture.TAP ) {
				soundsManager.playSnapshot();
				tutorialCompleted();
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
	Intent intent = new Intent( this, TutorialActivity4.class );
	intent.addFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
	return intent;
}

}

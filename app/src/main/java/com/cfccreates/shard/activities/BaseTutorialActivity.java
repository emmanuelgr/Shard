package com.cfccreates.shard.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.VideoView;

import com.cfccreates.shard.LocalStoreKeys;
import com.cfccreates.shard.Model;
import com.cfccreates.shard.R;
import com.cfccreates.shard.SoundsManager;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import gr.emmanuel.embox.util.LocalStore;

public abstract class BaseTutorialActivity extends Activity{
public static final String PREV_ACTIVITY_KEY = "prev_activity_key";
private VideoView videoView;
protected GestureDetector gestureDetector;
protected Uri uri;
protected SoundsManager soundsManager;

@Override
protected void onCreate( Bundle savedInstanceState ){
	super.onCreate( savedInstanceState );
	setContentView( R.layout.tutorial_video );

	getWindow().addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );

	videoView = (VideoView) findViewById( R.id.videoView );

	gestureDetector = new GestureDetector( this );

	soundsManager = SoundsManager.getInstance( this );
}

@Override
protected void onResume(){
	super.onResume();
	getVideoUri();
	gestureDetector.setBaseListener( getBaseListener() );
	showVideo();
}

abstract boolean isLoopable();

abstract Uri getVideoUri();

abstract GestureDetector.BaseListener getBaseListener();

abstract Intent getNextActivity();

@Override
public boolean onGenericMotionEvent( MotionEvent event ){
	if ( gestureDetector != null ) {
		return gestureDetector.onMotionEvent( event );
	}
	return false;
}

@Override
public void onBackPressed(){
	ShardsActivities prev = (ShardsActivities) getIntent().getSerializableExtra( BaseTutorialActivity.PREV_ACTIVITY_KEY );
	if ( prev != null ) {
		switch ( prev ) {
		case Settings:
			settingsActivity();
			break;
		default:
			exitActivity();
			break;
		}
	} else {
		exitActivity();
	}
}

protected void showVideo(){
	if ( isLoopable() ) {
		videoView.setOnPreparedListener( new MediaPlayer.OnPreparedListener(){
			@Override
			public void onPrepared( MediaPlayer mediaPlayer ){
				mediaPlayer.setLooping( true );
			}
		} );
	} else {
		videoView.setOnCompletionListener( new MediaPlayer.OnCompletionListener(){
			@Override
			public void onCompletion( MediaPlayer mediaPlayer ){
				nextActivity();
			}
		} );
	}
	videoView.setVideoURI( uri );
	videoView.seekTo( 0 );
	videoView.setVisibility( View.VISIBLE );
	videoView.start();
}

protected void hideVideo(){
	videoView.stopPlayback();
}

protected void exitActivity(){
	Intent intent = new Intent( Intent.ACTION_MAIN );
	intent.addCategory( Intent.CATEGORY_HOME );
	intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
}

protected void settingsActivity(){
	Intent intent = new Intent( Intent.ACTION_MAIN );
	intent = new Intent( this, SettingsActivity.class );
	intent.addFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
}

protected void nextActivity(){
	hideVideo();
	Intent intent = getNextActivity();
	if ( intent == null ) return;
	startActivity( intent );
}

protected void tutorialCompleted(){
	LocalStore.setBoolean( this, LocalStoreKeys.FILENAME, LocalStoreKeys.TUTORIAL_COMPLETE, true );
	Model.tutorialComplete = true;
}
}

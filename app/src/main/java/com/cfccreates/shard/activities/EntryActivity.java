package com.cfccreates.shard.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.cfccreates.shard.LocalStoreKeys;
import com.cfccreates.shard.Model;
import com.cfccreates.shard.R;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import gr.emmanuel.embox.util.LocalStore;
import gr.emmanuel.embox.util.Logs;

public class EntryActivity extends Activity{
private ImageView imageViewShard;
private ImageView imageViewCFC;
private GestureDetector gestureDetector;
private ViewPropertyAnimator viewPropertyAnimator;



@Override
protected void onCreate( Bundle savedInstanceState ){
	super.onCreate( savedInstanceState );


	final ActivityManager activityManager = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
	final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
	final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
	if ( !supportsEs2 ) {
		Log.e( Logs.TAG, "No support for OGL2 Exiting..." );
		finish();
	} else {

		initModel();

		showSplash();

		MediaPlayer.create( getApplicationContext(), R.raw.startup2_1 ).start();
	}
}

private void showSplash(){
	setContentView( R.layout.activity_shard_logo );
	getWindow().addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );

	gestureDetector = new GestureDetector( this );
	gestureDetector.setBaseListener( baseListener );

	imageViewShard = (ImageView) findViewById( R.id.imageviewShard );
	imageViewCFC = (ImageView) findViewById( R.id.imageviewCfc );

	imageViewShard.setAlpha( 0f );
	imageViewShard.setVisibility( View.VISIBLE );
	imageViewCFC.setVisibility( View.INVISIBLE );
	viewPropertyAnimator = imageViewShard.animate().alpha( 1f ).setDuration( 700 ).setInterpolator( new DecelerateInterpolator() ).setListener( new AnimatorListenerAdapter(){
		@Override
		public void onAnimationEnd( Animator animation ){
			imageViewCFC.setVisibility( View.VISIBLE );
			imageViewShard.animate().alpha( 0f ).setDuration( 700 ).setStartDelay( 2200 ).setInterpolator( new DecelerateInterpolator() ).setListener( new AnimatorListenerAdapter(){
				@Override
				public void onAnimationEnd( Animator animation ){
					imageViewShard.setVisibility( View.GONE );
					viewPropertyAnimator = imageViewCFC.animate().alpha( 0f ).setDuration( 700 ).setStartDelay( 1500 ).setInterpolator( new DecelerateInterpolator() ).setListener( new AnimatorListenerAdapter(){
						@Override
						public void onAnimationEnd( Animator animation ){
							imageViewCFC.setVisibility( View.GONE );
							decide();
						}
					} );
				}
			} );
		}
	} );
}


private void initModel(){

	Display display = getWindowManager().getDefaultDisplay();
	Point resolution = new Point();
	display.getSize( resolution );
	Model.screenW = resolution.x;
	Model.screenH = resolution.y;

	// Update Model from prefernces file
//	Model.email = AccountManager.get( this ).getAccounts()[ 0 ].name;
	Model.userID = LocalStore.getString( this, LocalStoreKeys.FILENAME, LocalStoreKeys.CALLISTO_USER_ID, "" );

	boolean isAppRunningForFirstTime = LocalStore.getBoolean( this, LocalStoreKeys.FILENAME, LocalStoreKeys.FIRST_RUN, true );
	if ( isAppRunningForFirstTime ) {
		LocalStore.setBoolean( this, LocalStoreKeys.FILENAME, LocalStoreKeys.FIRST_RUN, false );
		Model.firstRun = true;
	} else {
		Model.firstRun = false;
	}
	boolean tutorialComplete = LocalStore.getBoolean( this, LocalStoreKeys.FILENAME, LocalStoreKeys.TUTORIAL_COMPLETE, false );
	if ( tutorialComplete ) {
		Model.tutorialComplete = true;
	} else {
		Model.tutorialComplete = false;
	}
	Model.audioOn = LocalStore.getBoolean( this, LocalStoreKeys.FILENAME, LocalStoreKeys.AUDIO, true );
	Model.visualOn = LocalStore.getBoolean( this, LocalStoreKeys.FILENAME, LocalStoreKeys.VISUAL, true );
	Model.sharingOn = LocalStore.getBoolean( this, LocalStoreKeys.FILENAME, LocalStoreKeys.SHARE, false );

	Model.deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

//	AccountManager accountManager = AccountManager.get(this);
//// Use your Glassware's account type.
//	android.accounts.Account[] accounts = accountManager.getAccountsByType("com.google");
//	android.accounts.Account account = accounts[0];
//
//	// Your auth token type.
//	final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/login";
//	accountManager.getAuthToken(account, AUTH_TOKEN_TYPE, null, this, new AccountManagerCallback<Bundle>() {
//		public void run(AccountManagerFuture<Bundle> future) {
//			try {
//				String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
//				// Use the token.
//				Log.i( Logs.TAG, token );
//			} catch (Exception e) {
//				// Handle exception.
//				Log.e( Logs.TAG, "exception", e);
//			}
//		}
//	}, null);

}

@Override
public boolean onGenericMotionEvent( MotionEvent event ){
	if ( gestureDetector != null ) {
		return gestureDetector.onMotionEvent( event );
	}
	return false;
}

private GestureDetector.BaseListener baseListener = new GestureDetector.BaseListener(){
	@Override
	public boolean onGesture( Gesture gesture ){
		if ( gesture == Gesture.TAP ) {
			// do something on tap
			if ( viewPropertyAnimator != null ) viewPropertyAnimator.cancel();
			decide();
//			launchGL();
			return true;
//		} else if (gesture == Gesture.TWO_TAP) {
//			// do something on two finger tap
//			return true;
//		} else if (gesture == Gesture.SWIPE_RIGHT) {
//			// do something on right (forward) swipe
//			return true;
//		} else if (gesture == Gesture.SWIPE_LEFT) {
//			// do something on left (backwards) swipe
//			return true;
		}
		return false;
	}
};

//@Override
//public void onBackPressed(){
//	imageViewShard.animate().cancel();
//	Intent intent = new Intent( Intent.ACTION_MAIN );
//	intent.addCategory( Intent.CATEGORY_HOME );
//	intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
//	startActivity( intent );
//}

private void decide(){

//	tutorialActivity();

	if ( Model.firstRun ) {
		tutorialActivity();
	} else {
		launchGL();
//		testFB();
	}
}

private void tutorialActivity(){
	Intent intent = new Intent( this, TutorialActivity1.class );
	intent.putExtra( BaseTutorialActivity.PREV_ACTIVITY_KEY, ShardsActivities.Splash );
	intent.addFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
	startActivity( intent );
}

private void launchGL(){
	Intent intent = new Intent( this, GLActivity.class );
	intent.addFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
	startActivity( intent );
}

private void testFB(){
//	Intent intent = new Intent( this, FB_ActivationActivity.class );
//	startActivity( intent );
}

}

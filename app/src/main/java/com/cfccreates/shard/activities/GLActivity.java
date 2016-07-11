package com.cfccreates.shard.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import gr.emmanuel.embox.util.CompassManager;
import com.cfccreates.shard.GLRenderer;
import com.cfccreates.shard.Model;
import com.cfccreates.shard.R;
import com.cfccreates.shard.SoundsManager;
import com.cfccreates.shard.fxs.FxSettings;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import gr.emmanuel.embox.util.Logs;

public class GLActivity extends Activity{
public static final float SWIPE_DELTA_THRESHOLD_FOR = 185f;
public static final float SWIPE_DELTA_THRESHOLD_BAC = -110f;
public static final float MIN_SWIPE_SCROLL_MOVEMENT = 0.5f;
private GLSurfaceView glSurfaceView;
private GestureDetector gestureDetector;
private GLRenderer renderer;
//private AudioManager audioManager;
private FrameLayout frameLayoutVars;
private FrameLayout frameLayoutVols;
private FrameLayout frameLayoutZooms;
private ImageView imageViewFlash;
private ImageView[] imageViewsVols;
private ImageView[] imageViewsVars;
private ImageView[] imageViewsZooms;
private TextView feedbackView;
private TextView titleView;
private TextView textViewReminder;
private String titlePrev;
//public TutorialScrollView tutorialScrollView;
//public SettingsScrollView settingsScrollView;

private ViewPropertyAnimator feedbackPropertyAnimator;
private ViewPropertyAnimator viewPropertyAnimator;
private ViewPropertyAnimator titlePropertyAnimator;
private ViewPropertyAnimator imageViewFlashPropertyAnimator;
private SoundsManager soundsManager;
private long downTime;
private CompassManager compassManager;
private Handler handlerSwipe;
private ProgressBar progressBar;

@Override
protected void onCreate( Bundle savedInstanceState ){
	super.onCreate( savedInstanceState );
	init();
}

@Override
protected void onResume(){
	super.onResume();
	glSurfaceView.onResume();
	compassManager.onResume();
	renderer.onResume();
}

@Override
protected void onPause(){
	super.onPause();
	glSurfaceView.onPause();
	renderer.onPause();
	compassManager.onPause();
}

//@Override
//public boolean onCreateOptionsMenu( Menu menu ){
//	getMenuInflater().inflate( R.menu.gl, menu );
//	return true;
//}
//
//@Override
//public boolean onOptionsItemSelected( MenuItem item ){
//	// Handle item selection. Menu items typically start another
//	// activity, start a service, or broadcast another intent.
//	Resources resources = getResources();
//	switch ( item.getItemId() ) {
//	case R.id.audio_menu_item:
//		if ( item.getTitle() == resources.getString( R.string.audioOn ) ) {
//			item.setTitle( resources.getString( R.string.audioOn ) );
//		} else {
//			item.setTitle( resources.getString( R.string.audioOff ) );
//		}
//		return false;
//	case R.id.visual_menu_item:
////		startActivity( new Intent( this, TutorialActivity.class ) );
//		if ( item.getTitle() == resources.getString( R.string.visualOn ) ) {
//			item.setTitle( resources.getString( R.string.visualOff ) );
//		} else {
//			item.setTitle( resources.getString( R.string.visualOn ) );
//		}
//		return false;
//	case R.id.tutorial_menu_item:
////		startActivity( new Intent( this, TutorialActivity.class ) );
//		return false;
//	default:
//		return super.onOptionsItemSelected( item );
//	}
//}

private void init(){

	findViews();

	compassManager = CompassManager.getInstance( this );

	gestureDetector = new GestureDetector( this );

//	audioManager = (AudioManager) getSystemService( Context.AUDIO_SERVICE );
	soundsManager = SoundsManager.getInstance( this );

	startListening();

	handlerSwipe = new Handler();

	initGL();
}


private void findViews(){
	setContentView( R.layout.activity_gl );


	feedbackView = (TextView) findViewById( R.id.feedbackView );
	titleView = (TextView) findViewById( R.id.titleView );
	textViewReminder = (TextView) findViewById( R.id.textViewReminder );
	progressBar = (ProgressBar) findViewById(R.id.progressBar);
	//
	imageViewFlash = (ImageView) findViewById( R.id.imageViewFlash );
	//
	frameLayoutVars = (FrameLayout) findViewById( R.id.frameLayoutVars );
	frameLayoutVols = (FrameLayout) findViewById( R.id.frameLayoutVols );
	frameLayoutZooms = (FrameLayout) findViewById( R.id.frameLayoutZooms );

	imageViewsVols = new ImageView[]{
		(ImageView) findViewById( R.id.vol0 ),
		(ImageView) findViewById( R.id.vol1 ),
		(ImageView) findViewById( R.id.vol2 ),
		(ImageView) findViewById( R.id.vol3 ),
		(ImageView) findViewById( R.id.vol4 ),
		(ImageView) findViewById( R.id.vol5 ),
		(ImageView) findViewById( R.id.vol6 )
	};

	imageViewsVars = new ImageView[]{
		(ImageView) findViewById( R.id.var00 ),
		(ImageView) findViewById( R.id.var01 ),
		(ImageView) findViewById( R.id.var02 ),
		(ImageView) findViewById( R.id.var03 ),
		(ImageView) findViewById( R.id.var04 ),
		(ImageView) findViewById( R.id.var05 ),
		(ImageView) findViewById( R.id.var06 ),
		(ImageView) findViewById( R.id.var07 ),
		(ImageView) findViewById( R.id.var08 ),
		(ImageView) findViewById( R.id.var09 ),
		(ImageView) findViewById( R.id.var10 ),
		(ImageView) findViewById( R.id.var11 ),
		(ImageView) findViewById( R.id.var12 ),
		(ImageView) findViewById( R.id.var13 ),
		(ImageView) findViewById( R.id.var14 ),
		(ImageView) findViewById( R.id.var15 ),
		(ImageView) findViewById( R.id.var16 ),
		(ImageView) findViewById( R.id.var17 ),
		(ImageView) findViewById( R.id.var18 ),
		(ImageView) findViewById( R.id.var19 ),
		(ImageView) findViewById( R.id.var20 ),
		(ImageView) findViewById( R.id.var21 ),
		(ImageView) findViewById( R.id.var22 ),
		(ImageView) findViewById( R.id.var23 ),
		(ImageView) findViewById( R.id.var24 ),
		(ImageView) findViewById( R.id.var25 ),
		(ImageView) findViewById( R.id.var26 ),
		(ImageView) findViewById( R.id.var27 ),
		(ImageView) findViewById( R.id.var28 ),
		(ImageView) findViewById( R.id.var29 )
};


	imageViewsZooms = new ImageView[]{
     (ImageView) findViewById( R.id.zoom00 ),
     (ImageView) findViewById( R.id.zoom01 ),
     (ImageView) findViewById( R.id.zoom02 ),
     (ImageView) findViewById( R.id.zoom03 ),
     (ImageView) findViewById( R.id.zoom04 ),
     (ImageView) findViewById( R.id.zoom05 ),
     (ImageView) findViewById( R.id.zoom06 ),
     (ImageView) findViewById( R.id.zoom07 ),
     (ImageView) findViewById( R.id.zoom08 ),
     (ImageView) findViewById( R.id.zoom09 ),
     (ImageView) findViewById( R.id.zoom10 ),
     (ImageView) findViewById( R.id.zoom11 ),
     (ImageView) findViewById( R.id.zoom12 ),
     (ImageView) findViewById( R.id.zoom13 )
	};
}

private void initGL(){

	glSurfaceView = new GLSurfaceView( this );
	glSurfaceView.setEGLContextClientVersion( 2 );
	glSurfaceView.setPreserveEGLContextOnPause(true);
	renderer = new GLRenderer( this );
	glSurfaceView.setRenderer( renderer );

	FrameLayout frameLayout = (FrameLayout) findViewById( R.id.mains_view_frameLayout );

	frameLayout.addView( glSurfaceView, 0 );

//	settingsScrollView = new SettingsScrollView( this );
//	frameLayout.addView( settingsScrollView.cardScrollView, frameLayout.getChildCount()-1 );
//
//	tutorialScrollView = new TutorialScrollView( this );
//	frameLayout.addView( tutorialScrollView.cardScrollView, frameLayout.getChildCount()-1 );

//	settingsView = getLayoutInflater().inflate( R.layout.view_settings, frameLayout );
}

private void startListening(){
	gestureDetector.setAlwaysConsumeEvents( true );
	gestureDetector.setBaseListener( baseListener );
	gestureDetector.setScrollListener( scrollListener );
	gestureDetector.setTwoFingerScrollListener( twoFingerScrollListener );
}

// Send generic motion events to the gesture detector
@Override
public boolean onGenericMotionEvent( MotionEvent event ){

	if( !renderer.isInitialised)return false;

	if ( gestureDetector != null ) {
		switch ( event.getAction() ) {
		case MotionEvent.ACTION_DOWN:
			downTime = SystemClock.uptimeMillis();
			break;
		case MotionEvent.ACTION_UP:
			downTime = 0;
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		}
		return gestureDetector.onMotionEvent( event );
	}
	return false;
}

private GestureDetector.BaseListener baseListener = new GestureDetector.BaseListener(){
	@Override
	public boolean onGesture( Gesture gesture ){
		switch ( gesture ) {
		case TAP:
			Log.i( Logs.TAG, "one finger tap" );
			if ( renderer.currentFX instanceof FxSettings ) {
//				openMenu();
//				settingsScrollView.show();
				soundsManager.playIntensity();
				startSettings();
			} else {
//				randFX();
				snapShot();
			}
			return true;
//		case TWO_TAP:
//			randFX();
//			return true;
//		case TWO_LONG_PRESS:
//			Log.i( Logs.TAG, " two finger longer" );
//			return true;
		case SWIPE_LEFT:
			Log.e( Logs.TAG, "SWIPE_LEFT called." );
			prevFX();
			return true;
		case SWIPE_RIGHT:
			Log.e( Logs.TAG, "SWIPE_RIGHT called." );
			nextFX();
			return true;
//		case LONG_PRESS:
//			Log.e( Logs.TAG, "LONG_PRESS called." );
//			return true;
			case SWIPE_DOWN:
				Log.i( Logs.TAG, "SWIPE_DOWN called." );
				if ( renderer.captureScreenShot ) return true;
				Intent intent = new Intent( GLActivity.this, MenuActivity.class );
				intent.setFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
				startActivity( intent );
				return true;
		}
		return false;
	}
};

private GestureDetector.ScrollListener scrollListener = new GestureDetector.ScrollListener(){
	@Override
	public boolean onScroll( float displacement, float delta, float velocity ){
//		Log.i( Logs.TAG, "displacement: " + displacement + " delta: " + delta + " velocity: " + velocity );
//		Log.i( Logs.TAG, "downtime: " + downTime );
//		if ( Math.abs( velocity ) < MIN_SWIPE_SCROLL_MOVEMENT ) return true;
//		if ( delta > SWIPE_DELTA_THRESHOLD_FOR || delta < SWIPE_DELTA_THRESHOLD_BAC ) {
//			if ( displacement > 0 ) {
//				nextFX();
//			} else {
//				prevFX();
//			}
//		} else if ( downTime + 200 < SystemClock.uptimeMillis() ) {
			variationFX( delta );
//		}
		return true;
	}
};

private GestureDetector.TwoFingerScrollListener twoFingerScrollListener = new GestureDetector.TwoFingerScrollListener(){
	@Override
	public boolean onTwoFingerScroll( float displacement, float delta, float velocity ){
		if ( Math.abs( velocity ) < MIN_SWIPE_SCROLL_MOVEMENT ) return true;
//		intensity( delta );
		return true;
	}
};

@Override
public boolean onKeyDown( int keyCode, KeyEvent event ){
	if ( keyCode == KeyEvent.KEYCODE_CAMERA ) {
		Log.i( Logs.TAG, "Camera Down" );
		return true;
	}else {
		return super.onKeyDown( keyCode, event );
	}
}

@Override
public boolean onKeyUp( int keyCode, KeyEvent event ){
	if ( renderer.captureScreenShot ) return true;

	if ( keyCode == KeyEvent.KEYCODE_CAMERA ) {
		Log.i( Logs.TAG, "Camera Up" );
		snapShot();
		return true;
	} else {
		return super.onKeyUp( keyCode, event );
	}
}

public void prevFX(){
	if ( renderer.captureScreenShot ) return;
	renderer.prev();
//	flashTitle( titles[ renderer.indexFX ] );
	flashTitle( renderer.currentFX.title );
	soundsManager.playSwipe();
//	if ( renderer.currentFX instanceof FxSettings ) {
//		settingsScrollView.show();
//	}
}

public void nextFX(){
	if ( renderer.captureScreenShot ) return;

	renderer.next();
//	flashTitle( titles[ renderer.indexFX ] );
	flashTitle( renderer.currentFX.title );
	soundsManager.playSwipe();
}

public void startTutorial(){
	Intent intent = new Intent( this, BaseTutorialActivity.class );
	intent.putExtra( ShardsActivities.class.getName(), ShardsActivities.Tutorial);
	intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	startActivity( intent );
}

public void startSettings(){
	Intent intent = new Intent( this, SettingsActivity.class );
	intent.addFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
	startActivity( intent );
}

private void showViewByIndex( ImageView[] arr, int index ){
	for ( int i = 0; i < arr.length; i++ ) {
		arr[ i ].setVisibility( View.GONE );
	}
	arr[ index ].setVisibility( View.VISIBLE );
}

private void randFX(){
	if ( !renderer.currentFX.randomizable ) return;

	soundsManager.playRandomizing();

	renderer.randomizeFX();

	int index = (int) Math.floor( Math.random() * ( imageViewsVars.length - 1f ) );
	showViewByIndex( imageViewsVars, index );
	flashView( frameLayoutVars );
//	flashFeedback( "Randomizing" );
}

private void variationFX( float delta ){
	if ( renderer.captureScreenShot ) return;
	if ( !renderer.currentFX.variationable ) return;

	renderer.currentFX.incrementScroll( delta * renderer.currentFX.scrollerIncrements );

	int index;
	if ( renderer.currentFX.linearVariation ) {
		index = (int) Math.floor( renderer.currentFX.getScroll() * ( imageViewsZooms.length - 1 ) );
		showViewByIndex( imageViewsZooms, index );
		flashView( frameLayoutZooms );
		if ( renderer.currentFX.getScroll() == 0 || renderer.currentFX.getScroll() == 1 ) return;
	} else {
		index = (int) Math.floor( Math.random() * ( imageViewsVars.length - 1 ) );
		showViewByIndex( imageViewsVars, index );
		flashView( frameLayoutVars );
	}

//	soundsManager.playVariation();
//	flashFeedback( renderer.currentFX.feedbackText );
}

private void intensity( float delta ){
	if ( !renderer.currentFX.intesible ) return;

	renderer.currentFX.intencity -= delta * 0.001f;
	if ( renderer.currentFX.intencity > 1f ) renderer.currentFX.intencity = 1f;
	if ( renderer.currentFX.intencity < 0 ) renderer.currentFX.intencity = 0;

	soundsManager.playIntensity();
	int index = (int) ( Math.floor( ( 1f - renderer.currentFX.intencity ) * ( imageViewsVols.length - 1f ) ) );
	showViewByIndex( imageViewsVols, index );
	flashView( frameLayoutVols );
//	flashFeedback( "Intensity: " + ( (int) ( ( 1f - renderer.currentFX.intencity ) * 100 ) ) + "%" );
}

private void openMenu(){
	openOptionsMenu();
}

private void snapShot(){
	if ( renderer.captureScreenShot ) return;
	if ( !renderer.currentFX.snapshotable ) return;

	flashSnapshot();
	renderer.snap();
	soundsManager.playSnapshot();
//	flashFeedback( "Snapshot" );
}

private void flashFeedback( String text ){
	if ( !Model.visualOn ) return;

	if ( feedbackPropertyAnimator != null ) feedbackPropertyAnimator.cancel();
	feedbackView.setText( text );
	feedbackView.setAlpha( 1f );
	feedbackView.setVisibility( View.VISIBLE );
	feedbackPropertyAnimator = feedbackView.animate().alpha( 0f ).setDuration( 1500 ).setListener( new AnimatorListenerAdapter(){
		@Override
		public void onAnimationEnd( Animator animation ){
			feedbackView.setVisibility( View.GONE );
		}
	} );

}

private void flashTitle( String text ){
	if ( !Model.visualOn ) return;
	if ( renderer.currentFX instanceof FxSettings ) return;
	if ( titlePrev == text ) return;
	titlePrev = text;
	if ( titlePropertyAnimator != null ) titlePropertyAnimator.cancel();
	titleView.setText( text );
	titleView.setAlpha( 1f );
	titleView.setVisibility( View.VISIBLE );
	titlePropertyAnimator = titleView.animate().alpha( 0f ).setDuration( 900 ).setStartDelay( 500 ).setListener( new AnimatorListenerAdapter(){
		@Override
		public void onAnimationEnd( Animator animation ){
			titleView.setVisibility( View.GONE );
		}
	} );
	// Hack to add icon for settings <<<<<<<<<<<<<<<------------------
//	if ( renderer.currentFX instanceof FxSettings ){
//		imageViewCog.setAlpha( 1f );
//		imageViewCog.setVisibility( View.VISIBLE );
//		imageViewCog.animate().alpha( 0f ).setDuration( 900 ).setStartDelay( 500 ).setListener( new AnimatorListenerAdapter(){
//			@Override
//			public void onAnimationEnd( Animator animation ){
//				imageViewCog.setVisibility( View.GONE );
//			}
//		} );
//	}
}

private void flashReminder( String text ){

	textViewReminder.setAlpha( 0f );
	textViewReminder.setText( text );
	textViewReminder.setVisibility( View.VISIBLE );
	textViewReminder.animate().alpha( 1f ).setDuration( 900 ).setListener( new AnimatorListenerAdapter(){
		@Override
		public void onAnimationEnd( Animator animation ){

			textViewReminder.animate().alpha( 0f ).setDuration( 900 ).setStartDelay( 5000 ).setListener( new AnimatorListenerAdapter(){
				@Override
				public void onAnimationEnd( Animator animation ){
					textViewReminder.setVisibility( View.GONE );
				}
			} );
		}
	} );
}

private void flashSnapshot(){
	if ( !Model.visualOn ) return;

	if ( imageViewFlashPropertyAnimator != null ) imageViewFlashPropertyAnimator.cancel();
	imageViewFlash.setAlpha( 1f );
	imageViewFlash.setVisibility( View.VISIBLE );
	imageViewFlashPropertyAnimator = imageViewFlash.animate().alpha( 0f ).setDuration( 500 ).setInterpolator( new DecelerateInterpolator() ).setListener( new AnimatorListenerAdapter(){
		@Override
		public void onAnimationEnd( Animator animation ){
			imageViewFlash.setVisibility( View.GONE );
		}
	} );
}

private void flashView( View view ){
	if ( !Model.visualOn ) return;

	if ( viewPropertyAnimator != null ) viewPropertyAnimator.cancel();
	final View v = view;
	v.setAlpha( 1f );
	v.setVisibility( View.VISIBLE );
	viewPropertyAnimator = v.animate().alpha( 0f ).setDuration( 1500 ).setListener( new AnimatorListenerAdapter(){
		@Override
		public void onAnimationEnd( Animator animation ){
			v.setVisibility( View.GONE );
			viewPropertyAnimator = null;
		}
	} );
}

Runnable runnableSwipe = new Runnable() {
	@Override
	public void run() {
		handlerSwipe.postDelayed( runnableSwipe, 40000 );
//		showSwipe();
	}
};


public void runOnceOnInit(){

	progressBar.setVisibility( View.GONE );

	nextFX();

	if ( !Model.tutorialComplete ) {
		final Handler handler = new Handler();
		handler.postDelayed( new Runnable(){
			@Override
			public void run(){
				handlerSwipe.postDelayed( runnableSwipe, 30000 );
			}
		}, 10000 );
	}

}
}

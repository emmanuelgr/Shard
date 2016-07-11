package com.cfccreates.shard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.cfccreates.shard.activities.GLActivity;
import com.cfccreates.shard.fxs.AFX;
import com.cfccreates.shard.fxs.BlackMasks;
import com.cfccreates.shard.fxs.FxA;
import com.cfccreates.shard.fxs.FxB;
import com.cfccreates.shard.fxs.FxBW;
import com.cfccreates.shard.fxs.FxCalido;
import com.cfccreates.shard.fxs.FxCamera;
import com.cfccreates.shard.fxs.FxHazes;
import com.cfccreates.shard.fxs.FxOffset;
import com.cfccreates.shard.fxs.FxVignette;
import com.google.android.glass.app.Card;
import com.google.android.glass.timeline.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import EmBoxUnity.Utils.Interpolate;
import gr.emmanuel.embox.gles2.Camera;
import gr.emmanuel.embox.gles2.FramesPerSecond;
import gr.emmanuel.embox.gles2.Object3D;
import gr.emmanuel.embox.gles2.Transform;
import gr.emmanuel.embox.gles2.shaders.Blend;
import gr.emmanuel.embox.gles2.shaders.ShaderCamera;
import gr.emmanuel.embox.gles2.shaders.ShaderCameraGreys;
import gr.emmanuel.embox.gles2.shaders.ShaderCameraOffset;
import gr.emmanuel.embox.gles2.shaders.ShaderTexture;
import gr.emmanuel.embox.gles2.shaders.Textures;
import gr.emmanuel.embox.util.Logs;
import gr.emmanuel.embox.util.Time;

public class GLRenderer implements Renderer{
private Object3D fgCamFeed;
private Object3D hashes;
private GLActivity context;
private CameraHardware camHW;
private Camera camera;
public volatile boolean captureScreenShot = false;
private Transform mamaFx;
private Transform world;
private AFX[] fxs;
private int filterOffset;
public volatile AFX currentFX;
private float mamaFxTimer = Float.MAX_VALUE;
private float bounceFxTimer = Float.MAX_VALUE;
private float mamaFxSwipeDuration = 0.5f;
private float bounceFxSwipeDuration = 0.5f;
private BlackMasks blackMasks;
private SurfaceTexture surfaceTexture;
public volatile boolean isInitialised = false;
private boolean runOnceOnInit = true;
private FramesPerSecond fps;

public GLRenderer( GLActivity context ){
	this.context = context;
	camHW = new CameraHardware( Model.screenW * 2, Model.screenH * 2 );
}

@Override
public void onSurfaceCreated( GL10 gl, EGLConfig config ){

//	GLES20.glDisable( GLES20.GL_CULL_FACE );
	GLES20.glDisable( GLES20.GL_DEPTH_TEST );
	GLES20.glEnable( GLES20.GL_BLEND );

	filterOffset = -1;

	Library.forceUpdate( context );
	fps = new FramesPerSecond(30);

	world = new Transform();

	camera = new Camera();
	camera.transformEye.setPositionZ( 0 );
//	camera.transformEye.setPositionZ( -10 );
	camera.lookAt.z = -1f;

	int texture = Textures.createCameraTexture();
	surfaceTexture = new SurfaceTexture( texture );

	camHW.start( surfaceTexture );

	hashes = new Object3D( 1, new ShaderTexture( R.raw.hashes, context ), Library.getInstance( context ).getInstance( context ).meshSquare, Blend.Modes.NORMAL );
	hashes.transform.setScale( 0.5f );
	hashes.transform.setPositionX( 1.3f + 0.25f);
	hashes.transform.setPositionY( -0.53f - 0.25f);
	hashes.transform.setPositionZ( -1.7f );
	camera.transformEye.addChild( hashes.transform );

	ShaderCamera shaderCamera = new ShaderCamera( surfaceTexture, texture );
	fgCamFeed = new Object3D( 1, shaderCamera, Library.getInstance( context ).meshSquare, Blend.Modes.NORMAL_PRE_MULT );
	fgCamFeed.transform.setPositionZ( -camera.near - 0.1f );
	camera.transformEye.addChild( fgCamFeed.transform );

	mamaFx = new Transform();

	AFX.count = 0;

	fxs = new AFX[]{
		new FxCalido( context, mamaFx, shaderCamera ),
		new FxB( context, mamaFx, shaderCamera ),
		new FxA(  context, mamaFx, shaderCamera ),
		new FxBW( context, mamaFx, new ShaderCameraGreys( surfaceTexture, texture ) ),
		new FxHazes( context, mamaFx, shaderCamera ),
		new FxVignette( context, mamaFx, shaderCamera ),
		new FxOffset( context, mamaFx, new ShaderCameraOffset( surfaceTexture, texture ) ),
	};

	mamaFx.setPositionX( -filterOffset );
	mamaFx.setPositionZ( -2f );


	for ( int i = 0; i < fxs.length; i++ ) {
		fxs[ i ].onSurfaceCreated();
		fxs[ i ].randomize();
	}

	isInitialised = true;

	currentFX = fxs[fxs.length-1];
}

public void onResume(){
	camHW.start( surfaceTexture );
}

public void onPause(){
	camHW.stop();
}

@Override
public void onSurfaceChanged( GL10 gl, int width, int height ){
	// viewport will be fullscreen
	GLES20.glViewport( 0, 0, width, height );
	camera.onSurfaceChanged( width, height );

	for ( int i = 0; i < fxs.length; i++ ) {
		fxs[ i ].onSurfaceChanged( width, height );
	}

//	bgCamFeed.transform.setScale( Camera.getMain().matchCamFrustrum( bgCamFeed.transform.getPosGlobal() ) );
	fgCamFeed.transform.setScale( Camera.getMain().matchCamFrustrum( fgCamFeed.transform.getPosGlobal() ) );
	randomizeFX();
}


@Override
public void onDrawFrame( GL10 gl ){

	if ( captureScreenShot ) {
		capturingScreenShot();
		captureScreenShot = false;
	}

	fps.start();
	Time.update();

	GLES20.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );
	GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT );

	render();

	if ( runOnceOnInit ) {
		runOnceOnInit = false;
		context.runOnUiThread( new Runnable(){
			public void run(){
				context.runOnceOnInit();
			}
		} );
	}

	fps.end();
}

private void render(){
//
//	float oscillate1 = (float) ( Math.cos( Time.ct * 1.0f ) );
//	float oscillate2 = (float) ( Math.cos( Time.ct * 0.8f ) );

//	world.setRotationY( ( oscillate1 + 1f ) * 0.5f * 360f );

//	world.setRotationY( Input.getInstance().getPitch()*360f );
//	Log.i(Logs.TAG, "world: " + world.getRotationY());

//	camera.transformEye.setRotationX( camera.transformEye.getRotationX() + ( Input.getInstance().getPitch() * -180f  - camera.transformEye.getRotationX() ) * 0.1f);
//	camera.transformEye.setRotationY( camera.transformEye.getRotationY() + ( Input.getInstance().getAzimuth() * -180f - camera.transformEye.getRotationY() ) * 0.1f);
//	camera.transformEye.setRotationZ( camera.transformEye.getRotationZ() + ( Input.getInstance().getRoll() * 180f    - camera.transformEye.getRotationZ() ) * 0.1f);
//	camera.transformEye.setRotationZ( oscillate2 * 360f );
//	camera.fov = ( ( oscillate2 + 1f ) * 0.5f * 155f + 5f );
//	camera.transformEye.setPositionZ( ( oscillate1 + 1f ) * 0.5f * -6f);
//	camera.transformEye.setPositionZ( Math.min( 0, Input.getInstance().getScroll()*-5f));

//	camera.transformEye.setPositionZ( -5f );
	camera.onDrawFrame();

//	bounceFxTimer += Time.dt;
//	if ( bounceFxTimer < bounceFxSwipeDuration ) {
//		float ratio = bounceFxTimer / bounceFxSwipeDuration;
//		float x = Interpolate.inOutQuad( 0, 2f, Interpolate.zeroOnezero( 0, 1f, ratio ) );
//		mamaFx.setPositionOffsetX( filterOffset == 1 ? x * 1 : x * -1f );
//	}

	mamaFxTimer += Time.dt;
	if ( mamaFxTimer < mamaFxSwipeDuration ) {
		float mamaFxNextPos = filterOffset * -Model.fxCardsDistance;
		float mamaFxSwipeRatio = mamaFxTimer / mamaFxSwipeDuration;
		float x = Interpolate.outCubic( mamaFx.getPositionX(), mamaFxNextPos, mamaFxSwipeRatio );
		mamaFx.setPositionX( x );
	}

	currentFX.onDrawFrame();
//	blackMasks.render();
/*	foreground camera*/
//	fgCamFeed.transform.setScale( Camera.getMain().matchCamFrustrum( fgCamFeed.transform.getPosGlobal() ) );
//	fgCamFeed.color[ 3 ] = currentFX.intencity;
//	fgCamFeed.onDrawFrame();

}

public void snap(){
	if ( !isInitialised ) return;
	captureScreenShot = true;
}

public void randomizeFX(){
	if ( !isInitialised ) return;
	if ( captureScreenShot ) return;

	for ( int i = 0; i < fxs.length; i++ ) {
		fxs[ i ].randomize();
	}
}

public void next(){
	if ( !isInitialised ) return;
	if ( captureScreenShot ) return;

	if ( mamaFxTimer < mamaFxSwipeDuration ) return;
	filterOffset++;
	figureIndex();
}

public void prev(){
	if ( !isInitialised ) return;
	if ( captureScreenShot ) return;

	if ( mamaFxTimer < mamaFxSwipeDuration ) return;
	filterOffset--;
	figureIndex();
}

private void figureIndex(){
	int i = filterOffset % fxs.length;
	if( i<0){
		i = fxs.length + i;
	}
//	Log.i( Logs.TAG,"ii:> " + i );
	currentFX = fxs[ i ];
	mamaFxTimer = 0;
	if ( !( currentFX instanceof FxCamera ) ) camHW.ZoomTo( 0 );
}

private void capturingScreenShot(){
	String fileName = new SimpleDateFormat( "yyyy-MM-dd-HH:mm:ss" ).format( new Date() ) + ".jpg";

	Bitmap bitmap = renderToTexture(Model.screenW*2, Model.screenH*2);
//	Bitmap bitmap = readPixelsA( Model.screenW, Model.screenH );
	File file = saveToDisk( bitmap, fileName );
	scanFile( file );
	staticCard( file );
	bitmap.recycle();

//		Uri uri = ( Uri.parse( "file://" + file.toString() ) );
//		Intent intent = new Intent(context, UploadIntentService.class);
//		intent.setData( uri );
//		intent.putExtra("fileName", fileName);
//		context.startService(intent);

	GLES20.glViewport( 0, 0, Model.screenW, Model.screenH );
	camera.onSurfaceChanged( Model.screenW, Model.screenH );

}

public Bitmap readPixelsA( int width, int height ){
	int screenshotSize = width * height;
	ByteBuffer bb = ByteBuffer.allocateDirect( screenshotSize * 4 );
	bb.order( ByteOrder.nativeOrder() );
	GLES20.glReadPixels( 0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, bb );
	int pixelsBuffer[] = new int[ screenshotSize ];
	bb.asIntBuffer().get( pixelsBuffer );
	bb = null;
	Bitmap bitmap = Bitmap.createBitmap( width, height, Bitmap.Config.RGB_565 );
	bitmap.setPixels( pixelsBuffer, screenshotSize - width, -width, 0, 0, width, height );
	pixelsBuffer = null;

	short sBuffer[] = new short[ screenshotSize ];
	ShortBuffer sb = ShortBuffer.wrap( sBuffer );
	bitmap.copyPixelsToBuffer( sb );

	//Making created bitmap (from OpenGL points) compatible with Android bitmap
	for ( int i = 0; i < screenshotSize; ++i ) {
		short v = sBuffer[ i ];
		sBuffer[ i ] = (short) ( ( ( v & 0x1f ) << 11 ) | ( v & 0x7e0 ) | ( ( v & 0xf800 ) >> 11 ) );
	}
	sb.rewind();
	bitmap.copyPixelsFromBuffer( sb );
	return bitmap;
}

private Bitmap readPixelsB( int x, int y, int w, int h ){
	int b[] = new int[ w * ( y + h ) ];
	int bt[] = new int[ w * h ];
	IntBuffer ib = IntBuffer.wrap( b );
	ib.position( 0 );

	GLES20.glReadPixels( x, 0, w, y + h, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib );

	for ( int i = 0, k = 0; i < h; i++, k++ ) {
		for ( int j = 0; j < w; j++ ) {
			int pix = b[ i * w + j ];
			int pb = ( pix >> 16 ) & 0xff;
			int pr = ( pix << 16 ) & 0x00ff0000;
			int pix1 = ( pix & 0xff00ff00 ) | pr | pb;
			bt[ ( h - k - 1 ) * w + j ] = pix1;
		}
	}
	Bitmap sb = Bitmap.createBitmap( bt, w, h, Bitmap.Config.ARGB_8888 );
	return sb;
}

private Bitmap renderToTexture( int width, int height ){
	int[] fb, renderTex; // the framebuffer, the renderbuffer and the texture to render
	IntBuffer texBuffer; //  Buffer to store the texture
	int FLOAT_SIZE_BYTES = 4;
	// create the ints for the framebuffer, depth render buffer and texture
	fb = new int[ 1 ];
	renderTex = new int[ 1 ];
// generate a secondary framebuffer and bind it to OpenGL so we don't mess with existing output.
	GLES20.glGenFramebuffers( 1, fb, 0 );
	GLES20.glGenTextures( 1, renderTex, 0 );
// generate texture
	GLES20.glBindTexture( GLES20.GL_TEXTURE_2D, renderTex[ 0 ] );
	GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
	GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );
	GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
	GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
// create an empty intbuffer first
	int[] buf = new int[ width * height ];
	texBuffer = ByteBuffer.allocateDirect( buf.length * FLOAT_SIZE_BYTES ).order( ByteOrder.nativeOrder() ).asIntBuffer();
// generate the textures
	GLES20.glTexImage2D( GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, width, height, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_SHORT_5_6_5, texBuffer );
// create render buffer
	GLES20.glRenderbufferStorage( GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height );

	GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, fb[ 0 ] );
	// attach a texture to the framebuffer.
	GLES20.glFramebufferTexture2D( GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, renderTex[ 0 ], 0 );
	int status = GLES20.glCheckFramebufferStatus( GLES20.GL_FRAMEBUFFER );
	if ( status != GLES20.GL_FRAMEBUFFER_COMPLETE ) return null;
	// subsequent drawing operations are rendered into the texture

	GLES20.glViewport( 0, 0, width, height );
	camera.onSurfaceChanged( width, height );
	render();
	hashes.onDrawFrame();
	Bitmap bitmap = readPixelsB( 0,0,Model.screenW*2, Model.screenH*2 );
	GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, 0 );
	return bitmap;
}

private File saveToDisk( Bitmap bitmap, String fileName ){
	File dir = new File( Model.PATH_FOR_IMAGES );
	dir.mkdirs();
	File file = new File( Model.PATH_FOR_IMAGES, fileName );
	OutputStream outStream = null;
	try {
		outStream = new FileOutputStream( file );
		bitmap.compress( Bitmap.CompressFormat.JPEG, 100, outStream );
		outStream.flush();
		outStream.close();
	} catch ( FileNotFoundException e ) {
		Log.e( Logs.TAG, "FileNotFoundException: " + e.getMessage() );
	} catch ( IOException e ) {
		Log.e( Logs.TAG, "IOException: " + e.getMessage() );
	}
	return file;
}

private void scanFile( File file ){
	MediaScannerConnection.OnScanCompletedListener onScanComplete = new MediaScannerConnection.OnScanCompletedListener(){
		@Override
		public void onScanCompleted( String path, Uri uri ){
			Log.i( Logs.TAG, "Scanned path: " + path );
			Log.i( Logs.TAG, "Scanned uri: " + uri );
		}
	};
	MediaScannerConnection.scanFile( context, new String[]{ file.toString() }, null, onScanComplete );
}

private void staticCard( File file ){
//	Card card = new Card( context );
//	card.setFootnote( "Shard" );
//	card.setImageLayout( Card.ImageLayout.FULL );
//	card.addImage( Uri.parse( "file://" + file.getPath() ) );
//	TimelineManager.from( context ).insert( card );
}

}

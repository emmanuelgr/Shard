package com.cfccreates.shard;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;

import java.io.IOException;

import gr.emmanuel.embox.util.Logs;

/**
 Created by Emmanuel on 31/12/13.
 */
public class CameraHardware implements Camera.OnZoomChangeListener{

private final int width;
private final int height;
public volatile Camera camera;
public static int currentZoomLevel = 0;
public static int zoomingToZoomLevel = 0;
public static int maxZoomLevel = 0;
public boolean isZooming = false;
private Parameters cameraParams;

public CameraHardware( int width, int height ){
	this.width = width;
	this.height = height;
}

private void init(){
	Log.v( Logs.TAG, "Starting Camera" );
	try {
		camera = Camera.open();
	} catch ( Exception e ) {
		Log.e( Logs.TAG, "Cant open camera....!" );
		e.printStackTrace();
	}

		/*fix for glasss camera zig zags*/
	cameraParams = camera.getParameters();
	cameraParams.setPreviewFpsRange( 30000, 30000 );
	cameraParams.setPreviewSize( width, height );
	Model.cameraMaxW = cameraParams.getPictureSize().width;
	Model.cameraMaxH = cameraParams.getPictureSize().height;
	camera.setParameters( cameraParams );

	maxZoomLevel = cameraParams.getMaxZoom();
	currentZoomLevel = cameraParams.getZoom();

	Log.i( Logs.TAG, String.format( "Camera Size is w: %d h: %d", cameraParams.getPreviewSize().width, cameraParams.getPreviewSize().height ) );
}

public void start( SurfaceTexture surfaceTexture ){
	if ( camera == null ) init();
	try {
		camera.setPreviewTexture( surfaceTexture );
		camera.startPreview();
	} catch ( IOException e ) {
		Log.e( Logs.TAG, "Camera cant start preview..." );
		e.printStackTrace();
	}
}

public void stop(){
	if ( camera == null ) return;
	camera.stopPreview();
	camera.release();
	camera = null;
}

public void ZoomTo( int zoomTo ){
	if ( camera == null ) return;
//////////////////////////////
/////// Hard zoom ////////////
//////////////////////////////
	if ( zoomTo > maxZoomLevel ) zoomTo = maxZoomLevel;
	if ( zoomTo < 0 ) zoomTo = 0;
	if ( isZooming ) {
		camera.stopSmoothZoom();
		isZooming = false;
	}
	if ( zoomTo == zoomingToZoomLevel ) return;
	zoomingToZoomLevel = zoomTo;
	Camera.Parameters parameters = camera.getParameters();
	parameters.setZoom( zoomTo );
	camera.setParameters( parameters );
//////////////////////////////
}

@Override
public void onZoomChange( int zoomValue, boolean stopped, Camera camera ){
	currentZoomLevel = zoomValue;
	isZooming = !stopped;
}
}

package com.cfccreates.shard.fxs;

import android.content.Context;

import com.cfccreates.shard.GLRenderer;
import com.cfccreates.shard.Library;
import com.jme3.math.Vector3f;

import gr.emmanuel.embox.gles2.Object3D;
import gr.emmanuel.embox.gles2.Transform;
import gr.emmanuel.embox.gles2.shaders.Blend;
import gr.emmanuel.embox.gles2.shaders.ShaderCamera;
import com.cfccreates.shard.CameraHardware;

/**
 Created by Emmanuel on 16/01/14.
 */
public class FxCamera extends AFX{
private ShaderCamera shaderCamera;
private CameraHardware cameraHardware;
private final GLRenderer glRenderer;
private Object3D bgCamFeed;
private Object3D viewFinderPlane;

public FxCamera(  Context context, Transform mamaFX, ShaderCamera shaderCamera, CameraHardware cameraHardware, GLRenderer glRenderer ){
	super( context, mamaFX,"Zoom","Play", true, 0.001f, false, false, true, true, true );
	this.shaderCamera = shaderCamera;
	this.cameraHardware = cameraHardware;
	this.glRenderer = glRenderer;

}

@Override
public void onSurfaceCreated(){
	super.onSurfaceCreated();

	viewFinderPlane = new Object3D( 1, library.shaderTextureViewFinder, library.meshSquare, Blend.Modes.NORMAL );
	viewFinderPlane.transform.setScale( new Vector3f( 4.5302773f, 2.548281f, 1f ) );
	transform.addChild( viewFinderPlane.transform );

	bgCamFeed = new Object3D( 1, shaderCamera, library.meshSquare, Blend.Modes.NORMAL );
	bgCamFeed.transform.setScale( new Vector3f( 4.5302773f, 2.548281f, 1f ) );
	transform.addChild( bgCamFeed.transform );
}

@Override
public void onSurfaceChanged( int width, int height ){
}

@Override
public void randomize(){
}

@Override
public void onDrawFrame(){
	super.onDrawFrame();
	int zoom = (int) ( getScroll() * cameraHardware.maxZoomLevel );
	prevValue = CameraHardware.zoomingToZoomLevel;
	cameraHardware.ZoomTo( zoom );
	if ( prevValue != zoom ) playSoundVar();
	bgCamFeed.onDrawFrame();
	if(!glRenderer.captureScreenShot)viewFinderPlane.onDrawFrame();
}
}

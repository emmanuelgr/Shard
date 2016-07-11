package com.cfccreates.shard.fxs;

import android.content.Context;

import com.cfccreates.shard.Library;
import com.jme3.math.Vector3f;

import gr.emmanuel.embox.gles2.Object3D;
import gr.emmanuel.embox.gles2.Transform;
import gr.emmanuel.embox.gles2.shaders.Blend;
import gr.emmanuel.embox.gles2.shaders.ShaderCamera;

/**
 Created by Emmanuel on 16/01/14.
 */
public class FxSettings extends AFX{
private ShaderCamera shaderCamera;
private Object3D bgCamFeed;
private Object3D texture;

public FxSettings(  Context context, Transform mamaFX, ShaderCamera shaderCamera ){
	super( context, mamaFX,"    Settings", "", true, 000, false, false, false, false, false );
	this.shaderCamera = shaderCamera;
}

@Override
public void onSurfaceCreated(){
	super.onSurfaceCreated();

	texture = new Object3D( 1, library.shaderTextureSettings, library.meshSquare, Blend.Modes.NORMAL );
	texture.transform.setScale( new Vector3f( 4.5302773f, 2.548281f, 1f ) );
	transform.addChild( texture.transform );

//	bgCamFeed = new Object3D( 1, shaderCamera, library.meshSquare, Blend.Modes.NORMAL );
//	bgCamFeed.transform.setScale( new Vector3f( 4.5302773f, 2.548281f, 1f ) );
//	transform.addChild( bgCamFeed.transform );
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
//	bgCamFeed.onDrawFrame();
	texture.onDrawFrame();
}
}

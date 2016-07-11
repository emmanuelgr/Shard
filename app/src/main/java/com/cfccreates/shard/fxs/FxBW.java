package com.cfccreates.shard.fxs;

import android.content.Context;

import com.cfccreates.shard.Library;
import com.jme3.math.Vector3f;

import gr.emmanuel.embox.gles2.Object3D;
import gr.emmanuel.embox.gles2.Transform;
import gr.emmanuel.embox.gles2.shaders.Blend;
import gr.emmanuel.embox.gles2.shaders.ShaderCameraDesaturate;
import gr.emmanuel.embox.gles2.shaders.ShaderCameraGreys;

/**
 Created by Emmanuel on 16/01/14.
 */
public class FxBW extends AFX{
private ShaderCameraGreys shaderCameraGreys;
private Object3D bgCamFeed;

public FxBW( Context context, Transform mamaFX, ShaderCameraGreys shaderCameraGreys ){
	super( context, mamaFX, "Black & White", "Play", true, 0.001f, false, false, true, true, true );
	this.shaderCameraGreys = shaderCameraGreys;

}

@Override
public void onSurfaceCreated(){
	super.onSurfaceCreated();

	bgCamFeed = new Object3D( 1, shaderCameraGreys, library.meshSquare, Blend.Modes.NORMAL );
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
//	shaderCameraGreys.steps = getScroll()<0.1f?1f:(1f - getScroll())*0.5f+0.1f;
//	shaderCameraGreys.steps = getScroll() < 0.04f ? 1f : ( 1f - getScroll() ) * 0.46f + 0.04f;
	prevValue = shaderCameraGreys.steps;
	shaderCameraGreys.steps = ( 1f - getScroll() ) < 0.04f ? 1f : getScroll() * 0.46f + 0.04f;
	if ( prevValue != shaderCameraGreys.steps ) playSoundVar();
//	bgCamFeed.desaturation = (float) ( Math.cos( Time.ct * 10.0f )+1f )*0.5f;
//	Log.i( Logs.TAG,"desat: " + bgCamFeed.desaturation );
	bgCamFeed.onDrawFrame();
}


}

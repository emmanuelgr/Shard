package com.cfccreates.shard.fxs;

import android.content.Context;

import com.cfccreates.shard.Library;
import com.jme3.math.Vector3f;

import org.j3d.texture.procedural.PerlinNoiseGenerator;

import java.util.Random;

import gr.emmanuel.embox.gles2.Object3D;
import gr.emmanuel.embox.gles2.Transform;
import gr.emmanuel.embox.gles2.shaders.Blend;
import gr.emmanuel.embox.gles2.shaders.ShaderCameraOffset;

/**
 Created by Emmanuel on 16/01/14.
 */
public class FxOffset extends AFX{
private ShaderCameraOffset shaderCameraOffset;
private Object3D bgCamFeed;
private PerlinNoiseGenerator perlin;
private Random random;
private float rU;
private float gU;
private float bU;
private float rV;
private float gV;
private float bV;
private float rUOffset;
private float gUOffset;
private float bUOffset;
private float rVOffset;
private float gVOffset;
private float bVOffset;

public FxOffset(  Context context, Transform mamaFX, ShaderCameraOffset shaderCameraOffset ){
	super( context, mamaFX,"Offset", "Play", false, 0.001f, true, true, true, true, false);
	this.shaderCameraOffset = shaderCameraOffset;
	perlin = new PerlinNoiseGenerator();
	random = new Random();

	setScroll( 0.5f );
}

@Override
public void onSurfaceCreated(){
	super.onSurfaceCreated();

	bgCamFeed = new Object3D( 1, shaderCameraOffset, library.meshSquare, Blend.Modes.NORMAL );
	bgCamFeed.transform.setScale( new Vector3f( 4.5302773f, 2.548281f, 1f ) );
	transform.addChild( bgCamFeed.transform );
}

@Override
public void onSurfaceChanged( int width, int height ){
}

@Override
public void randomize(){

	rUOffset = ( random.nextFloat() * 2f - 1 );
	gUOffset = ( random.nextFloat() * 2f - 1 );
	bUOffset = ( random.nextFloat() * 2f - 1 );
	rVOffset = ( random.nextFloat() * 2f - 1 );
	gVOffset = ( random.nextFloat() * 2f - 1 );
	bVOffset = ( random.nextFloat() * 2f - 1 );

	bgCamFeed.color[ 0 ] = random.nextFloat() * 0.4f + 0.6f;
	bgCamFeed.color[ 1 ] = random.nextFloat() * 0.4f + 0.6f;
	bgCamFeed.color[ 2 ] = random.nextFloat() * 0.4f + 0.6f;
}

@Override
public void onDrawFrame(){
	super.onDrawFrame();

	rU = perlin.noise1( getScroll() * 1f ) * rUOffset;
	gU = perlin.noise1( getScroll() * 2f ) * gUOffset;
	bU = perlin.noise1( getScroll() * 3f ) * bUOffset;
	rV = perlin.noise1( getScroll() * 4f ) * rVOffset;
	gV = perlin.noise1( getScroll() * 5f ) * gVOffset;
	bV = perlin.noise1( getScroll() * 6f ) * bVOffset;

	shaderCameraOffset.offsetRU = rU;
	shaderCameraOffset.offsetGU = gU;
	shaderCameraOffset.offsetBU = bU;
	shaderCameraOffset.offsetRV = rV;
	shaderCameraOffset.offsetGV = gV;
	shaderCameraOffset.offsetBV = bV;
	bgCamFeed.onDrawFrame();



	if ( prevValue != getScroll() ) playSoundVar();
	prevValue = getScroll();
}


}

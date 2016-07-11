package com.cfccreates.shard.fxs;


import android.content.Context;

import gr.emmanuel.embox.util.CompassManager;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import org.j3d.texture.procedural.PerlinNoiseGenerator;

import java.util.Random;

import gr.emmanuel.embox.gles2.IOnEachObject3D;
import gr.emmanuel.embox.gles2.Object3D;
import gr.emmanuel.embox.gles2.RenderObject3DClone;
import gr.emmanuel.embox.gles2.Transform;
import gr.emmanuel.embox.gles2.shaders.Blend;
import gr.emmanuel.embox.gles2.shaders.ShaderCamera;

/**
 Created by Emmanuel on 16/01/14.
 */
public class FxA extends AFX{
private ShaderCamera shaderCamera;
private RenderObject3DClone object3DClonesA1;
private RenderObject3DClone object3DClonesA2;
private RenderObject3DClone object3DClonesA3;
private RenderObject3DClone object3DClonesA4;
private PerlinNoiseGenerator perlin;
private Random random;
private Object3D bgCamFeed;

public FxA( Context context, Transform mamaFX, ShaderCamera shaderCamera ){
	super( context, mamaFX, "JJ2", "Play",false, 0.0001f, true, true, true, true, false );
	this.shaderCamera = shaderCamera;
	perlin = new PerlinNoiseGenerator();
	random = new Random();
	setScroll( 0.5f );


}


@Override
public void onSurfaceCreated(){
	super.onSurfaceCreated();

	object3DClonesA1 = new RenderObject3DClone( 02, library.shaderTextureSquareX1, library.meshSquare, Blend.Modes.SCREEN );
	object3DClonesA2 = new RenderObject3DClone( 04, library.shaderTextureSquare2, library.meshSquare, Blend.Modes.SCREEN );
	object3DClonesA3 = new RenderObject3DClone( 03, library.shaderTextureSquareX1, library.meshSquare, Blend.Modes.SCREEN );
	object3DClonesA4 = new RenderObject3DClone( 10, library.shaderTextureSquareX1, library.meshSquare, Blend.Modes.SCREEN );

	transform.addChild( object3DClonesA1.transform );
	transform.addChild( object3DClonesA2.transform );
	transform.addChild( object3DClonesA3.transform );
	transform.addChild( object3DClonesA4.transform );

	bgCamFeed = new Object3D( 1, shaderCamera, library.meshSquare, Blend.Modes.NORMAL );
	bgCamFeed.transform.setScale( new Vector3f( 4.5302773f, 2.548281f, 1f ) );
	transform.addChild( bgCamFeed.transform );

	randomize();
}

@Override
public void onSurfaceChanged( int width, int height ){
//	bgCamFeed.transform.setScale( Camera.getMain().matchCamFrustrum( bgCamFeed.transform.getPosGlobal() ) );// nailling in values above
}

@Override
public void randomize(){

	object3DClonesA1.randomize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			object3D.color[ 0 ] = 66f / 255f;
			object3D.color[ 1 ] = 70f / 255f;
			object3D.color[ 2 ] = 167f / 255f;

			object3D.transform.setPositionX( random.nextFloat() * 6f - 3f );
			object3D.transform.setPositionY( random.nextFloat() * 4f - 2f );

			object3D.transform.setScaleX( random.nextFloat() * 15f + 0.8f );
			object3D.transform.setScaleY( random.nextFloat() * 4f + 0.15f );
		}
	} );


	object3DClonesA2.randomize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			object3D.color[ 0 ] = ( 255f - 66f ) / 255f;
			object3D.color[ 1 ] = ( 255f - 70f ) / 255f;
			object3D.color[ 2 ] = ( 255f - 167f ) / 255f;

			Vector2f vec = new Vector2f( random.nextFloat() * 2f - 1f, random.nextFloat() * 2f - 1f );
			vec.normalize();
			vec.mult( 2 );
			object3D.transform.setPositionOffsetX( vec.x );
			object3D.transform.setPositionOffsetY( vec.y );

			float rectRatio = random.nextFloat() * 2f + 2f;
			float rectSize = random.nextFloat() * 3f + 1f;
			object3D.transform.setScaleX( rectSize * rectRatio );
			object3D.transform.setScaleY( rectSize );
		}
	} );


	object3DClonesA3.randomize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			object3D.color[ 0 ] = 66f / 255f;
			object3D.color[ 1 ] = 70f / 255f;
			object3D.color[ 2 ] = 167f / 255f;

			Vector2f vec = new Vector2f( random.nextFloat() * 2f - 1f, random.nextFloat() * 2f - 1f );
			vec.normalize();
			vec.mult( 2 );
			object3D.transform.setPositionOffsetX( vec.x );
			object3D.transform.setPositionOffsetY( vec.y );

			object3D.transform.setScaleX( random.nextFloat() * 4f + 9f );
			object3D.transform.setScaleY( random.nextFloat() * 0.15f + 0.05f );
		}
	} );

	object3DClonesA4.randomize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){

			object3D.color[ 0 ] = 255f / 255f;
			object3D.color[ 1 ] = 30f / 255f;
			object3D.color[ 2 ] = 109f / 255f;

			Vector2f vec = new Vector2f( random.nextFloat() * 2f - 1f, random.nextFloat() * 2f - 1f );
			vec.normalize();
			vec.mult( 2 );
			object3D.transform.setPositionOffsetX( vec.x );
			object3D.transform.setPositionOffsetY( vec.y );

			object3D.transform.setScaleX( random.nextFloat() * 0.09f + 0.03f );
			object3D.transform.setScaleY( random.nextFloat() * 0.09f + 0.03f );
		}
	} );
}

@Override
public void onDrawFrame(){
	super.onDrawFrame();
	bgCamFeed.onDrawFrame();

	object3DClonesA1.onDrawFrame( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			object3D.transform.setPositionX( perlin.noise1( object3D.ratio * getScroll() * 10f ) * 10f );
			object3D.transform.setPositionY( perlin.noise1( object3D.ratio * getScroll() * 10f ) * 10f );
			object3D.transform.setRotationZ( CompassManager.getInstance(context).getRoll() * 180f );
		}
	} );

	object3DClonesA2.onDrawFrame( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			object3D.transform.setPositionX( perlin.noise1( getScroll() * 10f + object3D.ratio * 10f ) * 5f );
			object3D.transform.setPositionY( perlin.noise1( getScroll() * 10f + object3D.ratio * 20f ) * 5f );
			object3D.transform.setRotationZ( CompassManager.getInstance(context).getRoll() * 180f );
		}
	} );


	object3DClonesA3.onDrawFrame( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			object3D.transform.setPositionX( perlin.noise1( getScroll() * 10f + object3D.ratio * 30f ) * 5f );
			object3D.transform.setPositionY( perlin.noise1( getScroll() * 10f + object3D.ratio * 40f ) * 5f );
			object3D.transform.setRotationZ( CompassManager.getInstance(context).getRoll() * 180f );
		}
	} );

	object3DClonesA4.onDrawFrame( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			object3D.transform.setPositionX( perlin.noise1( getScroll() * 10f + object3D.ratio * 50f ) * 5f );
			object3D.transform.setPositionY( perlin.noise1( getScroll() * 10f + object3D.ratio * 60f ) * 5f );
		}
	} );


	if ( prevValue != getScroll() ) playSoundVar();
	prevValue = getScroll();
}

}

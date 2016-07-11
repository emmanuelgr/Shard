package com.cfccreates.shard.fxs;

import android.content.Context;

import gr.emmanuel.embox.util.CompassManager;

import com.jme3.math.Vector3f;

import org.j3d.texture.procedural.PerlinNoiseGenerator;

import java.util.Random;

import gr.emmanuel.embox.gles2.IOnEachObject3D;
import gr.emmanuel.embox.gles2.RenderObject3DClone;
import gr.emmanuel.embox.gles2.Object3D;
import gr.emmanuel.embox.gles2.Transform;
import gr.emmanuel.embox.gles2.shaders.Blend;
import gr.emmanuel.embox.gles2.shaders.ShaderCamera;

/**
 Created by Emmanuel on 16/01/14.
 */
public class FxB extends AFX{
private PerlinNoiseGenerator perlin;
private ShaderCamera shaderCamera;
private RenderObject3DClone object3DClonesA1;
private RenderObject3DClone object3DClonesA2;
private RenderObject3DClone object3DClonesA3;
private RenderObject3DClone object3DClonesA4;
private RenderObject3DClone object3DClonesA5;
private Random random;
private Object3D bgCamFeed;

public FxB( Context context, Transform mamaFX, ShaderCamera shaderCamera ){
	super( context, mamaFX, "JJ1", "Play", false, 0.0001f, true, true, true, true, false );
	this.shaderCamera = shaderCamera;
	perlin = new PerlinNoiseGenerator();
	random = new Random();

}


@Override
public void onSurfaceCreated(){
	super.onSurfaceCreated();

	object3DClonesA1 = new RenderObject3DClone( 4, library.shaderTextureSquare1, library.meshTrapezoid, Blend.Modes.INVERSE );
	object3DClonesA2 = new RenderObject3DClone( 9, library.shaderTextureSquareX2, library.meshTrapezoid, Blend.Modes.SCREEN );
	object3DClonesA3 = new RenderObject3DClone( 20, library.shaderTextureHotSpot1, library.meshSquare, Blend.Modes.SCREEN );
	object3DClonesA4 = new RenderObject3DClone( 2, library.shaderTextureSquare2, library.meshSquare, Blend.Modes.SCREEN );
	object3DClonesA5 = new RenderObject3DClone( 1, library.shaderTextureHotSpot1, library.meshSquare, Blend.Modes.SCREEN );

	transform.addChild( object3DClonesA1.transform );
	transform.addChild( object3DClonesA2.transform );
	transform.addChild( object3DClonesA3.transform );
	transform.addChild( object3DClonesA4.transform );
	transform.addChild( object3DClonesA5.transform );

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

			object3D.transform.setPivotY( random.nextFloat() * -0.1f );

			object3D.transform.setPositionOffsetX( 1.9f );
			object3D.transform.setPositionOffsetY( 1.0f );

			object3D.transform.setRotationOffsetZ( random.nextFloat() * 15f + ( object3D.ratio * 360f ) );

			object3D.transform.setScaleX( random.nextFloat() * 0.5f + 1.8f );
			object3D.transform.setScaleY( random.nextFloat() * 1.5f + 4.5f );
		}
	} );

	object3DClonesA2.randomize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){

			object3D.transform.setPivotY( random.nextFloat() * -1.0f );

			object3D.transform.setPositionOffsetX( 1.9f );
			object3D.transform.setPositionOffsetY( 1.0f );

			object3D.transform.setRotationOffsetZ( random.nextFloat() * 35f + object3D.ratio * 360f );

			object3D.color[ 0 ] = random.nextFloat() * 0.1f + 007f / 255f;
			object3D.color[ 1 ] = random.nextFloat() * 0.1f + 012f / 255f;
			object3D.color[ 2 ] = random.nextFloat() * 0.1f + 199f / 255f;

			object3D.transform.setScaleX( random.nextFloat() * 0.5f + 1.8f );
			object3D.transform.setScaleY( random.nextFloat() * 2.5f + 2.5f );
		}
	} );


	object3DClonesA3.randomize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){

			object3D.transform.setPivotY( random.nextFloat() * -2.5f );

			object3D.transform.setPositionOffsetX( 1.9f );
			object3D.transform.setPositionOffsetY( 1.0f );

			object3D.transform.setRotationOffsetZ( random.nextFloat() * object3D.ratio * 90f - 45f );

			object3D.color[ 0 ] = random.nextFloat() * 0.1f + 148f / 255f;
			object3D.color[ 1 ] = random.nextFloat() * 0.1f + 098f / 255f;
			object3D.color[ 2 ] = random.nextFloat() * 0.1f + 185f / 255f;

			object3D.transform.setScale( random.nextFloat() * 2.5f + 0.01f );
		}
	} );

	object3DClonesA4.randomize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){

			object3D.transform.setPositionOffsetX( 1.9f );
			object3D.transform.setPositionOffsetY( 1.0f );

			object3D.transform.setScaleX( random.nextFloat() * 3.0f + 7f );
			object3D.transform.setScaleY( random.nextFloat() * 0.2f + 0.05f );
		}
	} );

	object3DClonesA5.randomize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){

			object3D.transform.setPositionOffsetX( 1.4f );
			object3D.transform.setPositionOffsetY( 0.7f );

			object3D.transform.setScaleX( random.nextFloat() * 3.2f + 5f );
			object3D.transform.setScaleY( random.nextFloat() * 3.2f + 5f );
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
			//			object3D.transform.setPositionX(  perlin.noise1( object3D.ratio * getScroll() * 5f ) * 10f  );
			//			object3D.transform.setPositionY(  perlin.noise1( object3D.ratio * Input.getInstance().getY() * 5f ) * 10f  );
			//			object3D.transform.setRotationz = Input.getInstance().getRoll() * 180f  );
			//			object3D.transform.setRotationz = perlin.noise1( object3D.ratio * getScroll() ) * 3600f  );
			object3D.transform.setRotationZ( CompassManager.getInstance( context ).getRoll() * 180f + getScroll() * 2600f );
		}
	} );

	object3DClonesA2.onDrawFrame( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			//			object3D.transform.setPositionX(  perlin.noise1( getScroll() + object3D.ratio ) * 8f  );
			//			object3D.transform.setPositionY(  perlin.noise1( Input.getInstance().getY() + object3D.ratio ) * 8f  );

			//			object3D.transform.setRotationz = perlin.noise1( object3D.ratio * getScroll() ) * 1800f  );
			object3D.transform.setRotationZ( CompassManager.getInstance( context ).getRoll() * -180f );
		}
	} );
	//
	//
	object3DClonesA3.onDrawFrame( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			object3D.transform.setPositionX( perlin.noise1( getScroll() * 30f + object3D.ratio * 50f ) * 3f );
			object3D.transform.setPositionY( perlin.noise1( getScroll() * 30f + object3D.ratio * 60f ) * 3f );
		}
	} );
	object3DClonesA4.onDrawFrame( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			object3D.transform.setPositionX( perlin.noise1( object3D.ratio * getScroll() * 10f ) * 1.0f - 0.5f );
			object3D.transform.setPositionY( perlin.noise1( object3D.ratio * getScroll() * 20f ) * 1.0f - 0.5f );
			object3D.transform.setRotationZ( CompassManager.getInstance(context).getRoll() * 180f );
		}
	} );

	object3DClonesA5.onDrawFrame( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			//			object3D.transform.setPositionX(  perlin.noise1( object3D.ratio * getScroll() * 5f ) * 1.0f - 0.5f  );
			object3D.transform.setRotationZ( CompassManager.getInstance( context ).getRoll() * 180f );
		}
	} );


	if ( prevValue != getScroll() ) playSoundVar();
	prevValue = getScroll();
}
}

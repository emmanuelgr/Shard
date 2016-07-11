package com.cfccreates.shard.fxs;

import android.content.Context;

import com.cfccreates.shard.Library;
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
public class FxC extends AFX{
public static final float PIVOT_X = 10f;
public static final float PIVOT_Y = 1.2f;
public Transform papa = new Transform();
private PerlinNoiseGenerator perlin;
private ShaderCamera shaderCamera;
private RenderObject3DClone object3DClonesA1;
private RenderObject3DClone object3DClonesA2;
private RenderObject3DClone object3DClonesA3;
//private RenderObject3DClone object3DClonesA4;
private RenderObject3DClone object3DClonesA5;
private Random random;
private Object3D bgCamFeed;

public FxC( Context context, Transform mamaFX, ShaderCamera shaderCamera ){
	super( context, mamaFX,"Martian Rain","Variation...", false, 0.0001f, true, true, true, true, false );
	this.shaderCamera = shaderCamera;
	perlin = new PerlinNoiseGenerator();
	random = new Random();

}


@Override
public void onSurfaceCreated(){
	super.onSurfaceCreated();

	object3DClonesA1 = new RenderObject3DClone( 45, library.shaderTextureSpot3, library.meshSquare, Blend.Modes.NORMAL );
	object3DClonesA2 = new RenderObject3DClone( 60, library.shaderTextureSpot1, library.meshSquare, Blend.Modes.SCREEN );
	object3DClonesA3 = new RenderObject3DClone( 20, library.shaderTextureSpot3, library.meshSquare, Blend.Modes.NORMAL );
//	object3DClonesA4 = new RenderObject3DClone( 2, library.shaderTextureSquare2, library.meshSquare,Blend.Modes.SCREEN );
	object3DClonesA5 = new RenderObject3DClone( 1, library.shaderTextureSpot1, library.meshSquare, Blend.Modes.SCREEN );

	transform.addChild( papa );
	papa.setPositionOffsetX( PIVOT_X );
	papa.setPositionOffsetY( PIVOT_Y );
	papa.setRotationOffsetX( -40f );

	papa.addChild( object3DClonesA1.transform );
	papa.addChild( object3DClonesA2.transform );
	papa.addChild( object3DClonesA3.transform );
//	papa.addChild( object3DClonesA4.transform );
	transform.addChild( object3DClonesA5.transform );

	bgCamFeed = new Object3D( 1, shaderCamera, library.meshSquare, Blend.Modes.NORMAL );
	bgCamFeed.transform.setScale( new Vector3f( 4.5302773f, 2.548281f, 1f ) );
	transform.addChild( bgCamFeed.transform );

	object3DClonesA1.initialize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){

			Transform papa = object3D.transform.getParent();
			papa.removeChild( object3D.transform );
			Transform inbetweenPapa = new Transform();
			inbetweenPapa.addChild( object3D.transform );
			papa.addChild( inbetweenPapa );
			inbetweenPapa.setRotationX( 40f );


		}
	} );

	object3DClonesA2.initialize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			papa.removeChild( object3D.transform );
			Transform inbetweenPapa = new Transform();
			papa.addChild( inbetweenPapa );
			inbetweenPapa.addChild( object3D.transform );
			object3D.transform.setPivotY( -9.0f  );
			object3D.transform.setRotationOffsetZ( random.nextFloat() * 5f + object3D.ratio * 360f );
		}
	} );
	
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



			object3D.transform.setPivotY( random.nextFloat() * 0f - 1.4f );


//			int color = Resources.getColor(R.color.AlienGreen);
//			int red = Color.red(color);
//			int gre = Color.green(color);
//			int blu = Color.blue(color);

			object3D.color[ 0 ] = random.nextFloat() * 0.1f + 218f / 255f;
			object3D.color[ 1 ] = random.nextFloat() * 0.1f + 228f / 255f;
			object3D.color[ 2 ] = random.nextFloat() * 0.1f + 157f / 255f;

			object3D.transform.setScaleOffsetX( random.nextFloat() * 0.2f + 0.2f );
			object3D.transform.setScaleOffsetY( random.nextFloat() * 0.0f + 6.3f );

			object3D.transform.setRotationOffsetZ( random.nextFloat() * 3f + ( object3D.ratio * 360f ) );
		}
	} );

	object3DClonesA2.randomize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
//
//			object3D.transform.setPivotY(  - 11.0f );
//
//
//			object3D.transform.getParent().setRotationZ( random.nextFloat() * 5f + object3D.ratio * 360f );
//
			object3D.color[ 0 ] = random.nextFloat() * 0.1f + 181f / 255f;
			object3D.color[ 1 ] = random.nextFloat() * 0.1f + 217f / 255f;
			object3D.color[ 2 ] = random.nextFloat() * 0.1f + 143f / 255f;
//
//			object3D.transform.getParent().setScale( random.nextFloat() * 0.8f + 0.2f );
		}
	} );


	object3DClonesA3.randomize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){

			object3D.transform.setPivotY( -3.3f );

//
			object3D.transform.setRotationOffsetZ( random.nextFloat() * 35f + object3D.ratio * 360f );
//
			object3D.color[ 0 ] = random.nextFloat() * 0.1f + 046f / 255f;
			object3D.color[ 1 ] = random.nextFloat() * 0.1f + 055f / 255f;
			object3D.color[ 2 ] = random.nextFloat() * 0.1f + 125f / 255f;
//
			object3D.transform.setScaleOffset( random.nextFloat() * 0f + 3.5f );
		}
	} );

	object3DClonesA5.randomize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){

			object3D.transform.setPositionX(1.9f);
			object3D.transform.setPositionY(1f);
			object3D.color[ 0 ] = random.nextFloat() * 0.1f + 119f / 255f;
			object3D.color[ 1 ] = random.nextFloat() * 0.1f + 171f / 255f;
			object3D.color[ 2 ] = random.nextFloat() * 0.1f + 135f / 255f;

			object3D.transform.setScaleOffsetX( random.nextFloat() * 3.5f + 6.0f );
			object3D.transform.setScaleOffsetY( random.nextFloat() * 3.5f + 6.0f );
		}
	} );
}

@Override
public void onDrawFrame(){
	super.onDrawFrame();

	bgCamFeed.onDrawFrame();

//	papa.setRotationY( getScroll() * 360f );

	object3DClonesA1.onDrawFrame( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			object3D.transform.setRotationZ( getScroll() * -180f );
		}
	} );

	object3DClonesA2.onDrawFrame( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			object3D.transform.setRotationZ( getScroll() * -360f );
		}
	} );


	object3DClonesA3.onDrawFrame( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
//			object3D.transform.setRotationZ( perlin.noise1( object3D.ratio * getScroll() ) * -540 );
			object3D.transform.setRotationZ( getScroll() * -540 );
		}
	} );

	object3DClonesA5.onDrawFrame( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			object3D.transform.setRotationZ( getScroll() * -90f );
		}
	} );
}


}

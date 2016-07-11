package com.cfccreates.shard.fxs;

import android.content.Context;

import com.cfccreates.shard.Library;
import com.jme3.math.Vector3f;

import gr.emmanuel.embox.gles2.IOnEachObject3D;
import gr.emmanuel.embox.gles2.Object3D;
import gr.emmanuel.embox.gles2.RenderObject3DClone;
import gr.emmanuel.embox.gles2.Transform;
import gr.emmanuel.embox.gles2.shaders.Blend;
import gr.emmanuel.embox.gles2.shaders.ShaderCamera;

/**
 Created by Emmanuel on 16/01/14.
 */
public class FxCalido extends AFX{
private RenderObject3DClone triangleClones;
public Transform papa = new Transform();
private ShaderCamera shaderCamera;
private Object3D bgCamFeed;

public FxCalido(  Context context, Transform mamaFX, ShaderCamera shaderCamera ){
	super( context, mamaFX, "Kaleidos", "Play",true, 0.001f, false, true, true, true, true );
	this.shaderCamera = shaderCamera;
}


@Override
public void onSurfaceCreated(){
	super.onSurfaceCreated();

	triangleClones = new RenderObject3DClone( 6 * 15, shaderCamera, library.meshTriangle, Blend.Modes.NORMAL_PRE_MULT );
	papa.addChild( triangleClones.transform );
	transform.addChild( papa );
//	papa.setScale(4f);

	bgCamFeed = new Object3D( 1, shaderCamera, library.meshSquare, Blend.Modes.NORMAL );
	bgCamFeed.transform.setScale( new Vector3f( 4.5302773f, 2.548281f, 1f ) );
	transform.addChild( bgCamFeed.transform );

	Object3D[] trigs = triangleClones.getObject3Ds();
	Object3D aTrig;
	float scl = 3f;
	int maxRows = 3;
	float xOff = 0.5f;
	float yOff = 0.433013f;
	float w = trigs.length / ( maxRows * 6 ) * 0.25f * scl;
	float h = maxRows * yOff * 2 * 0.25f * scl;
	for ( int i = 0; i < trigs.length; i++ ) {
		aTrig = trigs[ i ];
		int r = i % 6;
//		Log.i( Logs.TAG,">: " + r );
		aTrig.transform.setRotationZ( 60f * r );
		if ( i % 2 == 0 ) {
			aTrig.transform.setScale( new Vector3f( scl, scl, scl ) );
		} else {
			aTrig.transform.setScale( new Vector3f( -scl, scl, scl ) );
		}

		int x = i / ( maxRows * 6 );
		int y = i / 6 % maxRows;
		int o = x % 2;
//		Log.i( Logs.TAG, "x: " + x + " y: " + y + " o: " + o+ " w: " + w+ " h: " + h );
		aTrig.transform.setPositionX( x * xOff * 1.5f * scl - w - xOff * 2f + ( xOff * 0.5f ) );
		aTrig.transform.setPositionY( y * yOff * 2f * -scl - ( yOff * o * -scl ) + h + ( yOff * -0.75f ) );
//		aTrig.transform.setPositionX( x * xOff * 1.5f *  scl                 );
//		aTrig.transform.setPositionY( y * yOff *   2f * -scl - ( yOff *  o )-yOff*scl*0.5f);
	}
	randomize();
}

@Override
public void onSurfaceChanged( int width, int height ){
}

@Override
public void randomize(){
	triangleClones.randomize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){

		}
	} );

}

@Override
public void onDrawFrame(){
	super.onDrawFrame();
	bgCamFeed.onDrawFrame();
//	transform.setRotationZ( Input.getInstance().getRoll() * -180f );
//	papa.setRotationX( Input.getInstance().getPitch() * -90);
//	transform.setScale( transform.getScale().x + Input.getInstance().getScroll()/100f );
	prevValue = papa.getScale().x;
	papa.setScale( ( 1 - getScroll() ) * 1.9f + 0.44f );
	if(prevValue!= papa.getScale().x) playSoundVar();
	triangleClones.onDrawFrame( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){

		}
	} );
}


}

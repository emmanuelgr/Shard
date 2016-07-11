package com.cfccreates.shard.fxs;

import android.content.Context;
import android.graphics.Color;

import com.cfccreates.shard.Library;

import org.j3d.texture.procedural.PerlinNoiseGenerator;

import java.util.Random;

import gr.emmanuel.embox.gles2.IOnEachObject3D;
import gr.emmanuel.embox.gles2.RenderObject3DClone;
import gr.emmanuel.embox.gles2.Object3D;
import gr.emmanuel.embox.gles2.Transform;
import gr.emmanuel.embox.gles2.shaders.Blend;

/**
 Created by Emmanuel on 17/01/14.
 */
public class FxCubes extends AFX{
public Transform transform = new Transform();
private final PerlinNoiseGenerator perlin;
private final Random random;
private RenderObject3DClone object3DClonesA1;

public FxCubes( Context context, Transform mamaFX ){
	super( context, mamaFX, "Cubes", "cubes", false, 00, false, false, false, false, false );
	perlin = new PerlinNoiseGenerator();
	random = new Random();
}

@Override
public void onSurfaceCreated(){
	super.onSurfaceCreated();

	object3DClonesA1 = new RenderObject3DClone( 50, library.shaderConstant, library.meshCube, Blend.Modes.NORMAL );

	transform.addChild( object3DClonesA1.transform );
}

@Override
public void randomize(){

	object3DClonesA1.randomize( new IOnEachObject3D(){
		@Override
		public void run( Object3D object3D ){
			object3D.transform.setPositionX( random.nextFloat() * 50f - 25f );
			object3D.transform.setPositionY( random.nextFloat() * 50f - 25f );
			object3D.transform.setPositionZ( random.nextFloat() * 50f - 25f );
//		object3D.transform.setPositionX( (float) ( Math.cos( object3D.ratio * Math.PI * 2f ) ) * 1f );
//		object3D.transform.setPositionZ( (float) ( Math.sin( object3D.ratio * Math.PI * 2f ) ) * 1f );
//		object3D.transform.setScale( 0.1f );

			float[] anArray = new float[ 3 ];
			anArray[ 0 ] = 360f * object3D.ratio;
			anArray[ 1 ] = 1;
			anArray[ 2 ] = 1;
			object3D.color[ 0 ] = Color.red( Color.HSVToColor( anArray ) ) / 255f;
			object3D.color[ 1 ] = Color.green( Color.HSVToColor( anArray ) ) / 255f;
			object3D.color[ 2 ] = Color.blue( Color.HSVToColor( anArray ) ) / 255f;
//			object3D.color[ 0 ] = random.nextFloat();
//			object3D.color[ 1 ] = random.nextFloat();
//			object3D.color[ 2 ] = random.nextFloat();
		}
	} );
}

@Override
public void onSurfaceChanged( int width, int height ){

}

@Override
public void onDrawFrame(){
	super.onDrawFrame();

}
}

package com.cfccreates.shard.fxs;

import com.cfccreates.shard.Library;
import com.cfccreates.shard.Model;
import com.jme3.math.Vector3f;

import gr.emmanuel.embox.gles2.Object3D;
import gr.emmanuel.embox.gles2.Transform;
import gr.emmanuel.embox.gles2.shaders.Blend;

/**
 Created by Emmanuel on 21/02/14.
 */
public class BlackMasks{
private final Library library;
private final int total;
private Transform parent;
private final float[] blackColor = new float[]{ 0, 0, 0, 1 };
private final Vector3f scl = new Vector3f( Model.fxCardsDistance - 4.5302773f, 2.548281f, 1f );
private Object3D[] object3Ds;

public BlackMasks( Library library, int total, Transform parent ){
	this.library = library;
	this.total = total;
	this.parent = parent;
	init();
}

private void init(){
	object3Ds = new Object3D[ total ];
	for ( int i = 0; i < total; i++ ) {
		object3Ds[ i ] = new Object3D( 0, library.shaderConstant, library.meshSquare, Blend.Modes.NORMAL );
		object3Ds[ i ].color = blackColor;
		object3Ds[ i ].transform.setPositionX( Model.fxCardsDistance * ( i + 0.5f ) );
		parent.addChild( object3Ds[ i ].transform );
		object3Ds[ i ].transform.setScale( scl );
	}

}
public void render(){
	for ( int i = 0; i < total; i++ ) {
		object3Ds[ i ].onDrawFrame();;
	}
}
}

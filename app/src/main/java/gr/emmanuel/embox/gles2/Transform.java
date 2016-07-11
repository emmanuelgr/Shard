package gr.emmanuel.embox.gles2;

import android.opengl.Matrix;

import com.jme3.math.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transform{

private Vector3f pivot;
private Vector3f positionOffset;
private Vector3f rotationOffset;
private Vector3f scaleOffset;
private Vector3f position;
private Vector3f rotation;
private Vector3f scale;
private float[] localMatrix;
private float[] tmpMatrix;

public boolean isDirty(){
	return dirty;
}

private boolean dirty;
public void invalidate(){
	this.dirty = true;
	for ( Transform child : mChildren ) {
		child.invalidate();
	}
}

private float[] matrixGlobal = new float[ 16 ];

private Transform mParent = null;
private final List<Transform> mChildren = new ArrayList<Transform>();

public Transform(){

//	pivottransform = new Transform();

	localMatrix = new float[ 16 ];
	tmpMatrix = new float[ 16 ];

	pivot = new Vector3f( 0, 0, 0 );
	scale = new Vector3f( 1f, 1f, 1f );
	rotation = new Vector3f( 0, 0, 0 );
	position = new Vector3f( 0, 0, 0 );
	positionOffset = new Vector3f( 0, 0, 0 );
	rotationOffset = new Vector3f( 0, 0, 0 );
	scaleOffset = new Vector3f( 1f, 1f, 1f );

	dirty = true;
}

public void reset(){
	pivot.x = pivot.y = pivot.z = 1f;
	scale.x = scale.y = scale.z = 1f;
	rotation.x = rotation.y = rotation.z = 0;
	position.x = position.y = position.z = 0;
	positionOffset.x = positionOffset.y = positionOffset.z = 0;
	rotationOffset.x = rotationOffset.y = rotationOffset.z = 0;
	scaleOffset.x = scaleOffset.y = scaleOffset.z = 1f;
//	quat = Quaternion.ZERO;
}

/**
 Update the transform
 @return its Global Matrix
 */
public float[] draw(){
	if ( mParent != null && mParent.isDirty() ) {
		mParent.validate();
	}
	if ( dirty ) {
		validate();
	}
	return matrixGlobal;
}

public void setMatrix( float[] matrix ){
	matrixGlobal = matrix;
}

private void validate(){

	Matrix.setIdentityM( localMatrix, 0 );
	Matrix.setIdentityM( tmpMatrix, 0 );

	Matrix.translateM( localMatrix, 0, position.x + positionOffset.x, position.y + positionOffset.y, position.z + positionOffset.z );
	Matrix.rotateM( localMatrix, 0, rotation.x + rotationOffset.x, 1f, 0, 0 );
	Matrix.rotateM( localMatrix, 0, rotation.y + rotationOffset.y, 0, 1f, 0 );
	Matrix.rotateM( localMatrix, 0, rotation.z + rotationOffset.z, 0, 0, 1f );
	Matrix.scaleM( localMatrix, 0, scale.x * scaleOffset.x, scale.y * scaleOffset.y, scale.z * scaleOffset.z );
	Matrix.translateM( localMatrix, 0, pivot.x, pivot.y, pivot.z );

//	System.arraycopy( localMatrix, 0, tmpMatrix, 0, 16 );
//	Matrix.setIdentityM( localMatrix, 0 );
//	Matrix.multiplyMM( localMatrix, 0, tmpMatrix, 0, pivottransform.draw(), 0 );

	Matrix.setIdentityM( matrixGlobal, 0 );
	if ( mParent != null ) {
		Matrix.multiplyMM( matrixGlobal, 0, mParent.draw(), 0, localMatrix, 0 );
	} else {
		System.arraycopy( localMatrix, 0, matrixGlobal, 0, 16 );
	}

	dirty = false;
}

public int getChildCount(){
	return mChildren.size();
}

public Transform getChildAt( int index ){
	if ( index < 0 || index >= mChildren.size() ) return null;
	return mChildren.get( index );
}

public void addChild( Transform child ){
	if(child.mParent!=null) child.mParent.removeChild( child );
	child.mParent = this;
	mChildren.add( child );
	child.invalidate();
}

public void removeChild( Transform child ){
	if ( Arrays.asList( mChildren ).indexOf( child ) == -1 ) return;
	mChildren.remove( child );
	child.mParent = null;
}

public void removeChild( int index ){
	Transform child = mChildren.remove( index );
	child.mParent = null;
}

public void removeChildren(){
	for ( Transform child : mChildren ) {
		child.mParent = null;
	}
	mChildren.clear();
}

///////////////////////////////////////////////////
///////////////////////////////////////////////////
///          Getters and Setters       ////////////
///////////////////////////////////////////////////
///////////////////////////////////////////////////
public Vector3f getPosGlobal(){
	Vector3f v = new Vector3f();
	v.x = draw()[ 12 ];
	v.y = draw()[ 13 ];
	v.z = draw()[ 14 ];
//	Log.i( Logs.TAG, "v>: " + v);
	return v;
}

//public Quaternion getQuat(){
//	return quat;
//}
//
//public void setQuat( Quaternion quat ){
//	this.quat = quat;
//	invalidate();
//}

public Vector3f getPivot(){
	return pivot;
}

public float getPivotX(){
	return pivot.x;
}

public float getPivotY(){
	return pivot.y;
}

public float getPivotZ(){
	return pivot.z;
}

public void setPivot( Vector3f pivot ){
	this.pivot = pivot;
	invalidate();
}

public void setPivotX( float x ){
	this.pivot.x = x;
	invalidate();
}

public void setPivotY( float y ){
	this.pivot.y = y;
	invalidate();
}

public void setPivotZ( float z ){
	this.pivot.z = z;
	invalidate();
}

public Vector3f getPositionOffset(){
	return positionOffset;
}

public float getPositionOffsetX(){
	return positionOffset.x;
}

public float getPositionOffsetY(){
	return positionOffset.y;
}

public float getPositionOffsetZ(){
	return positionOffset.z;
}

public void setPositionOffset( Vector3f positionOffset ){
	this.positionOffset = positionOffset;
	invalidate();
}

public void setPositionOffsetX( float x ){
	this.positionOffset.x = x;
	invalidate();
}

public void setPositionOffsetY( float y ){
	this.positionOffset.y = y;
	invalidate();
}

public void setPositionOffsetZ( float z ){
	this.positionOffset.z = z;
	invalidate();
}

public Vector3f getRotationOffset(){
	return rotationOffset;
}

public void setRotationOffset( Vector3f rotationOffset ){
	this.rotationOffset = rotationOffset;
	invalidate();
}

public void setRotationOffsetX( float x ){
	this.rotationOffset.x = x;
	invalidate();
}

public void setRotationOffsetY( float y ){
	this.rotationOffset.y = y;
	invalidate();
}

public void setRotationOffsetZ( float z ){
	this.rotationOffset.z = z;
	invalidate();
}

public Vector3f getScaleOffset(){
	return scaleOffset;
}

public float getScaleOffsetX(){
	return scaleOffset.x;
}

public float getScaleOffsetY(){
	return scaleOffset.y;
}

public float getScaleOffsetZ(){
	return scaleOffset.z;
}

public void setScaleOffset( Vector3f offsetScl ){
	this.scaleOffset = offsetScl;
	invalidate();
}

public void setScaleOffset( float scl ){
	this.scaleOffset.x = scl;
	this.scaleOffset.y = scl;
	this.scaleOffset.z = scl;
	invalidate();
}

public void setScaleOffsetX( float x ){
	this.scaleOffset.x = x;
	invalidate();
}

public void setScaleOffsetY( float y ){
	this.scaleOffset.y = y;
	invalidate();
}

public void setScaleOffsetZ( float z ){
	this.scaleOffset.z = z;
	invalidate();
}

public Vector3f getPosition(){
	return position;
}

public float getPositionX(){
	return position.x;
}

public float getPositionY(){
	return position.y;
}

public float getPositionZ(){
	return position.z;
}

public void setPosition( Vector3f position ){
	this.position = position;
	invalidate();
}

public void setPositionX( float x ){
	this.position.x = x;
	invalidate();
}

public void setPositionY( float y ){
	this.position.y = y;
	invalidate();
}

public void setPositionZ( float z ){
	this.position.z = z;
	invalidate();
}

public Vector3f getRotation(){
	return rotation;
}

public float getRotationX(){
	return rotation.x;
}

public float getRotationY(){
	return rotation.y;
}

public float getRotationZ(){
	return rotation.z;
}

public void setRotation( Vector3f rotation ){
	this.rotation = rotation;
	invalidate();
}

public void setRotationX( float x ){
	this.rotation.x = x;
	invalidate();
}

public void setRotationY( float y ){
	this.rotation.y = y;
	invalidate();
}

public void setRotationZ( float z ){
	this.rotation.z = z;
	invalidate();
}

public Vector3f getScale(){
	return scale;
}

public float getScaleX(){
	return scale.x;
}

public float getScaleY(){
	return scale.y;
}

public float getScaleZ(){
	return scale.z;
}

public void setScale( Vector3f scale ){
	this.scale = scale;
	invalidate();
}

public void setScale( float scale ){
	this.scale.x = scale;
	this.scale.y = scale;
	this.scale.z = scale;
	invalidate();
}

public void setScaleX( float x ){
	this.scale.x = x;
	invalidate();
}

public void setScaleY( float y ){
	this.scale.y = y;
	invalidate();
}

public void setScaleZ( float z ){
	this.scale.z = z;
	invalidate();
}

public Transform getParent(){
	return mParent;
}


}
package gr.emmanuel.embox.gles2;

import android.opengl.Matrix;
import android.util.Log;

import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;

import gr.emmanuel.embox.util.Logs;

public class Camera implements IRenderer{
private static Camera main;
public final Transform transformEye;
public final Vector3f lookAt = new Vector3f( 0, 0, 0 );
public final Vector3f up = new Vector3f( 0, 1f, 0 );

//private final Matrix4f mProjectionMatrix = new Matrix4f();
//private final Matrix4f mViewMatrix = new Matrix4f();
//private final Matrix4f mViewProjectionMatrix = new Matrix4f();
private final float[] mProjectionMatrix = new float[ 16 ];
private final float[] mViewMatrix = new float[ 16 ];
private final float[] mViewProjectionMatrix = new float[ 16 ];
public float fov = 65;
public float ratio=1f;
public float near = 0.01f;
public float far = 1000;
public float top;
public float bottom;
public float left;
public float right;


public float[] getVPMatrix(){
	return mViewProjectionMatrix;
	//	return mViewProjectionMatrix.getFloats();
}

public Camera(){
	main = this;
	transformEye = new Transform();
	transformEye.setPositionZ( 6f );
}

public static Camera getMain(){
	if ( main == null ) {
		Log.e( Logs.TAG, "Init a camera first....");
		return null;
	} else {
		return main;
	}
}

@Override
public void onSurfaceCreated(){

}

public void onSurfaceChanged( int width, int height ){
	ratio = (float) width / height;
}

@Override
public void onDrawFrame( ){

	top = (float) ( Math.tan( Math.toRadians( fov/2f ) ) * near );
	bottom = -top;
	left = ratio * bottom;
	right = ratio * top;
	// this projection matrix is applied to object coordinates in the onDrawFrame() method
	Matrix.setIdentityM( mProjectionMatrix, 0 );
	Matrix.frustumM( mProjectionMatrix, 0, left, right, bottom, top, near, far );
	//	mProjectionMatrix.loadIdentity();
	//		Matrix.frustumM( mProjectionMatrix.getFloats(), 0, left, right, bottom, top, near, far );

	////////////////////////////////

	// Set the camera position (View matrix)
//	Matrix.setIdentityM( mViewMatrix, 0 );
//	Matrix.setLookAtM( mViewMatrix, 0, transformEye.getPositionX(), transformEye.getPositionY(), transformEye.getPositionZ(), lookAt.x, lookAt.y, lookAt.z, up.x, up.y, up.z );
//	transformEye.setMatrix(mViewMatrix);// TODO fix hack of forcing orientation ... maybe no transorm field and create min transform implementation just rot and pos

	//	mViewMatrix.loadIdentity();
	//	Matrix.setLookAtM( mViewMatrix.getFloats(), 0, eye.x, eye.y, eye.z, lookAt.x, lookAt.y, lookAt.z, up.x, up.y, up.z );

	// Calculate the projection and view transformation
	Matrix.setIdentityM( mViewProjectionMatrix, 0 );
//	Matrix.multiplyMM( mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0 );
	Matrix.multiplyMM( mViewProjectionMatrix, 0, mProjectionMatrix, 0, transformEye.draw(), 0 );
	//	mViewProjectionMatrix.set( mProjectionMatrix ).mult( mViewMatrix );
}

public Vector3f matchCamFrustrum( Vector3f planePosition ){
	float dist = planePosition.clone().subtract( transformEye.getPosGlobal() ).length();
//	Log.i( Logs.TAG, "dist: " + dist );
	double angle = Math.toRadians( fov ) / 2f;
	float s = (float) ( Math.tan( angle ) * dist * 2f );
	return new Vector3f( s * ratio, s, 1f );
}

}

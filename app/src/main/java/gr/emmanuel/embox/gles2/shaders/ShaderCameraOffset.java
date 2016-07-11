package gr.emmanuel.embox.gles2.shaders;

import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import gr.emmanuel.embox.gles2.Camera;
import gr.emmanuel.embox.gles2.Object3D;
import gr.emmanuel.embox.gles2.mesh.Mesh;

/**
 * Created by Emmanuel on 30/12/13.
 */
public class ShaderCameraOffset implements IShaderDraw{

private static final String vertexShaderCode =
// This matrix member variable provides a hook to manipulate
// the coordinates of the objects that use this vertex shader
// The matrix must be included as a modifier of gl_Position.
// Note that the uMVPMatrix factor *must be first* in order
// for the matrix multiplication product to be correct.
				"attribute vec4 vPosition;\n" +
								"attribute vec2 aUV;\n" +
								"uniform mat4 uMMatrix;\n" +
								"uniform mat4 uVPMatrix;\n" +
								"uniform  float uRU;\n" +
								"uniform  float uGU;\n" +
								"uniform  float uBU;\n" +
								"uniform  float uRV;\n" +
								"uniform  float uGV;\n" +
								"uniform  float uBV;\n" +
								"varying vec2 vUV;\n" +
								"void main() {\n" +
								"	vUV = aUV;\n" +
								" gl_Position =uVPMatrix * uMMatrix * vPosition;\n" +
								"}\n";

private static final String fragmentShaderCode =
				"#extension GL_OES_EGL_image_external : require\n" +
								"precision mediump float;\n" +
								"uniform  vec4 uColor;\n" +
								"uniform  float uRU;\n" +
								"uniform  float uGU;\n" +
								"uniform  float uBU;\n" +
								"uniform  float uRV;\n" +
								"uniform  float uGV;\n" +
								"uniform  float uBV;\n" +
								"uniform samplerExternalOES sTexture;\n" +
								"varying vec2 vUV;\n" +
								"void main() {\n" +
								"vec4 color;\n" +

								"float color0 = texture2D( sTexture, vUV - vec2( uRU, uRV ) ).x;\n" +
								"float color1 = texture2D( sTexture, vUV - vec2( uGU, uGV ) ).y;\n" +
								"float color2 = texture2D( sTexture, vUV - vec2( uBU, uBV ) ).z;\n" +
//								"color = vec4(color0,color1,color2, 1.0);\n" +
								"color = vec4(color0,color1,color2, 1.0)*uColor;\n" +

								"gl_FragColor = color;\n" +
								"}";

private static int vertexShader;
private static int fragmentShader;
private SurfaceTexture mSurfaceTexture;
private int texture;

private int mPositionHandle;
private int mColorHandle;
private int mRUHandle;
private int mGUHandle;
private int mBUHandle;
private int mRVHandle;
private int mGVHandle;
private int mBVHandle;

private int mMMatrixHandle;
private int mVPMatrixHandle;
private int mUVsHandle;
private int mProgram;
public float offsetRU;
public float offsetGU;
public float offsetBU;
public float offsetRV;
public float offsetGV;
public float offsetBV;

public ShaderCameraOffset( SurfaceTexture mSurfaceTexture, int texture ){
	this.mSurfaceTexture = mSurfaceTexture;
	this.texture = texture;
	// prepare shaders and OpenGL program
	vertexShader = Shaders.createVertexShader( vertexShaderCode );
	fragmentShader = Shaders.createFragmentShader( fragmentShaderCode );
	mProgram = Shaders.program( vertexShader, fragmentShader );
	Shaders.validateProgram(mProgram);

}

@Override
public void drawPre( Object3D object3D ){
//	float[] mtx = new float[ 16 ];
	mSurfaceTexture.updateTexImage();
//	mSurfaceTexture.getTransformMatrix( mtx );

	// Add program to OpenGL environment
	GLES20.glUseProgram( mProgram );

	GLES20.glActiveTexture( GLES20.GL_TEXTURE0 );
	GLES20.glBindTexture( GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture );

	mPositionHandle = GLES20.glGetAttribLocation( mProgram, "vPosition" );
	GLES20.glEnableVertexAttribArray( mPositionHandle );
	GLES20.glVertexAttribPointer( mPositionHandle, Mesh.DIM_PER_VERTEX, GLES20.GL_FLOAT, false, Mesh.STRIDE_VERTEX, object3D.mesh.getVertexBuffer() );

	mUVsHandle = GLES20.glGetAttribLocation( mProgram, "aUV" );
	GLES20.glEnableVertexAttribArray( mUVsHandle );
	GLES20.glVertexAttribPointer( mUVsHandle, Mesh.DIM_PER_UV, GLES20.GL_FLOAT, false, Mesh.STRIDE_UV, object3D.mesh.getUVSBuffer() );

	// get handle to fragment shader's uColor member
	mColorHandle = GLES20.glGetUniformLocation( mProgram, "uColor" );

	// get handle to fragment shader's uR member
	mRUHandle = GLES20.glGetUniformLocation( mProgram, "uRU" );
	mRVHandle = GLES20.glGetUniformLocation( mProgram, "uRV" );

	// get handle to fragment shader's uR member
	mBUHandle = GLES20.glGetUniformLocation( mProgram, "uBU" );
	mBVHandle = GLES20.glGetUniformLocation( mProgram, "uBV" );

	// get handle to fragment shader's uR member
	mGUHandle = GLES20.glGetUniformLocation( mProgram, "uGU" );
	mGVHandle = GLES20.glGetUniformLocation( mProgram, "uGV" );

	// get handle to views's and projections matrix
	mVPMatrixHandle = GLES20.glGetUniformLocation( mProgram, "uVPMatrix" );

	// get handle to shape's transformation matrix
	mMMatrixHandle = GLES20.glGetUniformLocation( mProgram, "uMMatrix" );
}


@Override
public void draw( Object3D object3D ){

	// Apply the color, projection view and  transformation
	GLES20.glUniform4fv( mColorHandle, 1, object3D.color, 0 );
	GLES20.glUniform1f( mRUHandle, offsetRU );
	GLES20.glUniform1f( mGUHandle, offsetGU );
	GLES20.glUniform1f( mBUHandle, offsetBU );
	GLES20.glUniform1f( mRVHandle, offsetRV );
	GLES20.glUniform1f( mGVHandle, offsetGV );
	GLES20.glUniform1f( mBVHandle, offsetBV );
	GLES20.glUniformMatrix4fv( mMMatrixHandle, 1, false, object3D.transform.draw(), 0 );
	GLES20.glUniformMatrix4fv( mVPMatrixHandle, 1, false, Camera.getMain().getVPMatrix(), 0 );
	// Draw the square
	GLES20.glDrawElements( GLES20.GL_TRIANGLES, object3D.mesh.getIndicesLength(), GLES20.GL_UNSIGNED_SHORT, object3D.mesh.getIndicesBuffer() );

}

@Override
public void drawPost( Object3D object3D ){
	// Disable program.  Not strictly necessary.
//		GLES20.glUseProgram(mProgram);
	// Disable vertex array
	GLES20.glDisableVertexAttribArray( mPositionHandle );
	// Disable uvs array
	GLES20.glDisableVertexAttribArray( mUVsHandle );
}


}

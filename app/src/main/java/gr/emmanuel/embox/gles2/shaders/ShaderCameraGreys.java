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
public class ShaderCameraGreys implements IShaderDraw{

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
								"uniform  float stepsReciprocal;\n" +
								"varying vec2 vUV;\n" +
								"void main() {\n" +
								"	vUV = aUV;\n" +
								" gl_Position =uVPMatrix * uMMatrix * vPosition;\n" +
								"}\n";

private static final String fragmentShaderCode =
				"#extension GL_OES_EGL_image_external : require\n" +
								"precision mediump float;\n" +
								"uniform  vec4 uColor;\n" +
								"uniform  float stepsReciprocal;\n" +
								"uniform samplerExternalOES sTexture;\n" +
								"varying vec2 vUV;\n" +
								"void main() {\n" +
								"  vec4 color = texture2D( sTexture, vUV ) * uColor;\n" +
								"  float lum = (0.299 * color.r) + (0.587 * color.g) + (0.114 * color.b);\n" +
								"  if(stepsReciprocal==0.0){\n" +
								"    color = vec4( lum,lum,lum, uColor.a );\n" +
								"  } else {\n" +
								"    float greys = floor( lum / stepsReciprocal + 0.5 ) * stepsReciprocal;\n" +
								"    color = vec4( greys,greys,greys, uColor.a );\n" +
								"  }\n" +
								"  gl_FragColor = color;\n" +
								"}";

private static int vertexShader;
private static int fragmentShader;
private SurfaceTexture mSurfaceTexture;
private int texture;

private int positionHandle;
private int colorHandle;
private int stepsReciprocalHandle;
private int modelMatrixHandle;
private int viewPMatrixHandle;
private int uVsHandle;
private int program;
public float steps;

public ShaderCameraGreys( SurfaceTexture mSurfaceTexture, int texture ){
	this.mSurfaceTexture = mSurfaceTexture;
	this.texture = texture;
	// prepare shaders and OpenGL program
	vertexShader = Shaders.createVertexShader( vertexShaderCode );
	fragmentShader = Shaders.createFragmentShader( fragmentShaderCode );
	program = Shaders.program( vertexShader, fragmentShader );
	Shaders.validateProgram( program );

}


@Override
public void drawPre( Object3D object3D ){

//	float[] mtx = new float[ 16 ];
	mSurfaceTexture.updateTexImage();
//	mSurfaceTexture.getTransformMatrix( mtx );

	// Add program to OpenGL environment
	GLES20.glUseProgram( program );

	GLES20.glActiveTexture( GLES20.GL_TEXTURE0 );
	GLES20.glBindTexture( GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture );

	positionHandle = GLES20.glGetAttribLocation( program, "vPosition" );
	GLES20.glEnableVertexAttribArray( positionHandle );
	GLES20.glVertexAttribPointer( positionHandle, Mesh.DIM_PER_VERTEX, GLES20.GL_FLOAT, false, Mesh.STRIDE_VERTEX, object3D.mesh.getVertexBuffer() );

	uVsHandle = GLES20.glGetAttribLocation( program, "aUV" );
	GLES20.glEnableVertexAttribArray( uVsHandle );
	GLES20.glVertexAttribPointer( uVsHandle, Mesh.DIM_PER_UV, GLES20.GL_FLOAT, false, Mesh.STRIDE_UV, object3D.mesh.getUVSBuffer() );

	// get handle to fragment shader's uColor member
	colorHandle = GLES20.glGetUniformLocation( program, "uColor" );

	// get handle to fragment shader's stepsReciprocal member
	stepsReciprocalHandle = GLES20.glGetUniformLocation( program, "stepsReciprocal" );

	// get handle to views's and projections matrix
	viewPMatrixHandle = GLES20.glGetUniformLocation( program, "uVPMatrix" );

	// get handle to shape's transformation matrix
	modelMatrixHandle = GLES20.glGetUniformLocation( program, "uMMatrix" );
}


@Override
public void draw( Object3D object3D ){

	// Apply the color, projection view and  transformation
	GLES20.glUniform4fv( colorHandle, 1, object3D.color, 0 );
	GLES20.glUniform1f( stepsReciprocalHandle, steps );
	GLES20.glUniformMatrix4fv( modelMatrixHandle, 1, false, object3D.transform.draw(), 0 );
	GLES20.glUniformMatrix4fv( viewPMatrixHandle, 1, false, Camera.getMain().getVPMatrix(), 0 );
	// Draw the square
	GLES20.glDrawElements( GLES20.GL_TRIANGLES, object3D.mesh.getIndicesLength(), GLES20.GL_UNSIGNED_SHORT, object3D.mesh.getIndicesBuffer() );

}

@Override
public void drawPost( Object3D object3D ){
	// Disable program.  Not strictly necessary.
//		GLES20.glUseProgram(program);
	// Disable vertex array
	GLES20.glDisableVertexAttribArray( positionHandle );
	// Disable uvs array
	GLES20.glDisableVertexAttribArray( uVsHandle );
}


}

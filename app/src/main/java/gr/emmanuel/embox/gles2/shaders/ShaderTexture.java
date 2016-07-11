package gr.emmanuel.embox.gles2.shaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import java.io.InputStream;

import gr.emmanuel.embox.gles2.Camera;
import gr.emmanuel.embox.gles2.Object3D;

/**
 * Created by Emmanuel on 03/01/14.
 */
public class ShaderTexture implements IShaderDraw{
private static final String vertexShaderCode =
	"uniform mat4 uMMatrix;\n" +
	"uniform mat4 uVPMatrix;\n" +
	"attribute vec4 vPosition;" +
	"attribute vec2 TexCoordIn;" +
	"varying vec2 TexCoordOut;" +
	"void main() {" +
	"  gl_Position = uVPMatrix * uMMatrix * vPosition;" +
	"  TexCoordOut = TexCoordIn;" +
	"}";

private static final String fragmentShaderCode =
	"precision mediump float;" +
	"uniform vec4 uColor;" +
	"uniform sampler2D TexCoordIn;" +
	"varying vec2 TexCoordOut;" +
	"void main() {" +
	" gl_FragColor = texture2D(TexCoordIn, TexCoordOut) * uColor;" +
	"}";


private int texture;
private final int mProgram;
private int mPositionHandle;
private int mMMatrixHandle;
private int mVPMatrixHandle;

static final int COORDS_PER_VERTEX = 4;
static final int COORDS_PER_TEXTURE = 2;

private final int vertexStride = COORDS_PER_VERTEX * 4;
public static int textureStride = COORDS_PER_TEXTURE * 4;

public ShaderTexture( int useTexture, Context context ){

	InputStream imagestream = context.getResources().openRawResource( useTexture );
	Bitmap bitmap = null;
	try {
		bitmap = BitmapFactory.decodeStream( imagestream );
		imagestream.close();
		imagestream = null;
	} catch ( Exception e ) {
	}
	//
	texture = Textures.createTexture( bitmap );


	// prepare shaders and OpenGL program
	int vertexShader = Shaders.createVertexShader( vertexShaderCode );
	int fragmentShader = Shaders.createFragmentShader( fragmentShaderCode );
	mProgram = Shaders.program( vertexShader, fragmentShader );
}

@Override
public void drawPre( Object3D object3D ){
	GLES20.glUseProgram( mProgram );

	mPositionHandle = GLES20.glGetAttribLocation( mProgram, "vPosition" );
	GLES20.glEnableVertexAttribArray( mPositionHandle );
	GLES20.glVertexAttribPointer( mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, object3D.mesh.getVertexBuffer() );

	int vsTextureCoord = GLES20.glGetAttribLocation( mProgram, "TexCoordIn" );
	GLES20.glEnableVertexAttribArray( vsTextureCoord );
	GLES20.glVertexAttribPointer( vsTextureCoord, COORDS_PER_TEXTURE, GLES20.GL_FLOAT, false, textureStride, object3D.mesh.getUVSBuffer() );

	// get handle to views's and projections matrix
	mVPMatrixHandle = GLES20.glGetUniformLocation( mProgram, "uVPMatrix" );

	// get handle to shape's transformation matrix
	mMMatrixHandle = GLES20.glGetUniformLocation( mProgram, "uMMatrix" );
}

@Override
public void draw( Object3D object3D ){


	GLES20.glActiveTexture( GLES20.GL_TEXTURE0 );
	GLES20.glBindTexture( GLES20.GL_TEXTURE_2D, texture );

	GLES20.glUniformMatrix4fv( mMMatrixHandle, 1, false, object3D.transform.draw(), 0 );
	GLES20.glUniformMatrix4fv( mVPMatrixHandle, 1, false, Camera.getMain().getVPMatrix(), 0 );

	int fsTexture = GLES20.glGetUniformLocation( mProgram, "TexCoordOut" );
	int fsColor = GLES20.glGetUniformLocation( mProgram, "uColor" );
	GLES20.glUniform1i( fsTexture, 0 );

	// Apply the color, projection view and  transformation
	//        GLES20.glUniform4fv( fsColor, 1, object3D.color, 0 );
	GLES20.glUniform4f( fsColor, object3D.color[ 0 ], object3D.color[ 1 ], object3D.color[ 2 ], object3D.color[ 3 ] );

	GLES20.glDrawElements( GLES20.GL_TRIANGLES, object3D.mesh.getIndicesLength(), GLES20.GL_UNSIGNED_SHORT, object3D.mesh.getIndicesBuffer() );

}

@Override
public void drawPost( Object3D object3D ){
	GLES20.glDisableVertexAttribArray( mPositionHandle );

}
}

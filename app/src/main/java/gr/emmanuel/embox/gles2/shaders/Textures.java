package gr.emmanuel.embox.gles2.shaders;

import android.graphics.Bitmap;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 Created by Emmanuel on 21/02/14.
 */
public class Textures{
public static int createTexture( Bitmap bitmap ){
	int[] textures = new int[ 1 ];
	// generate one texture pointer and bind it as an external texture.
	// The first parameter is the number of texture names that you need generated.
	// When it comes time to bind the textures to a set of vertices, you will call them out of OpenGL ES by name.
	// Here, you are only loading one texture; therefore, you need only one texture name generated.
	// The second parameter is the array of int that you created to hold the number for each texture.
	// Again, there is only one value in this array right now.
	// Last parameter holds the pivot for the pointer into the array. Because your array is 0-based, the pivot is 0.
	GLES20.glGenTextures( 1, textures, 0 );
	// binds the texture into OpenGL ES.
	GLES20.glBindTexture( GLES20.GL_TEXTURE_2D, textures[ 0 ] );
	//  Filtering
	//		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
	GLES20.glTexParameterf( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
	GLES20.glTexParameterf( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
	// Clamp to edge.
	//		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
	//		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

	// associate the bitmap input stream that you created with the number 1 texture. The bitmap stream is then recycled.
	GLUtils.texImage2D( GLES20.GL_TEXTURE_2D, 0, bitmap, 0 );
	// Check
	if ( textures[ 0 ] == 0 ) throw new RuntimeException( "Error loading texture." );
	bitmap.recycle();
	return textures[ 0 ];
}
public static int createCameraTexture(){
	int[] textures = new int[ 1 ];
	// generate one texture pointer and bind it as an external texture.
	GLES20.glGenTextures( 1, textures, 0 );
	GLES20.glBindTexture( GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[ 0 ] );
	// No mip-mapping with camera source.
	GLES20.glTexParameterf( GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
	GLES20.glTexParameterf( GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
	// Clamp to edge is only option.
	GLES20.glTexParameteri( GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
	GLES20.glTexParameteri( GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );
	return textures[ 0 ];
}
}

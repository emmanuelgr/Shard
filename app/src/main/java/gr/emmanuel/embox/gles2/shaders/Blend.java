package gr.emmanuel.embox.gles2.shaders;

import android.opengl.GLES20;

/**
 Created by Emmanuel on 03/02/14.
 */
public class Blend{
public static enum Modes{
	NORMAL,
	NORMAL_PRE_MULT,
	SCREEN,
	INVERSE,
	MULTIPLY,
	LINEAR_DODGE
}

public static void setBlendMode( Modes blendMode ){
	switch ( blendMode ) {
	case SCREEN:
		GLES20.glBlendFunc( GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_COLOR );
		break;
	case INVERSE:
		GLES20.glBlendFunc( GLES20.GL_ONE_MINUS_DST_COLOR, GLES20.GL_ONE_MINUS_SRC_COLOR );
		break;
	case NORMAL:
		GLES20.glBlendFunc( GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA );
		break;
	case MULTIPLY:
		GLES20.glBlendFunc( GLES20.GL_DST_COLOR, GLES20.GL_ONE_MINUS_SRC_ALPHA );
		break;
	case LINEAR_DODGE:
		GLES20.glBlendFunc( GLES20.GL_ONE, GLES20.GL_ONE );
		break;
	case NORMAL_PRE_MULT:
		GLES20.glBlendFunc( GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA );
		break;
	}
}
}

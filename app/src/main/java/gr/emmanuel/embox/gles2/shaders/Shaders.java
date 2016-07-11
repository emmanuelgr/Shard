package gr.emmanuel.embox.gles2.shaders;

import android.opengl.GLES20;
import android.util.Log;

import gr.emmanuel.embox.util.Logs;

public class Shaders {

	public static int createVertexShader(String shaderCode) {
		return create(GLES20.GL_VERTEX_SHADER, shaderCode);
	}

	public static int createFragmentShader(String shaderCode) {
		return create(GLES20.GL_FRAGMENT_SHADER, shaderCode);
	}

	private static int create(int type, String shaderCode) {
		final int shader = GLES20.glCreateShader(type);
		if (shader == 0) {
			if (Logs.isLoggingGl) {
				Log.w(Logs.TAG, "Could not create new shader.");
			}
			return 0;
		}
		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		// Get the compilation status.
		final int[] compileStatus = new int[1];
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
		if (Logs.isLoggingGl) {
			Log.v(Logs.TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:" + GLES20.glGetShaderInfoLog(shader));
		}

		// Verify the compile status.
		if (compileStatus[0] == 0) {
			GLES20.glDeleteShader(shader);
			if (Logs.isLoggingGl) {
				Log.w(Logs.TAG, "Compilation of shader failed.");
			}
			return 0;
		}

		return shader;
	}

	public static int program(int vertexShader, int fragmentShader) {
		// create empty OpenGL Program
		final int program = GLES20.glCreateProgram();
		if (program == 0) {
			if (Logs.isLoggingGl) {
				Log.w(Logs.TAG, "Could not create new program");
			}
			return 0;
		}

		//add the shaders to program
		GLES20.glAttachShader(program, vertexShader);
		GLES20.glAttachShader(program, fragmentShader);
		// create OpenGL program executables
		GLES20.glLinkProgram(program);

		// Get the link status.
		final int[] linkStatus = new int[1];
		GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
		if (Logs.isLoggingGl) {
			Log.v(Logs.TAG, "Results of linking program:\n" + GLES20.glGetProgramInfoLog(program));
		}
		if (linkStatus[0] == 0) {
			GLES20.glDeleteProgram(program);
			if (Logs.isLoggingGl) {
				Log.w(Logs.TAG, "Linking of program failed.");
			}
			return 0;
		}
		return program;
	}

	/**
	 * Utility method for debugging OpenGL calls. Provide the name of the call
	 * just after making it:
	 * <pre>
	 * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
	 * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
	 * If the operation is not successful, the check throws an error.
	 *
	 * @param glOperation - Name of the OpenGL call to check.
	 */
	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(Logs.TAG, glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

	/**
	 * Validates an OpenGL program. Should only be called when developing the
	 * application.
	 */
	public static boolean validateProgram(int programObjectId) {
		GLES20.glValidateProgram(programObjectId);
		final int[] validateStatus = new int[1];
		GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
		Log.v(Logs.TAG, "Results of validating program: " + validateStatus[0] + "\nLog:" + GLES20.glGetProgramInfoLog(programObjectId));

		return validateStatus[0] != 0;
	}
}

package gr.emmanuel.embox.gles2.shaders;

import android.opengl.GLES20;

import gr.emmanuel.embox.gles2.Camera;
import gr.emmanuel.embox.gles2.Object3D;
import gr.emmanuel.embox.gles2.mesh.Mesh;

/**
 * Created by Emmanuel on 30/12/13.
 */
public class ShaderConstant implements IShaderDraw {


	private static final String vertexShaderCode =
	// This matrix member variable provides a hook to manipulate
	// the coordinates of the objects that use this vertex shader
	// The matrix must be included as a modifier of gl_Position.
	// Note that the uMVPMatrix factor *must be first* in order
	// for the matrix multiplication product to be correct.
	"attribute vec4 vPosition;" +
	"uniform mat4 uMMatrix;" +
	"uniform mat4 uVPMatrix;" +
	"void main() {" +
	"  gl_Position = uVPMatrix * uMMatrix * vPosition;" +
	"}";

	private static final String fragmentShaderCode =
	"precision mediump float;" +
	"uniform vec4 vColor;" +
	"void main() {" +
	"  gl_FragColor = vColor;" +
	"}";

	private static int vertexShader;
	private static int fragmentShader;

	private int mPositionHandle;
	private int mColorHandle;

	private int mMMatrixHandle;
	private int mVPMatrixHandle;
	public int mProgram;

	public ShaderConstant() {
		// prepare shaders and OpenGL program
		vertexShader = Shaders.createVertexShader(vertexShaderCode);
		fragmentShader = Shaders.createFragmentShader(fragmentShaderCode);
		mProgram = Shaders.program(vertexShader, fragmentShader);
	}

	@Override
	public void drawPre(Object3D object3D) {
		// Add program to OpenGL environment
		GLES20.glUseProgram(mProgram);
		// get handle to vertex shader's vPosition member
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		// Enable a handle to the triangle vertices
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		// get handle to fragment shader's vColor member
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
		// get handle to views's and projections matrix
		mVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uVPMatrix");
		// get handle to shape's transformation matrix
		mMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");

		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, Mesh.DIM_PER_VERTEX, GLES20.GL_FLOAT, false, Mesh.STRIDE_VERTEX, object3D.mesh.getVertexBuffer());
	}

	@Override
	public void draw(Object3D object3D) {
		// Set color for drawing the triangle
		GLES20.glUniform4fv(mColorHandle, 1, object3D.color, 0);
//		// Concatenate its transformation
//		float[] matrixTmp = new float[16];
//		Matrix.multiplyMM(matrixTmp, 0, mvpMatrix, 0, transformMatrix, 0);
//		// Apply the projection and view transformation
//		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, matrixTmp, 0);

		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mMMatrixHandle, 1, false, object3D.transform.draw(), 0);
		GLES20.glUniformMatrix4fv(mVPMatrixHandle, 1, false, Camera.getMain().getVPMatrix(), 0);

		// Draw the mesh
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, object3D.mesh.getIndicesLength(), GLES20.GL_UNSIGNED_SHORT, object3D.mesh.getIndicesBuffer());

	}

	@Override
	public void drawPost(Object3D object3D) {
		// Disable program.  Not strictly necessary.
		GLES20.glUseProgram(mProgram);
		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}


}

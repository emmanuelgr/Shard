package gr.emmanuel.embox.gles2.mesh;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import gr.emmanuel.embox.gles2.ByteBuffers;

/**
 * Created by Emmanuel on 30/12/13.
 */
public abstract class Mesh {
	public static int DIM_PER_VERTEX = 4;
	public static int DIM_PER_UV = 2;
	public static int STRIDE_VERTEX = DIM_PER_VERTEX * 4;    // 4 bytes per vertex
	public static int STRIDE_UV = DIM_PER_UV * 4;    // 4 bytes per vertex

	private FloatBuffer vertexBuffer;
	private ShortBuffer indicesBuffer;
	private FloatBuffer uvBuffer;
	private FloatBuffer colorBuffer;
	// number of coordinates per vertex in this array
	protected int indicesLength;

	public Mesh() {
		indicesLength = indices().length;
		// Create buffers
		vertexBuffer = ByteBuffers.FloatBuffer(vertices());
		indicesBuffer = ByteBuffers.ShortBuffer(indices());
		if (uvs() != null) uvBuffer = ByteBuffers.FloatBuffer(uvs());
		if (colors() != null) colorBuffer = ByteBuffers.FloatBuffer(colors());
	}

	abstract protected float[] vertices();
	abstract protected short[] indices();
	abstract protected float[] uvs();
	abstract protected float[] colors();

	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}

	public ShortBuffer getIndicesBuffer() {
		return indicesBuffer;
	}

	public FloatBuffer getUVSBuffer() {
		return uvBuffer;
	}

	public FloatBuffer getColorBuffer() {
		return colorBuffer;
	}

	public int getIndicesLength() {
		return indicesLength;
	}

}

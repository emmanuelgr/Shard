package gr.emmanuel.embox.gles2.mesh;

public class Cube extends Mesh {

	@Override
	protected float[] vertices() {
		return new float[]{
						// Front face
						-0.5f, -0.5f, 0.5f, 1,
						0.5f, -0.5f, 0.5f, 1,
						0.5f, 0.5f, 0.5f, 1,
						-0.5f, 0.5f, 0.5f, 1,

						// Back face
						-0.5f, -0.5f, -0.5f, 1,
						-0.5f, 0.5f, -0.5f, 1,
						0.5f, 0.5f, -0.5f, 1,
						0.5f, -0.5f, -0.5f, 1,

						// Top face
						-0.5f, 0.5f, -0.5f, 1,
						-0.5f, 0.5f, 0.5f, 1,
						0.5f, 0.5f, 0.5f, 1,
						0.5f, 0.5f, -0.5f, 1,

						// Bottom face
						-0.5f, -0.5f, -0.5f, 1,
						0.5f, -0.5f, -0.5f, 1,
						0.5f, -0.5f, 0.5f, 1,
						-0.5f, -0.5f, 0.5f, 1,

						// Right face
						0.5f, -0.5f, -0.5f, 1,
						0.5f, 0.5f, -0.5f, 1,
						0.5f, 0.5f, 0.5f, 1,
						0.5f, -0.5f, 0.5f, 1,

						// Left face
						-0.5f, -0.5f, -0.5f, 1,
						-0.5f, -0.5f, 0.5f, 1,
						-0.5f, 0.5f, 0.5f, 1,
						-0.5f, 0.5f, -0.5f, 1
		};
	}

	@Override
	protected short[] indices() {
		return new short[]{
						0, 1, 2, 0, 2, 3,    // front
						4, 5, 6, 4, 6, 7,    // back
						8, 9, 10, 8, 10, 11,   // top
						12, 13, 14, 12, 14, 15,   // bottom
						16, 17, 18, 16, 18, 19,   // right
						20, 21, 22, 20, 22, 23    // left
		};
	}

	@Override
	protected float[] uvs() {
		return new float[]{
						0, 1, 1, 1, 1, 0, 0, 0, // front
						0, 1, 1, 1, 1, 0, 0, 0, // up
						0, 1, 1, 1, 1, 0, 0, 0, // back
						0, 1, 1, 1, 1, 0, 0, 0, // down
						0, 1, 1, 1, 1, 0, 0, 0, // right
						0, 1, 1, 1, 1, 0, 0, 0, // left
		};
	}

	@Override
	protected float[] colors() {
		return new float[]{
						1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
						1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
						1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
						1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
						1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
						1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
		};
	}

}

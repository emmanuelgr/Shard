package gr.emmanuel.embox.gles2.mesh;

public class Triangle extends Mesh {

	@Override
	protected float[] vertices() {
		return new float[]{
			00.000000f, 00.000000f, 0.000000f, 1f,
			-0.250000f, -0.433013f, 0.000000f, 1f,
			00.250000f, -0.433013f, 0.000000f, 1f
		};
	}

	@Override
	protected short[] indices() {
		return new short[]{
			0, 1, 2
		};
	}

	@Override
	protected float[] uvs() {
		return new float[]{
      00.500000f, (1f - 0.000008f),
      0.175263f, (1f - 0.999940f),
			0.824737f, (1f - 0.999940f)
		};
	}

	@Override
	protected float[] colors() {
		return null;
	}

}

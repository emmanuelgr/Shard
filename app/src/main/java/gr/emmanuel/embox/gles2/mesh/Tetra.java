package gr.emmanuel.embox.gles2.mesh;

public class Tetra extends Mesh{

@Override
protected float[] vertices(){
	return new float[]{
					                   0.426118f, -0.106474f, -0.251465f, 1f,
					                  -0.002711f,  0.239724f,  0.005417f, 1f,
					                   0.004379f, -0.106474f,  0.484237f, 1f,
					                  -0.422301f, -0.106474f, -0.249030f, 1f
	};
}

@Override
protected short[] indices(){
	return new short[]{
					                  3, 1, 4, 2, 1, 3,
					                  1, 4, 2, 5, 3, 6,
					                  4, 7, 2, 8, 1, 9,
					                  2, 10, 4, 11, 3, 12
	};
}

@Override
protected float[] uvs(){
	return new float[]{
					                  0.921089f, 0.784553f,
					                  0.943792f, 0.140699f,
					                  0.614615f, 0.135538f,
					                  0.565268f, 0.825845f,
					                  0.266585f, 0.767731f,
					                  0.839902f, 0.788377f,
					                  0.484753f, 0.387115f,
					                  0.121813f, 0.263238f,
					                  0.158174f, 0.011661f,
					                  0.269182f, 0.454215f,
					                  -0.011993f, 0.485184f,
					                  0.248404f, 1.000000f
	};
}

@Override
protected float[] colors(){
	return null;
}


}

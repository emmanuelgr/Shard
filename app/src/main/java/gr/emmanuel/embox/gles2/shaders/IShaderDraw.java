package gr.emmanuel.embox.gles2.shaders;

import gr.emmanuel.embox.gles2.Object3D;

/**
 * Created by Emmanuel on 31/12/13.
 */
public interface IShaderDraw {
	public void drawPre( Object3D object3D);

	public void draw( Object3D object3D);

	public void drawPost( Object3D object3D);
}

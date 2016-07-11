package gr.emmanuel.embox.gles2;

import gr.emmanuel.embox.gles2.shaders.Blend;
import gr.emmanuel.embox.gles2.shaders.IShaderDraw;
import gr.emmanuel.embox.gles2.mesh.Mesh;

/**
 Created by Emmanuel on 29/12/13.
 */

public class Object3D implements IRenderer{
public Transform transform;
public float[] color = { 1f, 1f, 1f, 1f };
public float ratio;
public IShaderDraw shader;
public Mesh mesh;
public Blend.Modes blendMode;

public Object3D( float ratio, IShaderDraw shader, Mesh mesh, Blend.Modes blendMode ){
	this.ratio = ratio;
	this.shader = shader;
	this.mesh = mesh;
	this.blendMode = blendMode;
	transform = new Transform();

}

public float getRatio(){
	return ratio;
}

@Override
public void onSurfaceCreated(){

}

@Override
public void onSurfaceChanged( int width, int height ){

}

@Override
public void onDrawFrame(){
	Blend.setBlendMode(blendMode);
	shader.drawPre( this );
	shader.draw( this );
	shader.drawPost( this );
}
}

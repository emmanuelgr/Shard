package gr.emmanuel.embox.gles2;

import gr.emmanuel.embox.gles2.mesh.Mesh;
import gr.emmanuel.embox.gles2.shaders.Blend;
import gr.emmanuel.embox.gles2.shaders.IShaderDraw;

/**
 Created by Emmanuel on 16/01/14.
 */

public class RenderObject3DClone implements IRenderObject3DClone{
private int total;

private Object3D[] object3Ds;
public Object3D[] getObject3Ds(){
	return object3Ds;
}
public Transform transform;
public IShaderDraw shader;
public Mesh mesh;
public Blend.Modes blendMode;
private Object3D anObj3D;


public RenderObject3DClone( int total, IShaderDraw shader, Mesh mesh, Blend.Modes blendMode ){
	this.total = total;
	this.shader = shader;
	this.mesh = mesh;
	this.blendMode = blendMode;
	transform = new Transform();
	init();
}
private void init(){
	object3Ds = new Object3D[ total ];
	float indexRatio;
	for ( int i = 0; i < total; i++ ) {
		indexRatio = ( i + 1f ) / object3Ds.length;
		object3Ds[ i ] = new Object3D( indexRatio, shader, mesh, blendMode );
		transform.addChild( object3Ds[ i ].transform );
	}
}

@Override
public void initialize( IOnEachObject3D onEach ){
	for ( int i = 0; i < total; i++ ) {
		onEach.run( object3Ds[ i ] );
	}
}

@Override
public void randomize( IOnEachObject3D onEach ){
	for ( int i = 0; i < total; i++ ) {
		onEach.run( object3Ds[ i ] );
	}
}

@Override
public void onDrawFrame( IOnEachObject3D onEach ){
	Blend.setBlendMode(blendMode);
	for ( int i = 0; i < total; i++ ) {
		anObj3D = object3Ds[ i ];
		if ( i == 0 ) anObj3D.shader.drawPre( anObj3D );
		onEach.run( anObj3D );
		anObj3D.shader.draw( anObj3D );
		if ( i == object3Ds.length - 1 ) anObj3D.shader.drawPost( anObj3D );
	}
}
}

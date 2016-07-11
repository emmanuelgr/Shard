package gr.emmanuel.embox.gles2;

/**
 Created by Emmanuel on 03/02/14.
 */
public interface IRenderObject3DClone{
public void initialize( IOnEachObject3D onEach );
public void randomize( IOnEachObject3D onEach );
public void onDrawFrame( IOnEachObject3D onEach );
}

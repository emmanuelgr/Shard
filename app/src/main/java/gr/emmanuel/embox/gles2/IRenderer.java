package gr.emmanuel.embox.gles2;

/**
 Created by Emmanuel on 31/01/14.
 */
public interface IRenderer{
public void onSurfaceCreated();
public void onSurfaceChanged(int width , int height);
public void onDrawFrame();
}

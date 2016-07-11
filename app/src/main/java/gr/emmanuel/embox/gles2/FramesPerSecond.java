package gr.emmanuel.embox.gles2;

/**
 Created by Emmanuel on 16/03/14.
 */
public class FramesPerSecond{

private long loopStart = 0;
private long loopEnd = 0;
private long loopRunTime = 0;
private final int fps;

public FramesPerSecond( int fps ){
	this.fps = ( 1000 / fps );
}

public void start(){
	loopStart = System.currentTimeMillis();
	if ( loopRunTime < fps ) {
		try {
//			Log.i( Logs.TAG,"Sleep: "+ (FPS - loopRunTime) );
			Thread.sleep( fps - loopRunTime );
		} catch ( InterruptedException e ) {
			e.printStackTrace();
		}
	}
}

public void end(){
	loopEnd = System.currentTimeMillis();
	loopRunTime = ( ( loopEnd - loopStart ) );
}
}

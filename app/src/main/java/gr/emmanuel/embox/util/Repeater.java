package gr.emmanuel.embox.util;

import android.os.Handler;
import android.os.Looper;

public class Repeater{
private Handler handler = new Handler( Looper.getMainLooper() );
private Runnable runnable;
private int every;
private static int timesToRun;
private int count;
private boolean running;

public Repeater( final Runnable runnable, int interval, int timesToRun ){
	every = interval;
	Repeater.timesToRun = timesToRun;
	this.runnable = new Runnable(){
		@Override
		public void run(){
			runnable.run();
			count++;
			if ( count >= Repeater.timesToRun ) {
				stop();
			} else {
				handler.postDelayed( this, every );
			}
		}
	};
}

public synchronized void start(){
	running = true;
	runnable.run();
}

public synchronized void stop(){
	running = false;
	handler.removeCallbacks( runnable );
}

public boolean isRunning(){
	return running;
}
}
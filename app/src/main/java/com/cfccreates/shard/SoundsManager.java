package com.cfccreates.shard;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.SystemClock;

public class SoundsManager{
private SoundPool soundPool;
private int MAX_STREAMS = 2;
private static final int SOUND_PRIORITY = 1;
private int soundCurrent;
private long soundEnds=0;
private SoundVO soundRand;
private SoundVO soundSnap;
private SoundVO soundVari;
private SoundVO soundInte;
private SoundVO soundSwipe;
private SoundVO soundIntro;
private Context context;
private static SoundsManager instance;

public static SoundsManager getInstance( Context context ){
	if ( instance == null ) {
		instance = new SoundsManager( context.getApplicationContext() );
	}
	return instance;
}

private SoundsManager( Context context ){
	this.context = context;

	soundPool = new SoundPool( MAX_STREAMS, AudioManager.STREAM_MUSIC, 0 );

	soundRand = new SoundVO( context, soundPool, R.raw.random2, SOUND_PRIORITY );
	soundSnap = new SoundVO( context, soundPool, R.raw.snapshot2, SOUND_PRIORITY );
	soundVari = new SoundVO( context, soundPool, R.raw.variation4, SOUND_PRIORITY );
	soundInte = new SoundVO( context, soundPool, R.raw.intensity2, SOUND_PRIORITY );
	soundSwipe = new SoundVO( context, soundPool, R.raw.swipe1, SOUND_PRIORITY );
	soundIntro = new SoundVO( context, soundPool, R.raw.startup2_1, SOUND_PRIORITY );

}

private void tryToPlay( SoundVO soundVO ){
	if ( !Model.audioOn ) return;
	if( SystemClock.uptimeMillis() > soundEnds ) play( soundVO );
	if( soundVO.id == soundSwipe.id  ) play( soundVO );
	if ( soundCurrent == soundVO.id && SystemClock.uptimeMillis() < soundEnds ) return;
}
private void play( SoundVO soundVO ){
	soundCurrent = soundVO.id;
	soundEnds = soundVO.duration + SystemClock.uptimeMillis();
	soundPool.play( soundVO.id, 0.8f, 0.8f, SOUND_PRIORITY, 0, 1 );
}

public void playSwipe(){
	tryToPlay( soundSwipe );
}


public void playRandomizing(){
	tryToPlay( soundRand );
}

public void playSnapshot(){
	tryToPlay( soundSnap );
}

public void playVariation(){
	tryToPlay( soundVari );
}

public void playIntensity(){
	tryToPlay( soundInte );
}

public void playIntro(){
	tryToPlay( soundIntro );
}
}

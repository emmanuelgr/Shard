package com.cfccreates.shard;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 Created by Emmanuel on 12/02/14.
 */
public class SoundVO{
public int id;
public long duration;
private Context context;

public SoundVO( Context context, SoundPool soundPool, int rawId, int soundPriority ){
	this.context = context;
	id = soundPool.load(context, rawId , soundPriority);
	setSoundDuration( rawId );
}

private void setSoundDuration( int rawId ){
	MediaPlayer player = MediaPlayer.create( context, rawId );
	duration = player.getDuration();
	player.release();
}
}

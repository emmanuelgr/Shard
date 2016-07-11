package com.cfccreates.shard.fxs;

import android.content.Context;

import com.cfccreates.shard.Library;

import gr.emmanuel.embox.gles2.Transform;

/**
 Created by Emmanuel on 16/01/14.
 */
public class FxDummy extends AFX{

public FxDummy( Context context, Transform mamaFX ){
	super( context, mamaFX, "", "", true, 0, false, false, false, false, false );
}


@Override
public void onSurfaceChanged( int width, int height ){
}

@Override
public void randomize(){
}

@Override
public void onDrawFrame(){
	super.onDrawFrame();
}
}

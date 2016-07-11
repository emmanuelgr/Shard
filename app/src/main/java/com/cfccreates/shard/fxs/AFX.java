package com.cfccreates.shard.fxs;

import android.content.Context;
import android.util.Log;

import com.cfccreates.shard.Library;
import com.cfccreates.shard.Model;
import com.cfccreates.shard.SoundsManager;
import com.cfccreates.shard.activities.GLActivity;

import gr.emmanuel.embox.gles2.IRenderer;
import gr.emmanuel.embox.gles2.Transform;
import gr.emmanuel.embox.util.Logs;

/**
 Created by Emmanuel on 24/02/14.
 */
public abstract class AFX implements IRenderer{
protected final Context context;
protected Library library;
private float scroll = 0;
public Transform mamaFX;
public Transform transform = new Transform();
public final int index;
public float intencity = 0;
public final String title;
public final String feedbackText;
public final boolean scrollerValuesClamp;
public final float scrollerIncrements;
public final boolean randomizable;
public final boolean intesible;
public final boolean snapshotable;
public final boolean variationable;
public final boolean linearVariation;
public static int count = 0;
public static float length;
protected float prevValue;

public AFX( Context context, Transform mamaFX, String title, String feedbackText, boolean scrollerValuesClamp, float scrollerIncrements, boolean randomizable, boolean intesible, boolean snapshotable, boolean variationable, boolean linearVariation ){
	this.context = context;
	this.mamaFX = mamaFX;
	this.title = title;
	this.feedbackText = feedbackText;
	this.scrollerValuesClamp = scrollerValuesClamp;
	this.scrollerIncrements = scrollerIncrements;
	this.randomizable = randomizable;
	this.intesible = intesible;
	this.snapshotable = snapshotable;
	this.variationable = variationable;
	this.linearVariation = linearVariation;

	index = count;
	count++;
	mamaFX.addChild( transform );
	transform.setPositionX( Model.fxCardsDistance * index );
	length = Model.fxCardsDistance * count;
}

@Override
public void onSurfaceCreated(){
	library = Library.getInstance( context );
}

public abstract void randomize();

public float getScroll(){
	return scroll;
}

public void setScroll( float scroll ){
	this.scroll = scroll;
}

public void incrementScroll( float increment ){
	scroll += increment;
	if ( scrollerValuesClamp ) {
		if ( scroll < 0 ) scroll = 0f;
		if ( scroll > 1f ) scroll = 1f;
	} else {
		if ( scroll < 0 ) scroll = 1f + scroll;
		if ( scroll > 1f ) scroll = scroll - 1f;
	}
}

public void playSoundVar(){

	SoundsManager.getInstance(context).playVariation();
//	((GLActivity)(context)).runOnUiThread( new Runnable(){
//		public void run(){
//			((GLActivity)(context)).sou;
//		}
//	} );
}
@Override
public void onDrawFrame(){
	if(transform.getPosGlobal().x < Model.fxCardsDistance * -2) transform.setPositionX( transform.getPositionX() + length );
	if(transform.getPosGlobal().x > Model.fxCardsDistance * (count-2)) transform.setPositionX( transform.getPositionX() - length );
}
}

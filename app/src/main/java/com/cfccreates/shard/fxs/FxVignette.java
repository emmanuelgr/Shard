package com.cfccreates.shard.fxs;

import android.content.Context;

import com.cfccreates.shard.Library;
import com.jme3.math.Vector3f;

import org.j3d.texture.procedural.PerlinNoiseGenerator;

import java.util.Random;

import gr.emmanuel.embox.gles2.Object3D;
import gr.emmanuel.embox.gles2.Transform;
import gr.emmanuel.embox.gles2.shaders.Blend;
import gr.emmanuel.embox.gles2.shaders.ShaderCamera;

/**
 Created by Emmanuel on 16/01/14.
 */
public class FxVignette extends AFX{
private ShaderCamera shaderCamera;
private Object3D bgCamFeed;
private Object3D hazeObj;
private Object3D vignetteObj;
private float scroll;
private Random random;

private PerlinNoiseGenerator perlin;

public FxVignette( Context context, Transform mamaFX, ShaderCamera shaderCamera ){
	super( context, mamaFX, "Vignette", "Play", true, 0.001f, false, true, true, true, true );
	this.shaderCamera = shaderCamera;
	perlin = new PerlinNoiseGenerator();
	random = new Random();

}

@Override
public void onSurfaceCreated(){
	super.onSurfaceCreated();

	bgCamFeed = new Object3D( 1, shaderCamera, library.meshSquare, Blend.Modes.NORMAL );
	vignetteObj = new Object3D( 1, library.shaderTexturevignettes[ 0 ], library.meshSquare, Blend.Modes.NORMAL );
	hazeObj = new Object3D( 1, library.shaderTexturevignettes[ 0 ], library.meshSquare, Blend.Modes.NORMAL );
	bgCamFeed.transform.setScale( new Vector3f( 4.5302773f, 2.548281f, 1f ) );
	vignetteObj.transform.setScale( new Vector3f( 4.5302773f, 2.548281f, 1f ) );
	hazeObj.transform.setScale( new Vector3f( 4.5302773f, 2.548281f, 1f ) );
	transform.addChild( bgCamFeed.transform );
	transform.addChild( vignetteObj.transform );
	transform.addChild( hazeObj.transform );
}

@Override
public void onSurfaceChanged( int width, int height ){
}

@Override
public void randomize(){
//	vignetteObj.shader = vignettes[ ((int) ( random.nextFloat() * 9f )) ];
//	vignetteObj.color[ 0 ] = random.nextFloat()*0.3f;
//	vignetteObj.color[ 1 ] = random.nextFloat()*0.3f;
//	vignetteObj.color[ 2 ] = random.nextFloat()*0.3f;
}

@Override
public void onDrawFrame(){
	super.onDrawFrame();
	int haze = (int) (( getScroll() * ( library.shaderTexturevignettesHazes.length - 1 ) * ( library.shaderTexturevignettes.length - 1 ) ) %  library.shaderTexturevignettesHazes.length);
	if ( prevValue != haze ) playSoundVar();
	prevValue = haze;
	hazeObj.shader = library.shaderTexturevignettesHazes[ haze ];
	vignetteObj.shader = library.shaderTexturevignettes[ ( (int) ( getScroll() * ( ( library.shaderTexturevignettes ).length - 1 ) ) ) ];
	bgCamFeed.onDrawFrame();
	hazeObj.onDrawFrame();
	vignetteObj.onDrawFrame();
}


}

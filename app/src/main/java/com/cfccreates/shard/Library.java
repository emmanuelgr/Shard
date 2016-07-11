package com.cfccreates.shard;

import android.content.Context;

import gr.emmanuel.embox.gles2.mesh.Cube;
import gr.emmanuel.embox.gles2.mesh.Mesh;
import gr.emmanuel.embox.gles2.mesh.Square;
import gr.emmanuel.embox.gles2.mesh.Tetra;
import gr.emmanuel.embox.gles2.mesh.Trapezoid;
import gr.emmanuel.embox.gles2.mesh.Triangle;
import gr.emmanuel.embox.gles2.shaders.ShaderConstant;
import gr.emmanuel.embox.gles2.shaders.ShaderTexture;

/**
 Created by Emmanuel on 16/01/14.
 */
public class Library{
public final ShaderTexture shaderTextureSquareX1;
public final ShaderTexture shaderTextureSquareX2;
public final ShaderTexture shaderTextureSquare1;
public final ShaderTexture shaderTextureSquare2;
public final ShaderTexture shaderTextureSquare3;

public final ShaderTexture shaderTextureHotSpot1;
public final ShaderTexture shaderTextureHotSpot2;
public final ShaderTexture shaderTextureHotSpot3;
public final ShaderTexture shaderTextureHotSpot4;
public final ShaderTexture shaderTextureHotSpot5;

public final ShaderTexture shaderTextureSpot1;
public final ShaderTexture shaderTextureSpot2;
public final ShaderTexture shaderTextureSpot3;
public final ShaderTexture shaderTextureSpot4;
public final ShaderTexture shaderTextureSpot5;

public final ShaderTexture[] shaderTexturevignettes;
public final ShaderTexture[] shaderTexturevignettesHazes;
public final ShaderTexture[] shaderTextureHazes;
public final ShaderTexture shaderTextureUV;
public final ShaderTexture shaderTextureViewFinder;
public final ShaderTexture shaderTextureSettings;

public final ShaderConstant shaderConstant;

public final Mesh meshCube;
public final Mesh meshSquare;
public final Mesh meshTetra;
public final Mesh meshTrapezoid;
public final Mesh meshTriangle;
private Context context;
private static Library instance;

public static Library getInstance( Context context ){
	if ( instance == null ) {
		instance = new Library( context.getApplicationContext() );
	}
	return instance;
}
public static void forceUpdate( Context context ){
	instance = new Library( context.getApplicationContext() );
}
private Library( Context context ){
	this.context = context;

	meshCube = new Cube();
	meshSquare = new Square();
	meshTetra = new Tetra();
	meshTrapezoid = new Trapezoid();
	meshTriangle = new Triangle();

	shaderTextureSquareX1 = new ShaderTexture( R.raw.square_x_1, context );
	shaderTextureSquareX2 = new ShaderTexture( R.raw.square_x_2, context );

	shaderTextureSquare1 = new ShaderTexture( R.raw.square_1, context );
	shaderTextureSquare2 = new ShaderTexture( R.raw.square_2, context );
	shaderTextureSquare3 = new ShaderTexture( R.raw.square_3, context );

	shaderTextureHotSpot1 = new ShaderTexture( R.raw.hot_spot_1, context );
	shaderTextureHotSpot2 = new ShaderTexture( R.raw.hot_spot_2, context );
	shaderTextureHotSpot3 = new ShaderTexture( R.raw.hot_spot_3, context );
	shaderTextureHotSpot4 = new ShaderTexture( R.raw.hot_spot_4, context );
	shaderTextureHotSpot5 = new ShaderTexture( R.raw.hot_spot_5, context );

	shaderTextureSpot1 = new ShaderTexture( R.raw.spot_1, context );
	shaderTextureSpot2 = new ShaderTexture( R.raw.spot_2, context );
	shaderTextureSpot3 = new ShaderTexture( R.raw.spot_3, context );
	shaderTextureSpot4 = new ShaderTexture( R.raw.spot_4, context );
	shaderTextureSpot5 = new ShaderTexture( R.raw.spot_5, context );

	shaderTextureHazes = new ShaderTexture[]{
//					                                        new ShaderTexture( R.raw.hazes00, context ),
//					                                        new ShaderTexture( R.raw.hazes01, context ),
					                                        new ShaderTexture( R.raw.hazes02, context ),
//					                                        new ShaderTexture( R.raw.hazes03, context ),
//					                                        new ShaderTexture( R.raw.hazes04, context ),
//					                                        new ShaderTexture( R.raw.hazes05, context ),
					                                        new ShaderTexture( R.raw.hazes06, context ),
//					                                        new ShaderTexture( R.raw.hazes07, context ),
					                                        new ShaderTexture( R.raw.hazes08, context ),
					                                        new ShaderTexture( R.raw.hazes09, context ),
					                                        new ShaderTexture( R.raw.hazes10, context ),
//					                                        new ShaderTexture( R.raw.hazes11, context ),
//					                                        new ShaderTexture( R.raw.hazes12, context ),
					                                        new ShaderTexture( R.raw.hazes13, context ),
//					                                        new ShaderTexture( R.raw.hazes14, context ),
					                                        new ShaderTexture( R.raw.hazes15, context ),
//					                                        new ShaderTexture( R.raw.hazes16, context ),
//					                                        new ShaderTexture( R.raw.hazes17, context ),
//					                                        new ShaderTexture( R.raw.hazes18, context ),
					                                        new ShaderTexture( R.raw.hazes19, context ),
					                                        new ShaderTexture( R.raw.hazes20, context ),
//					                                        new ShaderTexture( R.raw.hazes21, context ),
					                                        new ShaderTexture( R.raw.hazes22, context ),

					                                        new ShaderTexture( R.raw.hazes23, context ),
					                                        new ShaderTexture( R.raw.hazes24, context ),
					                                        new ShaderTexture( R.raw.hazes25, context ),
					                                        new ShaderTexture( R.raw.hazes26, context ),
//					                                        new ShaderTexture( R.raw.hazes27, context ),
//					                                        new ShaderTexture( R.raw.hazes28, context ),
					                                        new ShaderTexture( R.raw.hazes29, context )
//					                                        new ShaderTexture( R.raw.hazes30, context ),
//					                                        new ShaderTexture( R.raw.hazes31, context ),
//					                                        new ShaderTexture( R.raw.hazes32, context )
	};

	shaderTexturevignettesHazes = new ShaderTexture[]{
//					                                            new ShaderTexture( R.raw.vigntt_haz_64_01, context ),
//					                                            new ShaderTexture( R.raw.vigntt_haz_64_02, context ),
//					                                            new ShaderTexture( R.raw.vigntt_haz_64_03, context ),
//					                                            new ShaderTexture( R.raw.vigntt_haz_64_04, context ),
//					                                            new ShaderTexture( R.raw.vigntt_haz_64_05, context ),
					                                            new ShaderTexture( R.raw.vigntt_haz_64_06, context ),
					                                            new ShaderTexture( R.raw.vigntt_haz_64_07, context ),
					                                            new ShaderTexture( R.raw.vigntt_haz_64_08, context ),
//					                                            new ShaderTexture( R.raw.vigntt_haz_64_09, context ),
					                                            new ShaderTexture( R.raw.vigntt_haz_64_10, context ),
//					                                            new ShaderTexture( R.raw.vigntt_haz_64_11, context ),
					                                            new ShaderTexture( R.raw.vigntt_haz_64_12, context )
	};

	shaderTexturevignettes = new ShaderTexture[]{
					                                            new ShaderTexture( R.raw.vignettes00, context ),
//					                                            new ShaderTexture( R.raw.vignettes01, context ),
					                                            new ShaderTexture( R.raw.vignettes02, context ),
//					                                            new ShaderTexture( R.raw.vignettes03, context ),
					                                            new ShaderTexture( R.raw.vignettes04, context ),
//					                                            new ShaderTexture( R.raw.vignettes05, context ),
					                                            new ShaderTexture( R.raw.vignettes06, context ),
					                                            new ShaderTexture( R.raw.vignettes07, context )
	};


	shaderTextureUV = new ShaderTexture( R.raw.uv, context );
	shaderTextureViewFinder = new ShaderTexture( R.raw.viewfinder, context );
	shaderTextureSettings = new ShaderTexture( R.raw.sshard_settings_settings, context );


	shaderConstant = new ShaderConstant();

}
}

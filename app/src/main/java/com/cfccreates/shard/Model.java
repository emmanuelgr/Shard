package com.cfccreates.shard;

import android.os.Environment;

/**
 Created by Emmanuel on 18/02/14.
 */
public class Model{
public static final String PATH_FOR_IMAGES = 	Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DCIM ).toString() + "/" + "Camera";
public static final float fxCardsDistance = 8f;
public static String email;
public static boolean audioOn = true;
public static boolean visualOn = true;
public static boolean tutorialComplete = false;
public static boolean firstRun;
public static boolean sharingOn;
public static int screenW;
public static int screenH;
public static int cameraMaxW;
public static int cameraMaxH;
public static String deviceID;
public static String userID;
}

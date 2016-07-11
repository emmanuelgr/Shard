package gr.emmanuel.embox.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 Created by Emmanuel on 18/02/14.
 */
public class LocalStore{

public static void clear( Context context, String fileName ){
	Editor editor = context.getSharedPreferences( fileName, Context.MODE_PRIVATE ).edit();
	editor.clear();
	editor.commit();
}

public static boolean setBoolean( Context context, String fileName, String key, boolean value ){
	Editor editor = context.getSharedPreferences( fileName, Context.MODE_PRIVATE ).edit();
	editor.putBoolean( key, value );

	return editor.commit();
}

public static boolean getBoolean( Context context, String fileName, String key, boolean defValue ){
	SharedPreferences savedSession = context.getSharedPreferences( fileName, Context.MODE_PRIVATE );

	return ( savedSession.getBoolean( key, defValue ) );
}

public static boolean setString( Context context, String fileName, String key, String value ){
	SharedPreferences.Editor editor = context.getSharedPreferences( fileName, Context.MODE_PRIVATE ).edit();
	editor.putString( key, value );

	return editor.commit();
}

public static String getString( Context context, String fileName, String key , String defValue){
	SharedPreferences savedSession = context.getSharedPreferences( fileName, Context.MODE_PRIVATE );

	return ( savedSession.getString( key, defValue ) );
}


public static boolean stringExist( Context context, String fileName, String customKey ){
	SharedPreferences savedSession = context.getSharedPreferences( fileName, Context.MODE_PRIVATE );

	return ( savedSession.getString( customKey, null ) ) == null ? false : true;
}

}

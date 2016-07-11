package com.cfccreates.shard.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfccreates.shard.LocalStoreKeys;
import com.cfccreates.shard.Model;
import com.cfccreates.shard.R;
import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

import gr.emmanuel.embox.util.LocalStore;


public class MenuActivity extends Activity{
private List<View> views;
private Resources resources;
CardScrollView cardScrollView;
String[] strings;
public static String AUDIO_ON;
public static String AUDIO_OFF;
private Context context;

@Override
protected void onCreate( Bundle savedInstanceState ){
	super.onCreate( savedInstanceState );
	context = this;

	resources = getResources();
	strings = new String[]{ "Play Shard", "Tutorial", ( Model.audioOn ) ? AUDIO_ON : AUDIO_OFF, "FaceBook",
//		( Model.visualOn ) ? VISUAL_ON : VISUAL_OFF,
					                      "Produced.", "Credits.",
//					                      "Licenses."
	};

	views = new ArrayList<View>();

	View view;
	for ( int i = 0; i < strings.length; i++ ) {
		view = LayoutInflater.from( context ).inflate( R.layout.scrollview, null );
		ImageView iv = (ImageView) view.findViewById( R.id.imageView );
//		TextView tv = (TextView) view.findViewById( R.id.textView );
//		tv.setText( strings[ i ] );
		views.add( view );


		switch ( i ) {
		case 0:
			iv.setImageResource( R.drawable.sshard_play );
			break;
		case 1:
			iv.setImageResource( R.drawable.sshard_settings_tutorial );
			break;
		case 2:
			if ( Model.audioOn ) {
				iv.setImageResource( R.drawable.sshard_settings_sound_on );
			} else {
				iv.setImageResource( R.drawable.sshard_settings_sound_off );
			}
			break;
		case 3:
			if ( Model.sharingOn ) {
				iv.setImageResource( R.drawable.fb_on );
			} else {
				iv.setImageResource( R.drawable.fb_off );
			}
			break;
		case 4:
			iv.setImageResource( R.drawable.sshard_settings_produced );
			break;
		case 5:
			iv.setImageResource( R.drawable.sshard_settings_credits );
			break;
		case 6:
			iv.setImageResource( R.drawable.sshard_settings_licences );
			break;
		}
	}


	cardScrollView = new CardScrollView( this );
	SettingsCardScrollAdapter adapter = new SettingsCardScrollAdapter();
	cardScrollView.setAdapter( adapter );
	cardScrollView.setOnItemClickListener( onItemClickListener );
	cardScrollView.activate();

	setContentView( cardScrollView );
}

@Override
protected void onResume(){
	super.onResume();
//	cardScrollView.updateViews( true );
}

@Override
public void onBackPressed(){
	Intent intent = new Intent( Intent.ACTION_MAIN );
	intent.addCategory( Intent.CATEGORY_HOME );
	intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
	startActivity( intent );
}


AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
	@Override
	public void onItemClick( AdapterView<?> adapterView, View view, int position, long id ){
//  save the card index that was selected
//	iPosition = position;
//		Log.i( Logs.TAG, "position: " + position );
//		TextView textView = (TextView) ( cardScrollView.getSelectedView().findViewById( R.id.textView ) );
//		textView.setVisibility( View.GONE );
		ImageView imageView = (ImageView) ( cardScrollView.getSelectedView().findViewById( R.id.imageView ) );

		Intent intent;
		switch ( position ) {
		case 0:
			intent = new Intent( context, GLActivity.class );
			intent.setFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
			startActivity( intent );
			break;
		case 1:
			intent = new Intent( context, TutorialActivity1.class );
			intent.putExtra( BaseTutorialActivity.PREV_ACTIVITY_KEY, ShardsActivities.Settings );
			intent.setFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
			startActivity( intent );
			break;
		case 2:
			if ( Model.audioOn ) {
				Model.audioOn = false;
				strings[ position ] = AUDIO_OFF;
				imageView.setImageResource( R.drawable.sshard_settings_sound_off );
				LocalStore.setBoolean( context, LocalStoreKeys.FILENAME, LocalStoreKeys.AUDIO, false );
			} else {
				Model.audioOn = true;
				strings[ position ] = AUDIO_ON;
				imageView.setImageResource( R.drawable.sshard_settings_sound_on );
				LocalStore.setBoolean( context, LocalStoreKeys.FILENAME, LocalStoreKeys.AUDIO, true );
			}
			break;
		case 3:
			if ( Model.userID == "" ) {
//				intent = new Intent( context, FB_ActivationActivity.class );
//				intent.setFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
//				startActivity( intent );
			} else {
				if ( Model.sharingOn ) {
					Model.sharingOn = false;
					imageView.setImageResource( R.drawable.fb_off );
					LocalStore.setBoolean( context, LocalStoreKeys.FILENAME, LocalStoreKeys.SHARE, false );
				} else {
					Model.sharingOn = true;
					imageView.setImageResource( R.drawable.fb_on );
					LocalStore.setBoolean( context, LocalStoreKeys.FILENAME, LocalStoreKeys.SHARE, true );
				}
			}
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		}
	}
};

private class SettingsCardScrollAdapter extends CardScrollAdapter{

	@Override
	public int getPosition( Object item ){
		return java.util.Arrays.asList( strings ).indexOf( item );
	}

	@Override
	public int getCount(){
		return strings.length;
	}

	@Override
	public Object getItem( int position ){
		return strings[ position ];
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ){
		View view = views.get( position );
		return view;
	}
}


}

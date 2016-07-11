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

import com.cfccreates.shard.LocalStoreKeys;
import com.cfccreates.shard.Model;
import com.cfccreates.shard.R;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import gr.emmanuel.embox.util.LocalStore;

public class SettingsActivity extends Activity{

public static String AUDIO_ON;
public static String AUDIO_OFF;
public static String VISUAL_ON;
public static String VISUAL_OFF;
private Resources resources;
CardScrollView cardScrollView;
String[] strings;
private Context context;

@Override
protected void onCreate( Bundle savedInstanceState ){
	super.onCreate( savedInstanceState );
	context = this;

	resources = getResources();
	AUDIO_ON = resources.getString( R.string.audioOn );
	AUDIO_OFF = resources.getString( R.string.audioOff );
	VISUAL_ON = resources.getString( R.string.visualOn );
	VISUAL_OFF = resources.getString( R.string.visualOff );
	strings = new String[]{
		"Tutorial.",
		( Model.audioOn ) ? AUDIO_ON : AUDIO_OFF,
//		( Model.visualOn ) ? VISUAL_ON : VISUAL_OFF,
		"Produced.",
		"Credits.",
		"Licenses."

	};

	cardScrollView = new CardScrollView( this );
	SettingsCardScrollAdapter adapter = new SettingsCardScrollAdapter();
	cardScrollView.setAdapter( adapter );
	cardScrollView.setOnItemClickListener( onItemClickListener );
	cardScrollView.activate();

	setContentView( cardScrollView );
}

@Override
public void onBackPressed(){
	Intent intent = new Intent( this, GLActivity.class );
	intent.setFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
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
//			if ( Model.visualOn ) {
//				Model.visualOn = false;
//				strings[ position ] = VISUAL_OFF;
//				textView.setText( strings[ position ] );
//				LocalStore.setBoolean( context, LocalStoreKeys.VISUAL, false );
//			} else {
//				Model.visualOn = true;
//				strings[ position ] = VISUAL_ON;
//				textView.setText( strings[ position ] );
//				LocalStore.setBoolean( context, LocalStoreKeys.VISUAL, true );
//			}
			intent = new Intent( context, BaseTutorialActivity.class );
			intent.putExtra( BaseTutorialActivity.PREV_ACTIVITY_KEY, ShardsActivities.Settings );
			intent.setFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
			startActivity( intent );
			break;
		case 1:
			if ( Model.audioOn ) {
				Model.audioOn = false;
				strings[ position ] = AUDIO_OFF;
//				textView.setText( strings[ position ] );
				imageView.setImageResource( R.drawable.sshard_settings_sound_off );
				LocalStore.setBoolean( context, LocalStoreKeys.FILENAME, LocalStoreKeys.AUDIO, false );
			} else {
				Model.audioOn = true;
				strings[ position ] = AUDIO_ON;
//				textView.setText( strings[ position ] );
				imageView.setImageResource( R.drawable.sshard_settings_sound_on );
				LocalStore.setBoolean( context, LocalStoreKeys.FILENAME, LocalStoreKeys.AUDIO, true );
			}
			break;
		case 2:
			imageView.setImageResource( R.drawable.sshard_settings_produced );
			break;
		case 3:
			imageView.setImageResource( R.drawable.sshard_settings_credits );
			break;
		case 4:
//			intent = new Intent(context, LicensesActivity.class);
//			startActivity(intent);
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
		if ( convertView == null ) {
			convertView = LayoutInflater.from( context ).inflate( R.layout.scrollview, parent );
		}

//		final TextView textView = (TextView) convertView.findViewById( R.id.textView );
//		textView.setText( strings[ position ] );

		ImageView imageView = (ImageView) ( convertView.findViewById( R.id.imageView ) );
		switch ( position ) {
		case 0:
			imageView.setImageResource( R.drawable.sshard_settings_tutorial );
			break;
		case 1:
			imageView.setImageResource( R.drawable.sshard_settings_sound_on );
			break;
		case 2:
			imageView.setImageResource( R.drawable.sshard_settings_produced );
			break;
		case 3:
			imageView.setImageResource( R.drawable.sshard_settings_credits );
			break;
		case 4:
			imageView.setImageResource( R.drawable.sshard_settings_licences );
			break;
		}

		return  imageView;
	}
}

}
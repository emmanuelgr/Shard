package EmBoxUnity.Commands;

import android.util.Log;

import EmBoxUnity.Commands.Core.BaseCommand;
import gr.emmanuel.embox.util.Logs;

public class CForDebugging extends BaseCommand{
@Override
protected void DoIn(){
	Log.i( Logs.TAG, " in" );
	ExecuteInComplete();
}

@Override
protected void CancelIn(){

}

@Override
protected void DoOut(){
	Log.i( Logs.TAG, " out" );
	ExecuteOutComplete();
}

@Override
protected void CancelOut(){

}

@Override
public void update(){

}
}
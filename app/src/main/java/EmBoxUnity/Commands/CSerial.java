package EmBoxUnity.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import EmBoxUnity.Commands.Core.BaseCommand;
import EmBoxUnity.Commands.Core.IAction;
import EmBoxUnity.Commands.Core.ICommand;
import EmBoxUnity.Commands.Core.ICommandComposition;
import EmBoxUnity.EmBox;

public class CSerial extends BaseCommand implements ICommandComposition{
private List<ICommand> cmnds;
private List<Integer> guids;
private List<Integer> guidsInComplete;
private List<Integer> guidsOutComplete;
private int cursor = 0;

private void init(){
	cmnds = new ArrayList<ICommand>();
	guids = new ArrayList<Integer>();
	guidsInComplete = new ArrayList<Integer>();
	guidsOutComplete = new ArrayList<Integer>();
}

public CSerial(){
	super();
	init();
}

public CSerial( ICommand[] cmnds ){
	super();
	init();
	this.cmnds.addAll( Arrays.asList( cmnds ) );
	for ( int i = 0; i < cmnds.length; i++ ) {
		guids.add( cmnds[ i ].GUID );
		guidsInComplete.add( EmBox.GUID() );
		guidsOutComplete.add( EmBox.GUID() );
	}
}

public void Add( ICommand cmnd ){
	cmnds.add( cmnd );
	guids.add( cmnd.GUID );
	guidsInComplete.add( EmBox.GUID() );
	guidsOutComplete.add( EmBox.GUID() );
}

protected void DoIn(){
	if ( cmnds.size() == 0 ) {
		ExecuteInComplete();
		return;
	}
	cmnds.get( cursor ).AddOnInComplete( subInCom, guidsInComplete.get( cursor ) );
	cmnds.get( cursor ).ExecuteIn();
}

@Override
protected void CancelIn(){
	cmnds.get( cursor ).RemOnInComplete( guidsInComplete.get( cursor ) );
}

@Override
protected void DoOut(){
	if ( cmnds.size() == 0 ) {
		ExecuteOutComplete();
		return;
	}
	cmnds.get( cursor ).AddOnInComplete( subOutCom, guidsOutComplete.get( cursor ) );
	cmnds.get( cursor ).ExecuteOut();
}

@Override
protected void CancelOut(){
	cmnds.get( cursor ).RemOnOutComplete( guidsOutComplete.get( cursor ) );
}

private IAction subInCom = new IAction(){
	@Override
	public void act( int i ){
		int index = guidsInComplete.indexOf( GUID() );
		cmnds.get( index ).RemOnInComplete( GUID() );
		if ( cursor == cmnds.size() - 1 ) {
			ExecuteInComplete();
		} else {
			cursor++;
			cmnds.get( cursor ).AddOnInComplete( subInCom, guidsInComplete.get( cursor ) );
			cmnds.get( cursor ).ExecuteIn();
		}
	}
};


private IAction subOutCom = new IAction(){
	@Override
	public void act( int i ){
		int index = guidsOutComplete.indexOf( GUID() );
		cmnds.get( index ).RemOnOutComplete( GUID() );
		if ( cursor == 0 ) {
			ExecuteOutComplete();
		} else {
			cursor--;
			cmnds.get( cursor ).AddOnInComplete( subOutCom, guidsOutComplete.get( cursor ) );
			cmnds.get( cursor ).ExecuteOut();
		}
	}
};

@Override
public void update(){

}
}


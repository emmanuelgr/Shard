package EmBoxUnity.Commands.Core;

import java.util.ArrayList;
import java.util.List;

import EmBoxUnity.EmBox;

public abstract class BaseCommand implements ICommand{

private int guid;
private List<Integer> guidsIn = new ArrayList<Integer>();
private List<IAction> actionsIn = new ArrayList<IAction>();
private List<Integer> guidsOut = new ArrayList<Integer>();
private List<IAction> actionsOut = new ArrayList<IAction>();
private static int anInt;

public int GUID(){
	return guid;
}

public StatesC State;

public BaseCommand(){
	guid = EmBox.GUID();
	State = StatesC.ExecuteNone;
}

public void ExecuteIn(){
	switch ( State ) {
	case ExecutingIn:
		// busy... already executing In
		break;
	case ExecuteInComplete:
		// all done but trigger fn() call again
		ExecuteInComplete();
		break;
	case ExecutingOut:
		CancelOut();
		seqIn();
		break;
	case ExecuteError:
	case ExecuteNone:
	case ExecuteOutComplete:
		seqIn();
		break;
	}
}

private void seqIn(){
	State = StatesC.ExecutingIn;
	PreExecuteIn();
	DoIn();
}

public void ExecuteOut(){
	switch ( State ) {
	case ExecutingOut:
		// busy... already executing Out
		break;
	case ExecuteOutComplete:
		// all done but trigger fn() call again
		ExecuteOutComplete();
		break;
	case ExecuteNone:
		// Will need to do an in before ...
		break;
	case ExecutingIn:
		CancelIn();
		seqOut();
		break;
	case ExecuteError:
	case ExecuteInComplete:
		seqOut();
		break;
	}
}

private void seqOut(){
	State = StatesC.ExecutingOut;
	PreExecuteOut();
	DoOut();
}

/// <summary>
/// Allways call this Fn when DoIn has finished
/// </summary>
protected void ExecuteInComplete(){
	State = StatesC.ExecuteInComplete;
	PostExecuteIn();
	for ( int i = 0; i < actionsIn.size(); i++ ) {
		actionsIn.get( i ).act( guidsIn.get( i ) );
	}
}

/// <summary>
/// Allways call this Fn when DoOut has finished
/// </summary>
protected void ExecuteOutComplete(){
	State = StatesC.ExecuteOutComplete;
	PostExecuteOut();
	for ( int i = 0; i < actionsOut.size(); i++ ) {
		actionsOut.get( i ).act( guidsOut.get( i ) );
	}
}

/// <summary>
/// Triggered before DoIn()
/// </summary>
protected void PreExecuteIn(){
}

/// <summary>
///  Override and call ExecuteInComplete() when done
/// </summary>
protected abstract void DoIn();

protected abstract void CancelIn();

/// <summary>
/// Triggered after ExecuteInComplete()
/// </summary>
protected void PostExecuteIn(){
}

/// <summary>
/// Triggered before DoOut()
/// </summary>
protected void PreExecuteOut(){
}

/// <summary>
/// Override and call ExecuteOutComplete() when done
/// </summary>
protected abstract void DoOut();

protected abstract void CancelOut();

/// <summary>
/// Triggered after ExecuteOutComplete()
/// </summary>
protected void PostExecuteOut(){
}

public void Toggle(){
	switch ( State ) {
	case ExecutingIn:
	case ExecuteInComplete:
		ExecuteOut();
		break;
	case ExecutingOut:
	case ExecuteOutComplete:
	case ExecuteNone:
		ExecuteIn();
		break;
	}
}

public void AddOnInComplete( IAction act, int guid ){
	anInt = guidsIn.indexOf( guid );
	if ( anInt >= 0 ) {
	} else {
		guidsIn.add( guid );
		actionsIn.add( act );
	}
}

public void RemOnInComplete( int guid ){
	anInt = guidsIn.indexOf( guid );
	if ( anInt >= 0 ) {
		guidsIn.remove( anInt );
		actionsIn.remove( anInt );
	}
}

public void AddOnOutComplete( IAction act, int guid ){
	anInt = guidsOut.indexOf( guid );
	if ( anInt >= 0 ) {
	} else {
		guidsOut.add( guid );
		actionsOut.add( act );
	}
}

public void RemOnOutComplete( int guid ){
	anInt = guidsIn.indexOf( guid );
	if ( anInt >= 0 ) {
		guidsOut.remove( anInt );
		actionsOut.remove( anInt );
	}
}

}
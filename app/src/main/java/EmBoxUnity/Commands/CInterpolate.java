package EmBoxUnity.Commands;

import EmBoxUnity.Commands.Core.BaseCommand;
import EmBoxUnity.Commands.Core.StatesC;
import EmBoxUnity.EmBox;
import EmBoxUnity.Utils.Interpolate;
import gr.emmanuel.embox.util.Time;

public class CInterpolate extends BaseCommand{

private float interpolated, ratio, timer;
public float fromIn, toIn, fromOut, toOut, timeIn, timeOut;
public Interpolate.Eases inFn, outFn;
private boolean active =false;

public CInterpolate( float fromIn, float pose, float toOut, float timeIn, float timeOut, Interpolate.Eases inFn, Interpolate.Eases outFn ){
	super();
	this.fromIn = interpolated = fromIn;
	this.toIn = pose;
	this.fromOut = pose;
	this.toOut = toOut;
	this.timeIn = timeIn;
	this.timeOut = timeOut;
	this.inFn = inFn == null ? Interpolate.Eases.InOutCubic : inFn;
	this.outFn = outFn == null ? Interpolate.Eases.InOutCubic : outFn;
	timer = 0f;
}

public CInterpolate( float fromIn, float toIn, float fromOut, float toOut, float timeIn, float timeOut, Interpolate.Eases inFn, Interpolate.Eases outFn ){
	super();
	this.fromIn = interpolated = fromIn;
	this.toIn = toIn;
	this.fromOut = fromOut;
	this.toOut = toOut;
	this.timeIn = timeIn;
	this.timeOut = timeOut;
	this.inFn = inFn == null ? Interpolate.Eases.InOutCubic : inFn;
	this.outFn = outFn == null ? Interpolate.Eases.InOutCubic : outFn;
	timer = 0f;
}

@Override
protected void DoIn(){
	active = true;
}

@Override
protected void CancelIn(){
	timer = timeOut * ( 1 - ratio );
}

@Override
protected void DoOut(){
	active = true;
}

@Override
protected void CancelOut(){
	timer = timeIn * ( 1 - ratio );
}

@Override
public void update(){
	if ( !active )return;

	timer += Time.dt;
	switch ( State ) {
	case ExecutingIn:
		ratio = timer / timeIn;
		ratio = ratio > 1f ? 1f : ratio;
		interpolated = Interpolate.ease.ease( fromIn, toIn, ratio, inFn );
		if ( ratio == 1f ) {
			timer = 0f;
			ExecuteInComplete();
		}
		break;
	case ExecutingOut:
		ratio = timer / timeOut;
		ratio = ratio > 1f ? 1f : ratio;
		interpolated = Interpolate.ease.ease( fromIn, toIn, ratio, outFn );
		if ( ratio == 1f ) {
			timer = 0f;
			ExecuteOutComplete();
		}
		break;
	case ExecuteInComplete:
	case ExecuteOutComplete:
		active = false;
		timer = 0f;
		break;
	}
}

public float Value(){
	return interpolated;
}
}


package EmBoxUnity.Utils;

public class Interpolate{
public static enum Eases{
	Spring,
	Linear,
	InQuad,
	OutQuad,
	InOutQuad,
	InCubic,
	OutCubic,
	InOutCubic,
	InQuart,
	OutQuart,
	InOutQuart,
	InQuint,
	OutQuint,
	InOutQuint,
	Insine,
	Outsine,
	InOutsine,
	InExpo,
	OutExpo,
	InOutExpo,
	InCirc,
	OutCirc,
	InOutCirc,
	InBounce,
	OutBounce,
	InOutBounce,
	InBack,
	OutBack,
	InOutBack,
	InElastic,
	OutElastic,
	InOutElastic,
	zeroOnezero
}

public static IEase ease = new IEase(){
	float val;

	@Override
	public float ease( float start, float end, float val, Eases ease ){
		switch ( ease ) {
		case Spring:
			val = spring( start, end, val );
			break;
		case Linear:
			val = linear( start, end, val );
			break;
		case InQuad:
			val = inQuad( start, end, val );
			break;
		case OutQuad:
			val = outQuad( start, end, val );
			break;
		case InOutQuad:
			val = inOutQuad( start, end, val );
			break;
		case InCubic:
			val = inCubic( start, end, val );
			break;
		case OutCubic:
			val = outCubic( start, end, val );
			break;
		case InOutCubic:
			val = inOutCubic( start, end, val );
			break;
		case InQuart:
			val = inQuart( start, end, val );
			break;
		case OutQuart:
			val = outQuart( start, end, val );
			break;
		case InOutQuart:
			val = inOutQuart( start, end, val );
			break;
		case InQuint:
			val = inQuint( start, end, val );
			break;
		case OutQuint:
			val = outQuint( start, end, val );
			break;
		case InOutQuint:
			val = inOutQuint( start, end, val );
			break;
		case Insine:
			val = insine( start, end, val );
			break;
		case Outsine:
			val = outsine( start, end, val );
			break;
		case InOutsine:
			val = inOutsine( start, end, val );
			break;
		case InExpo:
			val = inExpo( start, end, val );
			break;
		case OutExpo:
			val = outExpo( start, end, val );
			break;
		case InOutExpo:
			val = inOutExpo( start, end, val );
			break;
		case InCirc:
			val = inCirc( start, end, val );
			break;
		case OutCirc:
			val = outCirc( start, end, val );
			break;
		case InOutCirc:
			val = inOutCirc( start, end, val );
			break;
		case InBounce:
			val = inBounce( start, end, val );
			break;
		case OutBounce:
			val = outBounce( start, end, val );
			break;
		case InOutBounce:
			val = inOutBounce( start, end, val );
			break;
		case InBack:
			val = inBack( start, end, val );
			break;
		case OutBack:
			val = outBack( start, end, val );
			break;
		case InOutBack:
			val = inOutBack( start, end, val );
			break;
		case InElastic:
			val = inElastic( start, end, val );
			break;
		case OutElastic:
			val = outElastic( start, end, val );
			break;
		case InOutElastic:
			val = inOutElastic( start, end, val );
			break;
		case zeroOnezero:
			val = zeroOnezero( start, end, val );
			break;
		}
		return val;
	}
};

public static float spring( float start, float end, float val ){
	//		val = Math.Clamp01( val );
	val = (float) ( ( Math.sin( val * Math.PI * ( 0.2f + 2.5f * val * val * val ) ) * Math.pow( 1f - val, 2.2f ) + val ) * ( 1f + ( 1.2f * ( 1f - val ) ) ) );
	return start + ( end - start ) * val;
}

public static float linear( float start, float end, float val ){
	end -= start;
	return end * val + start;
}

public static float inQuad( float start, float end, float val ){
	end -= start;
	return end * val * val + start;
}

public static float outQuad( float start, float end, float val ){
	end -= start;
	return -end * val * ( val - 2 ) + start;
}

public static float inOutQuad( float start, float end, float val ){
	val /= .5f;
	end -= start;
	if ( val < 1 ) return end / 2 * val * val + start;
	val--;
	return -end / 2 * ( val * ( val - 2 ) - 1 ) + start;
}

public static float inCubic( float start, float end, float val ){
	end -= start;
	return end * val * val * val + start;
}

public static float outCubic( float start, float end, float val ){
	val--;
	end -= start;
	return end * ( val * val * val + 1 ) + start;
}

public static float inOutCubic( float start, float end, float val ){
	val /= .5f;
	end -= start;
	if ( val < 1 ) return end / 2 * val * val * val + start;
	val -= 2;
	return end / 2 * ( val * val * val + 2 ) + start;
}

public static float inQuart( float start, float end, float val ){
	end -= start;
	return end * val * val * val * val + start;
}

public static float outQuart( float start, float end, float val ){
	val--;
	end -= start;
	return -end * ( val * val * val * val - 1 ) + start;
}

public static float inOutQuart( float start, float end, float val ){
	val /= .5f;
	end -= start;
	if ( val < 1 ) return end / 2 * val * val * val * val + start;
	val -= 2;
	return -end / 2 * ( val * val * val * val - 2 ) + start;
}

public static float inQuint( float start, float end, float val ){
	end -= start;
	return end * val * val * val * val * val + start;
}

public static float outQuint( float start, float end, float val ){
	val--;
	end -= start;
	return end * ( val * val * val * val * val + 1 ) + start;
}

public static float inOutQuint( float start, float end, float val ){
	val /= .5f;
	end -= start;
	if ( val < 1 ) return end / 2 * val * val * val * val * val + start;
	val -= 2;
	return end / 2 * ( val * val * val * val * val + 2 ) + start;
}

public static float insine( float start, float end, float val ){
	end -= start;
	return (float) ( -end * Math.cos( val / 1 * ( Math.PI / 2 ) ) + end + start );
}

public static float outsine( float start, float end, float val ){
	end -= start;
	return (float) ( end * Math.sin( val / 1 * ( Math.PI / 2 ) ) + start );
}

public static float inOutsine( float start, float end, float val ){
	end -= start;
	return (float) ( -end / 2 * ( Math.cos( Math.PI * val / 1 ) - 1 ) + start );
}

public static float inExpo( float start, float end, float val ){
	end -= start;
	return (float) ( end * Math.pow( 2, 10 * ( val / 1 - 1 ) ) + start );
}

public static float outExpo( float start, float end, float val ){
	end -= start;
	return (float) ( end * ( -Math.pow( 2, -10 * val / 1 ) + 1 ) + start );
}

public static float inOutExpo( float start, float end, float val ){
	val /= .5f;
	end -= start;
	if ( val < 1 ) return (float) ( end / 2 * Math.pow( 2, 10 * ( val - 1 ) ) + start );
	val--;
	return (float) ( end / 2 * ( -Math.pow( 2, -10 * val ) + 2 ) + start );
}

public static float inCirc( float start, float end, float val ){
	end -= start;
	return (float) ( -end * ( Math.sqrt( 1 - val * val ) - 1 ) + start );
}

public static float outCirc( float start, float end, float val ){
	val--;
	end -= start;
	return (float) ( end * Math.sqrt( 1 - val * val ) + start );
}

public static float inOutCirc( float start, float end, float val ){
	val /= .5f;
	end -= start;
	if ( val < 1 ) return (float) ( -end / 2 * ( Math.sqrt( 1 - val * val ) - 1 ) + start );
	val -= 2;
	return (float) ( end / 2 * ( Math.sqrt( 1 - val * val ) + 1 ) + start );
}

public static float inBounce( float start, float end, float val ){
	end -= start;
	float d = 1f;
	return end - outBounce( 0, end, d - val ) + start;
}

public static float outBounce( float start, float end, float val ){
	val /= 1f;
	end -= start;
	if ( val < ( 1 / 2.75f ) ) {
		return end * ( 7.5625f * val * val ) + start;
	} else if ( val < ( 2 / 2.75f ) ) {
		val -= ( 1.5f / 2.75f );
		return end * ( 7.5625f * ( val ) * val + .75f ) + start;
	} else if ( val < ( 2.5 / 2.75 ) ) {
		val -= ( 2.25f / 2.75f );
		return end * ( 7.5625f * ( val ) * val + .9375f ) + start;
	} else {
		val -= ( 2.625f / 2.75f );
		return end * ( 7.5625f * ( val ) * val + .984375f ) + start;
	}
}

public static float inOutBounce( float start, float end, float val ){
	end -= start;
	float d = 1f;
	if ( val < d / 2 ) return inBounce( 0, end, val * 2 ) * 0.5f + start;
	else return outBounce( 0, end, val * 2 - d ) * 0.5f + end * 0.5f + start;
}

public static float inBack( float start, float end, float val ){
	end -= start;
	val /= 1;
	float s = 1.70158f;
	return end * ( val ) * val * ( ( s + 1 ) * val - s ) + start;
}

public static float outBack( float start, float end, float val ){
	float s = 1.70158f;
	end -= start;
	val = ( val / 1 ) - 1;
	return end * ( ( val ) * val * ( ( s + 1 ) * val + s ) + 1 ) + start;
}

public static float inOutBack( float start, float end, float val ){
	float s = 1.70158f;
	end -= start;
	val /= .5f;
	if ( ( val ) < 1 ) {
		s *= ( 1.525f );
		return end / 2 * ( val * val * ( ( ( s ) + 1 ) * val - s ) ) + start;
	}
	val -= 2;
	s *= ( 1.525f );
	return end / 2 * ( ( val ) * val * ( ( ( s ) + 1 ) * val + s ) + 2 ) + start;
}

public static float inElastic( float start, float end, float val ){
	end -= start;

	float d = 1f;
	float p = d * .3f;
	float s = 0;
	float a = 0;

	if ( val == 0 ) return start;
	val = val / d;
	if ( val == 1 ) return start + end;

	if ( a == 0f || a < Math.abs( end ) ) {
		a = end;
		s = p / 4;
	} else {
		s = (float) ( p / ( 2 * Math.PI ) * Math.asin( end / a ) );
	}
	val = val - 1;
	return (float) ( -( a * Math.pow( 2, 10 * val ) * Math.sin( ( val * d - s ) * ( 2 * Math.PI ) / p ) ) + start );
}

public static float outElastic( float start, float end, float val ){
	end -= start;

	float d = 1f;
	float p = d * .3f;
	float s = 0;
	float a = 0;

	if ( val == 0 ) return start;

	val = val / d;
	if ( val == 1 ) return start + end;

	if ( a == 0f || a < Math.abs( end ) ) {
		a = end;
		s = p / 4;
	} else {
		s = (float) ( p / ( 2 * Math.PI ) * Math.asin( end / a ) );
	}

	return (float) ( a * Math.pow( 2, -10 * val ) * Math.sin( ( val * d - s ) * ( 2 * Math.PI ) / p ) + end + start );
}

public static float inOutElastic( float start, float end, float val ){
	end -= start;

	float d = 1f;
	float p = d * .3f;
	float s = 0;
	float a = 0;

	if ( val == 0 ) return start;

	val = val / ( d / 2 );
	if ( val == 2 ) return start + end;

	if ( a == 0f || a < Math.abs( end ) ) {
		a = end;
		s = p / 4;
	} else {
		s = (float) ( p / ( 2 * Math.PI ) * Math.asin( end / a ) );
	}

	if ( val < 1 ) {
		val = val - 1;
		return (float) ( -0.5f * ( a * Math.pow( 2, 10 * val ) * Math.sin( ( val * d - s ) * ( 2 * Math.PI ) / p ) ) + start );
	}
	val = val - 1;
	return (float) ( a * Math.pow( 2, -10 * val ) * Math.sin( ( val * d - s ) * ( 2 * Math.PI ) / p ) * 0.5f + end + start );
}

public static float zeroOnezero( float start_end, float peak, float val ){
	val = inOutCubic( 0,1f,val );
	peak -= start_end;
	val *= peak * 2f;
	if(val>peak) val = peak - (val- peak);
	return val + start_end;
}
}

package edu.virginia.engine.tween;

public class TweenTransitions {
	
	
	public double applyTransition(double percentDone, TweenTransitionIndex transition){
		switch (transition){
			case LINEAR:
				return linear(percentDone);
			case QUAD:
				return quad(percentDone);
			case INVERSE_QUAD:
				return inverseQuad(percentDone);
		}
		return -1;
	}
	
	public double quad(double percentDone){
		return percentDone*percentDone;
	}

	public double inverseQuad(double percentDone) { return Math.sqrt(percentDone); }
	
	public double linear(double percentDone){
		return percentDone;
	}
	
	public double inNOut(double percentDone){
		double a = Math.pow(percentDone, 4);
		double b = Math.pow(percentDone, 3);
		return Math.abs(a-b);
	}
	
}

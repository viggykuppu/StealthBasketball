package edu.virginia.engine.tween;

public class TweenTransitions {
	
	
	public double applyTransition(double percentDone, int index){
		switch (index){
			case 0:
				return linear(percentDone);
			case 1:
				return quad(percentDone);
		}
		return -1;
	}
	
	public double quad(double percentDone){
		return percentDone*percentDone;
	}
	
	public double linear(double percentDone){
		return percentDone;
	}
	
	public double inNOut(double percentDone){
		double a = Math.pow(percentDone, 4);
		double b = Math.pow(percentDone, 3);
		return Math.abs(a-b);
	}
	
}

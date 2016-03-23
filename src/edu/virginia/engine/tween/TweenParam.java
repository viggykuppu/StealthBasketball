package edu.virginia.engine.tween;

import static edu.virginia.engine.tween.TweenTransitionIndex.LINEAR;

public class TweenParam {
	private TweenableParams paramToTween;
	private double startVal;
	private double currentVal;
	private double endVal;
	private long time;
	private TweenTransitionIndex transitionIndex;
	
	public TweenParam(TweenableParams paramToTween, double startVal, double endVal, long timems){
		this.paramToTween = paramToTween;
		this.startVal = startVal;
		this.currentVal = startVal;
		this.endVal = endVal;
		this.time = timems*1000*1000;
		this.transitionIndex = LINEAR;
	}

	public TweenParam(TweenableParams paramToTween, double startVal, double endVal, long timems, TweenTransitionIndex transition){
		this.paramToTween = paramToTween;
		this.startVal = startVal;
		this.currentVal = startVal;
		this.endVal = endVal;
		this.time = timems*1000*1000;
		this.transitionIndex = transition;
	}
	
	public TweenableParams getParam(){ return this.paramToTween; }
	
	public double getStartVal(){
		return this.startVal;
	}
	
	public double getEndVal(){
		return this.endVal;
	}
	
	public long getTweenTime(){
		return this.time;
	}
	
	public double getCurrentValue(){
		return this.currentVal;
	}
	
	public void setCurrentValue(double currentVal){
		this.currentVal = currentVal;
	}

	public TweenTransitionIndex getTransitionIndex(){ return this.transitionIndex; }
}

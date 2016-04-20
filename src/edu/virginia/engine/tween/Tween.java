package edu.virginia.engine.tween;

import edu.virginia.engine.display.BallSprite;
import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.PlayerPingEffect;
import edu.virginia.engine.events.EventDispatcher;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static edu.virginia.engine.tween.TweenTransitionIndex.LINEAR;

public class Tween extends EventDispatcher{
	private DisplayObject object;
	private long startTime;
	private Map<TweenableParams,TweenParam> params = new HashMap<>();
	private TweenTransitions transition;
	private boolean complete = true;
	
	public Tween(DisplayObject object) {
		this.object = object;
		this.transition = new TweenTransitions();
		this.startTime = -1;
	}
	
	public void animate(TweenableParams fieldToAnimate, double startVal, double endVal, long timems){
		complete = false;
		TweenParam tp = new TweenParam(fieldToAnimate,startVal,endVal,timems);
		params.put(fieldToAnimate, tp);
	}
	
	public void processTween(TweenParam tp){
		TweenableParams fieldToAnimate = tp.getParam();
		double startVal = tp.getStartVal();
		double endVal = tp.getEndVal();
		long time = tp.getTweenTime();
		double val = endVal - startVal;
		val = getPercentDone(time,tp.getTransitionIndex())*val+startVal;
		switch (fieldToAnimate){
			case SCALE_X:
				object.setScaleX(val);
				break;
			case SCALE_Y:
				object.setScaleY(val);
				break;
			case ROTATE:
				object.setRotation(val);
				break;
			case ALPHA:
				object.setAlpha((float)val);
				break;
			case X:
				object.setPosition(new Point((int)val,object.getPosition().y));
				break;
			case Y:
				object.setPosition(new Point(object.getPosition().x,(int)val));
				break;
			case PING_RADIUS:
				((PlayerPingEffect)object).setRadius((int)val);
				break;
			case BALL_DRIBBLE:
				BallSprite ball = (BallSprite)object;
				System.out.println(val);
				ball.setDuck((int)val);
				break;
		}
	}
	
	public double getPercentDone(long time, TweenTransitionIndex transitionIndex){
		long dT = System.nanoTime()-this.startTime;
		if(dT>time){
			dT = time;
			this.complete = true;
			TweenEvent endTween = new TweenEvent("tweenEnd",this);
			this.dispatchEvent(endTween);
		}
		double percent = (double)dT/(double)time;
		return this.transition.applyTransition(percent, transitionIndex);
	}
	
	public void update(){
		if(startTime == -1){
			startTime = System.nanoTime();
			TweenEvent startTween = new TweenEvent("tweenStart",this);
			this.dispatchEvent(startTween);
		}
		for(TweenParam tp : params.values()){
			this.processTween(tp);
		}
	}

	public DisplayObject getDisplayObject(){
		return object;
	}

	public boolean isComplete(){
		return this.complete;
	}
	
	public void setValue(TweenableParams param, double value){
		this.params.get(param).setCurrentValue(value);
	}
}

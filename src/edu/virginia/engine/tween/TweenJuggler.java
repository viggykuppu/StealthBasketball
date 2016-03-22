package edu.virginia.engine.tween;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;

import java.util.ArrayList;

public class TweenJuggler implements IEventListener{
	ArrayList<Tween> tweens;
	private static TweenJuggler instance = new TweenJuggler();
	
	protected TweenJuggler(){
		tweens = new ArrayList<>();
	}
	
	public static TweenJuggler getInstance(){
		return instance;
	}
	
	public void addTween(Tween t){
		tweens.add(t);
		t.addEventListener(this, "tweenEnd");
	}

	public void addTweenNonRedundant(Tween t, DisplayObject o){
		removeTweensByObject(o);
		tweens.add(t);
		t.addEventListener(this,"tweenEnd");
	}

	void removeTweensByObject(DisplayObject displayObject){
		for (int i = tweens.size() - 1; i >= 0; i--){
			if (tweens.get(i).getDisplayObject() == displayObject)
				tweens.remove(i);
		}
	}
	
	public void nextFrame(){
		for(int i = 0; i < tweens.size(); i++){
			tweens.get(i).update();
		}
	}

	@Override
	public void handleEvent(Event event) {
		if (event.getEventType() == "tweenEnd")
			this.tweens.remove(event.getSource());
	}

	public String getId() {
		return "Juggler";
	}
}

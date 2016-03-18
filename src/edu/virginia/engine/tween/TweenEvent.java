package edu.virginia.engine.tween;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventDispatcher;

public class TweenEvent extends Event{
	
	public TweenEvent(String eventType, IEventDispatcher source) {
		super(eventType, source);
		// TODO Auto-generated constructor stub
	}
	
	public Tween getTween(){
		return (Tween)this.getSource();
	}

}

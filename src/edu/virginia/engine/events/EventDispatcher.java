package edu.virginia.engine.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventDispatcher implements IEventDispatcher{
	private Map<String,ArrayList<IEventListener>> lookup;
	
	@Override
	public void addEventListener(IEventListener listener, String eventType) {
		if(lookup == null){
			lookup = new HashMap<String,ArrayList<IEventListener>>();
		}
		if(!lookup.containsKey(eventType)){
			ArrayList<IEventListener> temp = new ArrayList<IEventListener>();
			temp.add(listener);
			lookup.put(eventType, temp);
		} else {
			lookup.get(eventType).add(listener);
		}
		
	}

	@Override
	public void removeEventListener(IEventListener listener, String eventType) {
		if(this.hasEventListener(listener, eventType)){
			lookup.get(eventType).remove(listener);
		}		
	}

	@Override
	public void dispatchEvent(Event event) {
		ArrayList<IEventListener> removed = new ArrayList<IEventListener>();
		if(lookup != null && lookup.containsKey(event.eventType)){
			for(IEventListener l : lookup.get(event.getEventType())){
				l.handleEvent(event);
				removed.add(l);
			}
			for(IEventListener l : removed){
				this.removeEventListener(l, event.getEventType());
			}
		}
	}

	@Override
	public boolean hasEventListener(IEventListener listener, String eventType) {
		if(lookup.containsKey(eventType)){
			if(lookup.get(eventType) == null){
				return false;
			}
			if(lookup.get(eventType).contains(listener)){
				return true;
			}
		}
		return false;		
	}

}

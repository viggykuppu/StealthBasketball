package edu.virginia.engine.events;

public class Quest extends Event{

	public Quest(String eventType, IEventDispatcher source) {
		super(eventType, source);
		// TODO Auto-generated constructor stub
	}
	
	public void finishedQuest(){
		System.out.println("You finished quest: " + eventType);
	}
	
	

}

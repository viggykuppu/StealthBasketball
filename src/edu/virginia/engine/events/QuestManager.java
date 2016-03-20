package edu.virginia.engine.events;

import java.util.ArrayList;

public class QuestManager implements IEventListener{
	ArrayList<Quest> quests = new ArrayList<Quest>();
	
	public void addQuest(Quest quest){
		this.quests.add(quest);
	}
	
	public void removeQuest(Quest quest){
		this.quests.remove(quest);
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		for(Quest q : quests){
			if(event.eventType.equals(q.eventType)){
				q.finishedQuest();
			}
		}
	}

	public String getId(){
		return "questManager";
	}
}

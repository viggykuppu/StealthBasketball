package edu.virginia.test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.events.Quest;
import edu.virginia.engine.events.QuestManager;
import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.tween.TweenableParams;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Example game that utilizes our engine. We can create a simple prototype game with just a couple lines of code
 * although, for now, it won't be a very fun game :)
 * */
public class GameTest extends Game implements IEventListener{
	
	/**
	 * Constructor. See constructor in Game.java for details on the parameters given
	 * */
	public GameTest() {
		super("Test Game", 1000, 500);

	}
	
	/**
	 * Engine will automatically call this update method once per frame and pass to us
	 * the set of keys (as strings) that are currently being pressed down
	 * */
	@Override
	public void update(ArrayList<String> pressedKeys){
		super.update(pressedKeys);
		/* Make sure mario is not null. Sometimes Swing can auto cause an extra frame to go before everything is initialized */
		for(String dir : pressedKeys){
			if(dir.equals(KeyEvent.getKeyText(KeyEvent.VK_UP))){

			}
			if(dir.equals(KeyEvent.getKeyText(KeyEvent.VK_DOWN))){

			}
			if(dir.equals(KeyEvent.getKeyText(KeyEvent.VK_LEFT))){

			}
			if(dir.equals(KeyEvent.getKeyText(KeyEvent.VK_RIGHT))){

			}
		}
	}
	
	/**
	 * Engine automatically invokes draw() every frame as well. If we want to make sure mario gets drawn to
	 * the screen, we need to make sure to override this method and call mario's draw method.
	 * */
	@Override
	public void draw(Graphics g){
		super.draw(g);
	}

	/**
	 * Quick main class that simply creates an instance of our game and starts the timer
	 * that calls update() and draw() every frame
	 * */
	public static void main(String[] args) {
		GameTest game = new GameTest();
		game.start();

	}

	@Override
	public void handleEvent(Event event) {
		
	}
}

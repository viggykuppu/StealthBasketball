package edu.virginia.test;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.DisplayObjectContainer;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.PhysicsSprite;
import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.events.Quest;
import edu.virginia.engine.events.QuestManager;
import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.tween.TweenableParams;

/**
 * Example game that utilizes our engine. We can create a simple prototype game with just a couple lines of code
 * although, for now, it won't be a very fun game :)
 * */
public class GameTest extends Game implements IEventListener{
	/* Create a sprite object for our game. We'll use mario */
//	Sprite mario = new Sprite("Mario", "Mario.png");
	PhysicsSprite mario = new PhysicsSprite("Mario","mario.png");
	Sprite coin = new Sprite("Coin","coin.gif");
	DisplayObjectContainer floors = new DisplayObjectContainer("floor container");
	Sprite floor1 = new Sprite("Floor","platform.png");
	Sprite floor2 = new Sprite("Floor","platform.png");
	Sprite floor3 = new Sprite("Floor","platform.png");
	Tween coinTween;
	QuestManager qM = new QuestManager();
	TweenJuggler tJ;
	boolean once = true;

	/**
	 * Constructor. See constructor in Game.java for details on the parameters given
	 * */
	public GameTest() {
		super("Lab One Test Game", 1000, 500);
		mario.setPosition(new Point(50,210));
		mario.giveGravity();
		coin.setPosition(new Point(700,300-126));
		coin.setPivotPoint(new Point(28,28));


		mario.setPivotPoint(new Point(mario.getUnscaledWidth()/2,mario.getUnscaledHeight()/2));
		floor1.setPosition(new Point(0,439));
		floor2.setPosition(new Point(500,439));
		floor3.setPosition(new Point(400,200));
		floors.addChild(floor1);
		floors.addChild(floor2);
		floors.addChild(floor3);
		this.addChild(floors);
		this.addChild(mario);
		this.addChild(coin);

		tJ = TweenJuggler.getInstance();
		Tween marioTween = new Tween(mario);
		marioTween.animate(TweenableParams.ALPHA, 0, 1, 1);
		tJ.addTween(marioTween);



		Quest q = new Quest("grab the coin!",coin);
		qM.addQuest(q);
		coin.addEventListener(qM, "grab the coin!");
	}

	/**
	 * Engine will automatically call this update method once per frame and pass to us
	 * the set of keys (as strings) that are currently being pressed down
	 * */
	@Override
	public void update(ArrayList<String> pressedKeys){
		super.update(pressedKeys);

		/* Make sure mario is not null. Sometimes Swing can auto cause an extra frame to go before everything is initialized */
		if(mario != null){
			tJ.nextFrame();
			mario.update(pressedKeys);
			for(String dir : pressedKeys){
				if(dir.equals(KeyEvent.getKeyText(KeyEvent.VK_UP))){
					mario.setvX(2);
				}
				if(dir.equals(KeyEvent.getKeyText(KeyEvent.VK_DOWN))){
					//Do nothing for now
				}
				if(dir.equals(KeyEvent.getKeyText(KeyEvent.VK_LEFT))){
					mario.setvX(-2);
					mario.animate("walkL");
				}
				if(dir.equals(KeyEvent.getKeyText(KeyEvent.VK_RIGHT))){
					mario.setvX(2);
					mario.animate("walkR");
				}
			}
			if(pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_Z))){
				mario.setRotation(mario.getRotation()-1);
			}
			if(pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_X))){
				mario.setRotation(mario.getRotation()+1);
			}
			if(pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_W))){
				mario.setScaleY(mario.getScaleY()+0.1);
			}
			if(pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_S))){
				mario.setScaleY(mario.getScaleY()-0.1);
			}
			if(pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_A))){
				mario.setScaleX(mario.getScaleX()-0.1);
			}
			if(pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_D))){
				mario.setScaleX(mario.getScaleX()+0.1);
			}
			if(pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_I))){
				mario.setVisible(!mario.isVisible());
			}
			if(pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_E))){
				mario.setAlpha(mario.getAlpha()+0.05f);
			}
			if(pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_Q))){
				mario.setAlpha(mario.getAlpha()-0.05f);
			}
			if(pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_SPACE)) && !mario.jumping){
				mario.setaY(-13);
				mario.animate("jump");
				mario.jumping = true;
			}
//			mario.setRotation(mario.getRotation()+1);
			if(coin.collidesWith(mario) && once){
				once = false;
				coinTween = new Tween(coin,1);
				coinTween.animate(TweenableParams.SCALE_X, 1, 2, 1);
				coinTween.animate(TweenableParams.SCALE_Y, 1, 2, 1);
				coinTween.animate(TweenableParams.X, coin.getPosition().x, 500, 1);
				coinTween.animate(TweenableParams.Y, coin.getPosition().y, 250, 1);
				coinTween.addEventListener(this, "tweenEnd");
				tJ.addTween(coinTween);
				coin.dispatchEvent(new Event("grab the coin!",coin));
			}
			for(DisplayObject d : floors.getChildren()){
				if(mario.collidesWith(d)){
					mario.jumping = false;
					mario.place(d);
				}
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
		Graphics2D g2d = (Graphics2D) g;
//		if(mario != null && coin !=null){
//			g2d.draw(mario.getHitbox());
//			g2d.draw(coin.getHitbox());
//		}


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
		if(event.getEventType().equals("tweenEnd") && event.getSource().equals(coinTween)){
			Tween coinRemoveTween = new Tween(coin,1);
			coinRemoveTween.animate(TweenableParams.ALPHA,1,0,1);
			tJ.addTween(coinRemoveTween);
		}

	}
}

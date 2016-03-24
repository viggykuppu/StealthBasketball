package edu.virginia.test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.util.Direction;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/19/2016.
 */
public class StealthBasketball extends Game {

    BallSprite ball = new BallSprite("Ball", "coin.gif");
    PlayerSprite player = new PlayerSprite("Player", "mario.png", ball);
    GridGuardSprite guard = new GridGuardSprite("Guard", "floryan,mark.png",player);

    //Instantiate all sprites prior to the nullChecker
    Sprite nullChecker = new Sprite("nullChecker", "coin.gif");

    public StealthBasketball() {
        super("Stealth Basketball!", 1007, 530);

        player.setPivotPoint(new Point(player.getUnscaledWidth() / 2, player.getUnscaledHeight() / 2));
        ball.setPivotPoint(new Point(28, 28));
        guard.setPivotPoint(new Point(34, 46));

        GridManager.getInstance().setGridSize(10, 5, 1000, 500);
        GridManager.getInstance().addToGrid(player, 9, 4);
        GridManager.getInstance().addToGrid(guard, 0, 0);
        for(int i = 0; i < 9; i++) {
            GridManager.getInstance().addWall(new Point(i, 0), Direction.DOWN);
        }
        GridManager.getInstance().addWall(new Point(9,1),Direction.LEFT);
        GridManager.getInstance().startTurns();

        ball.setPosition(new Point (player.getPosition().x+ball.getPlayerOffset().x,player.getPosition().y+ball.getPlayerOffset().y));
    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as strings) that are currently being pressed down
     */
    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys) {
        if (nullChecker != null) {
            GridManager.getInstance().update(pressedKeys,heldKeys);
            TweenJuggler.getInstance().nextFrame();
        }
    }

    /**
     * Engine automatically invokes draw() every frame as well. If we want to make sure mario gets drawn to
     * the screen, we need to make sure to override this method and call mario's draw method.
     */
    @Override
    public void draw(Graphics g) {
        super.draw(g);
        if (nullChecker != null) {
            GridManager.getInstance().draw(g);
        }
    }


    public static void main(String[] args) {
        StealthBasketball game = new StealthBasketball();
        game.start();
    }

}

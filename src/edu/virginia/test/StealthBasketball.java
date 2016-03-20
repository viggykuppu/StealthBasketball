package edu.virginia.test;

import edu.virginia.engine.display.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/19/2016.
 */
public class StealthBasketball extends Game {

    Sprite nullChecker = new Sprite("nullChecker","coin.gif");//Why do we even need this shit
    PlayerSprite player = new PlayerSprite("Player","mario.png");

    public StealthBasketball(){
        super("Stealth Basketball!",1007,530);

        player.setPivotPoint(new Point(player.getUnscaledWidth()/2,player.getUnscaledHeight()/2));

        GridManager.getInstance().setGridSize(10,5,1000,500);
        //GridManager.getInstance().centerGridPointOnScreen(9,4,1000,500);
        GridManager.getInstance().addToGrid(player,9,4);
        GridManager.getInstance().startTurns();
    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as strings) that are currently being pressed down
     * */
    @Override
    public void update(ArrayList<String> pressedKeys){
        GridManager.getInstance().update(pressedKeys);
    }

    /**
     * Engine automatically invokes draw() every frame as well. If we want to make sure mario gets drawn to
     * the screen, we need to make sure to override this method and call mario's draw method.
     * */
    @Override
    public void draw(Graphics g){
        super.draw(g);
        if (nullChecker != null)
            GridManager.getInstance().draw(g);
    }

    public static void main(String[] args) {
        StealthBasketball game = new StealthBasketball();
        game.start();
    }

}

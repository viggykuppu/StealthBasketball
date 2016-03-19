package edu.virginia.test;

import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.GridManager;
import edu.virginia.engine.display.Sprite;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/19/2016.
 */
public class StealthBasketball extends Game {

    Sprite nullChecker = new Sprite("nullChecker","coin.gif");//Why do we even need this shit
    Sprite coin = new Sprite("Coin","coin.gif");

    public StealthBasketball(){
        super("Stealth Basketball!",1007,530);

        coin.setPivotPoint(new Point(28,28));

        GridManager.getInstance().setGridSize(10,5,1000,500);
        //GridManager.getInstance().centerGridPointOnScreen(9,4,1000,500);
        GridManager.getInstance().addToGrid(coin,9,4);
    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as strings) that are currently being pressed down
     * */
    @Override
    public void update(ArrayList<String> pressedKeys){

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

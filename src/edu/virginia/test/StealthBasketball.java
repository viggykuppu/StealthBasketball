package edu.virginia.test;

import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.GridManager;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/19/2016.
 */
public class StealthBasketball extends Game {

    public StealthBasketball(){
        super("Stealth Basketball!",1000,500);
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
    }

    public static void main(String[] args) {
        StealthBasketball game = new StealthBasketball();
        game.start();
    }

}

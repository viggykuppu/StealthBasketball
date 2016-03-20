package edu.virginia.engine.display;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/20/2016.
 */
public class PlayerSprite extends GridSprite {

    public PlayerSprite(String id, String imageFileName) {
        super(id, imageFileName);
    }

    public PlayerSprite(String id, String imageFileName, Sprite myBall) {
        super(id, imageFileName);
    }

    @Override
    public void update(ArrayList<String> pressedKeys) {
        super.update(pressedKeys);
    }

    @Override
    public void gridTurnUpdate(ArrayList<String> activeKeyPresses){
        System.out.println(activeKeyPresses);

        //Player movement code
        if (activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_UP))) {
            moveOnGrid(0,-1);
        }
        if (activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_DOWN))) {
            moveOnGrid(0,1);
        }
        if (activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_LEFT))) {
            moveOnGrid(-1,0);
        }
        if (activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_RIGHT))) {
            moveOnGrid(1,0);
        }
    }
}

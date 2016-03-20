package edu.virginia.engine.display;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/20/2016.
 */
public class PlayerSprite extends GridSprite {

    BallSprite myBall;
    PlayerState state = PlayerState.NEUTRAL;

    public PlayerSprite(String id, String imageFileName) {
        super(id, imageFileName);
    }

    public PlayerSprite(String id, String imageFileName, BallSprite myBall) {
        super(id, imageFileName);
        this.myBall = myBall;
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);
    }

    @Override
    public void gridTurnUpdate(ArrayList<Integer> activeKeyPresses) {

        if (state == PlayerState.DunkingUp) {
            if (GridManager.getInstance().getSpriteAtGridPoint(new Point(gridPosition.x,gridPosition.y-1)).getId() == "Guard"){
                //Event code here
            }
            state = PlayerState.NoBall;
            //myBall.goFetch();
        }else if (state == PlayerState.DunkingDown){
            if (GridManager.getInstance().getSpriteAtGridPoint(new Point(gridPosition.x,gridPosition.y+1)).getId() == "Guard"){
                //Event code here
            }
            state = PlayerState.NoBall;
            //myBall.goFetch();
        }else if (state == PlayerState.DunkingLeft){
            if (GridManager.getInstance().getSpriteAtGridPoint(new Point(gridPosition.x-1,gridPosition.y)).getId() == "Guard"){
                //Event code here
            }
            state = PlayerState.NoBall;
            //myBall.goFetch();
        }else if (state == PlayerState.DunkingUp){
            if (GridManager.getInstance().getSpriteAtGridPoint(new Point(gridPosition.x+1,gridPosition.y)).getId() == "Guard"){
                //Event code here
            }
            state = PlayerState.NoBall;
            //myBall.goFetch();
        } else {
            
            if (activeKeyPresses.contains(KeyEvent.VK_Z)) {

                if (activeKeyPresses.contains(KeyEvent.VK_UP)) {
                    state = PlayerState.DunkingUp;
                    moveOnGrid(0, -1);
                }
                if (activeKeyPresses.contains(KeyEvent.VK_DOWN)) {
                    state = PlayerState.DunkingDown;
                    moveOnGrid(0, 1);
                }
                if (activeKeyPresses.contains(KeyEvent.VK_LEFT)) {
                    state = PlayerState.DunkingLeft;
                    moveOnGrid(-1, 0);
                }
                if (activeKeyPresses.contains(KeyEvent.VK_RIGHT)) {
                    state = PlayerState.DunkingRight;
                    moveOnGrid(1, 0);
                }

            } else {//Player movement code
                if (activeKeyPresses.contains(KeyEvent.VK_UP)) {
                    moveOnGrid(0, -1);
                }
                if (activeKeyPresses.contains(KeyEvent.VK_DOWN)) {
                    moveOnGrid(0, 1);
                }
                if (activeKeyPresses.contains(KeyEvent.VK_LEFT)) {
                    moveOnGrid(-1, 0);
                }
                if (activeKeyPresses.contains(KeyEvent.VK_RIGHT)) {
                    moveOnGrid(1, 0);
                }
            }
        }
    }
}

enum PlayerState {
    NEUTRAL, DunkingLeft, DunkingRight, DunkingUp, DunkingDown, NoBall, THROWING
}

package edu.virginia.engine.display;

import edu.virginia.engine.util.Direction;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/20/2016.
 */
public class PlayerSprite extends GridSprite {

    BallSprite myBall;
    PlayerState state = PlayerState.NEUTRAL;

    boolean movedThisTurn = false;
    boolean dunkKeyed = false;
    Direction dunkDir;
    GridManager gridManager;

    private enum PlayerState {
        NEUTRAL, DUNKING, NoBall, THROWING
    }

    public PlayerSprite(String id, String imageFileName) {
        super(id, imageFileName);
    }

    public PlayerSprite(String id, String imageFileName, BallSprite myBall) {
        super(id, imageFileName);
        gridManager = GridManager.getInstance();
        this.myBall = myBall;
        gridManager.setPlayer(this);
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys) {
        super.update(pressedKeys, heldKeys);

        if (state == PlayerState.NEUTRAL) {
            if (heldKeys.contains(KeyEvent.VK_Z)) {
                dunkKeyed = true;
            } else
                dunkKeyed = false;

            if (!dunkKeyed) {//Normal Movement

                if (pressedKeys.contains(KeyEvent.VK_UP)) {
                    if (moveOnGrid(0, -1, 500)) {
                        myBall.pathToGridPoint(gridPosition, 500);
                        movedThisTurn = true;
                    }
                } else if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
                    if (moveOnGrid(0, 1, 500)) {
                        myBall.pathToGridPoint(gridPosition, 500);
                        movedThisTurn = true;
                    }
                } else if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
                    if (moveOnGrid(-1, 0, 500)) {
                        myBall.pathToGridPoint(gridPosition, 500);
                        movedThisTurn = true;
                    }
                } else if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                    if (moveOnGrid(1, 0, 500)) {
                        myBall.pathToGridPoint(gridPosition, 500);
                        movedThisTurn = true;
                    }
                }
            } else {//Dunking action

                if (pressedKeys.contains(KeyEvent.VK_UP)) {
                    if (moveOnGrid(0, -1, 500)) {
                        myBall.pathToGridPoint(new Point(gridPosition.x, gridPosition.y - 2), 500);
                        movedThisTurn = true;
                        state = PlayerState.NoBall;
                    }
                } else if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
                    if (moveOnGrid(0, 1, 500)) {
                        myBall.pathToGridPoint(new Point(gridPosition.x, gridPosition.y + 2), 500);
                        movedThisTurn = true;
                        state = PlayerState.NoBall;
                    }
                } else if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
                    if (moveOnGrid(-1, 0, 500)) {
                        myBall.pathToGridPoint(new Point(gridPosition.x - 2, gridPosition.y), 500);
                        movedThisTurn = true;
                        state = PlayerState.NoBall;
                    }
                } else if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                    if (moveOnGrid(1, 0, 500)) {
                        myBall.pathToGridPoint(new Point(gridPosition.x + 2, gridPosition.y), 500);
                        movedThisTurn = true;
                        state = PlayerState.NoBall;
                    }
                }
            }
        } else if (state == PlayerState.NoBall) {

            if (pressedKeys.contains(KeyEvent.VK_UP)) {
                moveOnGrid(0, -1, 500);
                movedThisTurn = true;
            } else if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
                moveOnGrid(0, 1, 500);
                movedThisTurn = true;
            } else if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
                moveOnGrid(-1, 0, 500);
                movedThisTurn = true;
            } else if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                moveOnGrid(1, 0, 500);
                movedThisTurn = true;
            }
        }

    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        myBall.draw(g);
        Rectangle ballBox = myBall.getHitCircle().getBounds();
        g.drawOval(ballBox.x, ballBox.y, ballBox.width, ballBox.height);
    }

    @Override
    public void gridTurnUpdate() {
        movedThisTurn = false;
    }

    public BallSprite getBall() {
        return myBall;
    }
}

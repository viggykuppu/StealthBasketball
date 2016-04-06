package edu.virginia.engine.display;

import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.tween.TweenTransitions;
import edu.virginia.engine.tween.TweenableParams;
import edu.virginia.engine.util.Direction;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/20/2016.
 */
public class BallSprite extends PhysicsSprite {

    Point playerOffset = new Point(0,-25);
    Tween ballFollowPlayer;
    GridManager gridManager;
    static int VELOCITY = 5;
    boolean didCollide;
    boolean prevCollide; // this boolean exists solely to manage shouldCollide in loops
    //static int DEACCEL = -1;

    public BallSprite(String id, String imageFileName) {

        super(id, imageFileName);
        gridManager = GridManager.getInstance();
        didCollide = false; // boolean to prevent double collisions
        prevCollide = false;
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys) {
        super.update(pressedKeys,heldKeys);
        didCollide = false;
        ArrayList<DisplayObject> spriteList = gridManager.getChildren();
        for (DisplayObject obj : spriteList) {
            GridSprite s = (GridSprite) obj;
            if (this.collidesWith(s)) {
                // collision confirmed
                didCollide = true;
                // did prev frame not have a collision?
                if (prevCollide == false) {
                    if (s.getId() == "Player") {
                        PlayerSprite player = (PlayerSprite) s;
                        if (player.getState() != PlayerSprite.PlayerState.NEUTRAL) {
                            this.setvX(0);
                            this.setvY(0);
                            player.setState(PlayerSprite.PlayerState.NEUTRAL);
                        }
                    } else if (s.getId() == "Guard") {
                        System.out.println("Guard hit!");
                        Direction reflection = this.getCollisionNormal(s);
                        this.reflect(reflection);
                    } else {
                        // Probably hit a wall
                        Direction reflection = this.getCollisionNormal(s);
                        this.reflect(reflection);
                    }
                }
            }
        }
        // only if there is a frame where there is no collisions should this reset
        prevCollide = didCollide;
    }


    public void pathToGridPoint(Point gridDest, long timems) {
        Point endPosition = new Point(gridDest);
        endPosition = GridManager.getInstance().gridtoGamePoint(endPosition);
        endPosition.x += playerOffset.x;
        endPosition.y += playerOffset.y;

        ballFollowPlayer = new Tween(this);
        ballFollowPlayer.animate(TweenableParams.X,getPosition().x,endPosition.x,timems);
        ballFollowPlayer.animate(TweenableParams.Y,getPosition().y,endPosition.y,timems);
        TweenJuggler.getInstance().addTweenNonRedundant(ballFollowPlayer,this);
    }


    /*
        Gets the current position and gives the ball physics
     */
    public void dunk(Direction vector) {

        switch (vector) {
            case UP:
                this.setvY(-VELOCITY);
                //this.setaX(-DEACCEL);
                break;
            case DOWN:
                this.setvY(VELOCITY);
                //this.setaY(DEACCEL);
                break;
            case LEFT:
                this.setvX(-VELOCITY);
                //this.setaX(-DEACCEL);
                break;
            case RIGHT:
                this.setvX(VELOCITY);
                //this.setaY(DEACCEL);
                break;
        }
    }

    /*
        Calculates which rectangle side the ball is closest to
        Returns: Direction Enum
     */
    public Direction getCollisionNormal(GridSprite s) {
        Rectangle area = s.getHitbox().getBounds();

        // the sides of the rectangle
        double left = area.getX();
        double right = left + area.getWidth();
        double up = area.getY();
        double down = up + area.getHeight();

        // get the closest side of the rectangle
        Rectangle ball = this.getHitCircle().getBounds();

        // get the center of ball
        double centerX = ball.getX() + (ball.getWidth() / 2);
        double centerY = ball.getY() + (ball.getHeight() / 2);

        // do the math
        double distLeft = Math.abs(left - centerX);
        double distRight = Math.abs(right - centerX);
        double distUp = Math.abs(up - centerY);
        double distDown = Math.abs(down - centerY);

        Direction ret = Direction.LEFT;
        double smallest = distLeft;

        if (distRight < smallest) {
            ret = Direction.RIGHT;
            smallest = distRight;
        }

        if (distUp < smallest) {
            ret = Direction.UP;
            smallest = distUp;
        }

        if (distDown < smallest) {
            ret = Direction.DOWN;
        }

        return ret;
    }

    public void reflect(Direction reflection) {
        switch (reflection) {
            case UP:
                this.setvY(-this.getvY());
                break;
            case DOWN:
                this.setvY(-this.getvY());
                break;
            case LEFT:
                this.setvX(-this.getvX());
                break;
            case RIGHT:
                this.setvX(-this.getvX());
                break;
        }
    }

    public Point getPlayerOffset() {
        return playerOffset;
    }

    public void setPlayerOffset(Point playerOffset) {
        this.playerOffset = playerOffset;
    }
}

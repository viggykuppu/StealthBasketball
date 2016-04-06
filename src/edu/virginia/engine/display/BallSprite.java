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
                    if (s.getId().equals("Player")) {
                        PlayerSprite player = (PlayerSprite) s;
                        if (player.getState() != PlayerSprite.PlayerState.NEUTRAL) {
                            this.setvX(0);
                            this.setvY(0);
                            player.setState(PlayerSprite.PlayerState.NEUTRAL);
                            break;
                        }
                    } else if (s.getId().equals("Guard")) {
                        System.out.println("Guard hit!");
                        Direction reflection = this.getCollisionNormal(s);
                        this.reflect(reflection);
                        break;
                    } else if(s.getId().equals("Wall")){
                        // Probably hit a wall
                        Direction reflection = this.getCollisionNormal(s);
                        this.reflect(reflection);
                        break;
                    } else if(s.getId().equals("Hoop")){
                        System.out.println("You won the game");
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

        // get the center of ball
        Rectangle ball = this.getHitCircle().getBounds();
        double ballCenterX = ball.getX() + (ball.getWidth() / 2);
        double ballCenterY = ball.getY() + (ball.getHeight() / 2);

        //Get the center of the rectangle
        double rectCenterX = area.getX() + (area.getWidth() / 2);
        double rectCenterY = area.getY() + (area.getHeight() / 2);

        //Created as vector from ball center to rect center
        double collisionPointX = rectCenterX - ballCenterX;
        double collisionPointY = rectCenterY - ballCenterY;

        //Normalize vector from ball center to rect center to be ball radius length
        double magnitude = Math.sqrt(collisionPointX*collisionPointX + collisionPointY*collisionPointY);
        collisionPointX /= magnitude;
        collisionPointY /= magnitude;
        collisionPointX *= ball.getWidth()/2;//Ball radius standin
        collisionPointY *= ball.getHeight()/2;

        //collisionPoint to be the point the ball hits the rectangle now
        collisionPointX = ballCenterX + collisionPointX;
        collisionPointY = ballCenterY + collisionPointY;

        //If ball has already travelled a good bit through the rectangle, use ballCenter instead of collisionPoint
        if (Math.abs(ballCenterX - collisionPointX) > Math.abs(ballCenterX - rectCenterX))
            collisionPointX = ballCenterX;
        if (Math.abs(ballCenterY - collisionPointY) > Math.abs(ballCenterY - rectCenterY))
            collisionPointY = ballCenterY;

        //Choose the closest rectangle side
        double distLeft = Math.abs(left - collisionPointX);
        double distRight = Math.abs(right - collisionPointX);
        double distUp = Math.abs(up - collisionPointY);
        double distDown = Math.abs(down - collisionPointY);

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

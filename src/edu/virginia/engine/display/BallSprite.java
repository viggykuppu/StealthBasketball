package edu.virginia.engine.display;

import edu.virginia.engine.tween.*;
import edu.virginia.engine.util.Direction;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/20/2016.
 */
public class BallSprite extends PhysicsSprite {

    // static variables
    static int DRIBBLE_OFFSET = 30;

    Point playerOffset = new Point(0, -10);
    Tween ballFollowPlayer;
    GridManager gridManager;
    static int VELOCITY = 5;
    boolean didCollide;
    boolean prevCollide; // this boolean exists solely to manage shouldCollide in loops
    boolean playerCollide;
    Tween dribble;
    boolean dribbleUp; // status of whether the ball is 'up' or on dribble 'down'
    private int duck = 0;
    //static int DEACCEL = -1;

    public BallSprite(String id, String imageFileName) {

        super(id, imageFileName);
        gridManager = GridManager.getInstance();
        didCollide = false; // boolean to prevent double collisions
        prevCollide = false;
        playerCollide = true;
        dribble = new Tween(this);
        dribbleUp = true;
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys) {
        super.update(pressedKeys, heldKeys);
        didCollide = false;
        ArrayList<DisplayObject> spriteList = gridManager.getChildren();
        boolean print = false;
        for (DisplayObject obj : spriteList) {
            if (!obj.equals(this)) {
                GridSprite s = (GridSprite) obj;
                if (this.collidesWith(s)) {
                    // collision confirmed
                    didCollide = true;
                    if (s.getId().equals("Player")) {
                        playerCollide = true;
                    }
                    // did prev frame not have a collision?
                    if (prevCollide == false) {
                        if (s.getId().equals("Player")) {
                            PlayerSprite player = (PlayerSprite) s;
                            if (player.getState() != PlayerSprite.PlayerState.NEUTRAL) {
                                this.setvX(0);
                                this.setvY(0);
                                this.setPosition(new Point(this.getPlayerOffset().x,this.getPlayerOffset().y));
                                GridManager.getInstance().player.addChild(this);
                                player.setState(PlayerSprite.PlayerState.NEUTRAL);
                                GridManager.getInstance().removeBall = true;
                                playerCollide = true;
                                break;
                            }
                        } else if (s.getId().equals("Guard")) {
                            print = true;
                            Direction reflection = this.getCollisionNormal(s);
                            System.out.println("Guard hit! " + reflection);
                            GridGuardSprite guard = (GridGuardSprite) s;
                            guard.setStun(true);
                            this.reflect(reflection);
                            break;
                        } else if (s.getId().equals("Wall")) {
                            print = true;
                            // Probably hit a wall
                            Direction reflection = this.getCollisionNormal(s);
                            System.out.println("wall hit " + reflection);
                            this.reflect(reflection);
                            break;
                        } else if (s.getId().equals("Hoop")) {
                            GridManager.getInstance().endLevel();
                            System.out.println("You won the level!!!");
                        }
                    } else if (playerCollide) {
                        if (s.getId().equals("Guard")) {
                            print = true;
                            Direction reflection = this.getCollisionNormal(s);
                            System.out.println("Guard hit! " + reflection);
                            GridGuardSprite guard = (GridGuardSprite) s;
                            guard.setStun(true);
                            this.reflect(reflection);
                            break;
                        } else if (s.getId().equals("Wall")) {
                            print = true;
                            // Probably hit a wall
                            Direction reflection = this.getCollisionNormal(s);
                            System.out.println("wall hit " + reflection);
                            this.reflect(reflection);
                            break;
                        } else if (s.getId().equals("Hoop")) {
                            GridManager.getInstance().endLevel();
                            System.out.println("You won the level!!!");
                        }
                    }
                }
            }
        }
        if (getPosition().x >= gridManager.getGameX()) {
            this.reflect(Direction.LEFT);
        } else if (getPosition().x <= 0) {
            this.reflect(Direction.RIGHT);
        }

        if (getPosition().y >= gridManager.getGameY()) {
            this.reflect(Direction.UP);
        } else if (getPosition().y <= 0) {
            this.reflect(Direction.DOWN);
        }
        if (print)
            System.out.println("-------------");
        // only if there is a frame where there is no collisions should this reset
        prevCollide = didCollide;
        // we should only turn playerCollide to false once all collisions are done
        playerCollide = false;
    }


    public void pathToGridPoint(Point gridDest, long timems) {
        Point endPosition = new Point(gridDest);
        endPosition = GridManager.getInstance().gridtoGamePoint(endPosition);
        endPosition.x += playerOffset.x;
        endPosition.y += playerOffset.y;

        ballFollowPlayer = new Tween(this);
        ballFollowPlayer.animate(TweenableParams.X, getPosition().x, endPosition.x, timems);
        ballFollowPlayer.animate(TweenableParams.Y, getPosition().y, endPosition.y, timems);
        TweenJuggler.getInstance().addTweenNonRedundant(ballFollowPlayer, this);
        dribbleUp = true;
    }


    /*
        Gets the current position and gives the ball physics
     */
    public void throwBall(int x, int y) {
        //First calculate
        TweenJuggler.getInstance().removeTween(dribble);
        double normalizingFactor = Math.sqrt(x*x+y*y);
        double vx = VELOCITY*(x/normalizingFactor);
        double vy  = VELOCITY*(y/normalizingFactor);
        this.setvX(vx);
        this.setvY(vy);
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

        //The corners of the rectangle
        Point2D.Double topLeft = new Point2D.Double(left, up);
        Point2D.Double topRight = new Point2D.Double(right, up);
        Point2D.Double bottomLeft = new Point2D.Double(left, down);
        Point2D.Double bottomRight = new Point2D.Double(right, down);

        // get the center of ball
        Rectangle ball = this.getHitCircle().getBounds();
        double ballCenterX = ball.getX() + (ball.getWidth() / 2);
        double ballCenterY = ball.getY() + (ball.getHeight() / 2);
        Point2D.Double ballCenter = new Point2D.Double(ballCenterX, ballCenterY);

        double distLeft = distToSegment2(ballCenter, topLeft, bottomLeft);
        double distRight = distToSegment2(ballCenter, topRight, bottomRight);
        double distUp = distToSegment2(ballCenter, topLeft, topRight);
        double distDown = distToSegment2(ballCenter, bottomLeft, bottomRight);

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
        if(s.getId().equals("Wall")){
            GridWallSprite wall = (GridWallSprite) s;
            boolean isHorizontal = wall.isHorizontal();
            Direction temp;
            double d = 0;
            if(isHorizontal && (ret == Direction.LEFT || ret == Direction.RIGHT)){
                if(distDown < distUp){
                    temp = Direction.DOWN;
                    d = distDown;
                } else {
                    temp = Direction.UP;
                    d = distUp;
                }
                if(d == smallest){
                    ret = temp;
                }
            }
            if(!isHorizontal && (ret == Direction.UP || ret == Direction.DOWN)){
                if(distLeft < distRight){
                    temp = Direction.LEFT;
                    d = distLeft;
                } else {
                    temp = Direction.RIGHT;
                    d = distRight;
                }
                if(d == smallest){
                    ret = temp;
                }
            }
        }
        return ret;
    }

    double distToSegment2(Point2D.Double p, Point2D.Double v, Point2D.Double w) {
        double l2 = v.distanceSq(w);
        if (l2 == 0)
            return p.distanceSq(v);
        double t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2;
        t = Math.max(0, Math.min(1, t));
        return p.distanceSq(new Point2D.Double(v.x + t * (w.x - v.x), v.y + t * (w.y - v.y)));
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

    /*
        Player should call this once every x secs or so
        This method will tween down then back up
        param: how long you want the tween to be
     */
    public void dribble(long timems) {
        // player will call dribble if he's dribbling
        // if currently tweening, don't do anything
        //if (ballFollowPlayer.isComplete()) {
            // if not tweening, call appropriate tween animation
            // dribble down
        dribble = new Tween(this);
            if (dribbleUp) {
                dribble.animate(TweenableParams.BALL_DRIBBLE, 0, DRIBBLE_OFFSET, timems, TweenTransitionIndex.QUAD);
                dribbleUp = false;
            } else {
                // dribble up
                dribble.animate(TweenableParams.BALL_DRIBBLE, DRIBBLE_OFFSET, 0, timems, TweenTransitionIndex.INVERSE_QUAD);
                dribbleUp = true;
            }
            TweenJuggler.getInstance().addTweenNonRedundant(dribble, this);
        //}
    }

    @Override
    public Point getPosition(){
        return new Point(super.getPosition().x,super.getPosition().y+duck);
    }

    @Override
    public void setPosition(Point position){
       if(GridManager.getInstance().player.getState() == PlayerSprite.PlayerState.NoBall){
            super.setPosition(position);
       } else {
           position = new Point(position.x,position.y-duck);
           super.setPosition(position);
       }
    }
    
    public Point getPlayerOffset() {
        return playerOffset;
    }

    public void setDuck(int duck){
        this.duck = duck;
    }

    public int getDuck(){
        return this.duck;
    }

    public void setPlayerOffset(Point playerOffset) {
        this.playerOffset = playerOffset;
    }
}

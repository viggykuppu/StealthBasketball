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
    Point p;
    Point playerOffset = new Point(0, -10);
    Tween ballFollowPlayer;
    GridManager gridManager;
    static int VELOCITY = 4;
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
        ArrayList<DisplayObject> spriteList = gridManager.getChildren();
        boolean print = false;
        boolean playerFlag = false;
        for (DisplayObject obj : spriteList) {
            if (!obj.equals(this)) {
                GridSprite s = (GridSprite) obj;

                //Check if our ball collides with this gridsprite
                if (this.collidesWith(s)) {
                    //If we are colliding with the player, let's do stuff here
                    if (s.getId().equals("Player")) {
                        playerFlag = true;
                        PlayerSprite player = (PlayerSprite) s;
                        //If we don't have the ball, let gridmanager know to give us the ball
                        //Also, we only want the ball back after it has:
                        //a) collided with something else
                        //b) stopped colliding with the player
                        if (player.getState() != PlayerSprite.PlayerState.NEUTRAL && !playerCollide) {
                            print = true;
//                            System.out.println("caught ball");
                            GridManager.getInstance().removeBall = true;
                            playerCollide = true;
                            break;
                        }
                        //Let us know that we are currently colliding with the player
                        playerCollide = true;
                    }

                    //Only check other collisions if the player doesn't have the ball, because when the player does
                    //The ball does not have physics
                    //In each collision we then binary search to determine the closest position where the ball
                    //Is no longer in collision
                    if (GridManager.getInstance().player.getState() == PlayerSprite.PlayerState.NoBall) {
                        if (s.getId().equals("Wall")) {
//                            print = true;
                            playerCollide = false;
                            Direction reflection = this.getCollisionNormal(s);
//                            System.out.println("wall hit " + reflection);
                            this.reflect(reflection);
                            this.setPosition(this.determineCollisionsPlacement(s));
                        } else if (s.getId().equals("Guard")) {
//                            print = true;
                            playerCollide = false;
                            Direction reflection = this.getCollisionNormal(s);
                            GridGuardSprite guard = (GridGuardSprite) s;
                            guard.setStun(true);
                            this.reflect(reflection);
                            this.setPosition(this.determineCollisionsPlacement(s));
                        } else if (s.getId().equals("Hoop")) {
                            GridManager.getInstance().endLevel();
                            System.out.println("You won the level!!!");
                            break;
                        }
                    }
                }
            }
        }
        if(!playerFlag){
            playerCollide = playerFlag;
        }
        //Update our previous position, useful because when we collide, we can place our ball just before the collision
        p = this.getPosition();
//        if(print)
//            System.out.println("-------------");
    }

    //Binary search methodology for getting the ball as close as possible to a colliding object
    public Point determineCollisionsPlacement(GridSprite g){
        //Our last point of collision
        Point collision = this.getPosition();
        //Set it to our previous location and see what happens, should not collide here
        this.setPosition(p);
        Point lastSuccess;
        if(this.collidesWith(g)){
            //If it does collide, that's bad
            return p;
        } else {
            //Else, we now find our last successful non-collision point
            lastSuccess = new Point(p.x,p.y);
            //Keep track of where we currently are in our binary search
            Point current = new Point(p.x,p.y);
            //Also want previous point in case we end up futile in our search
            Point previous;
            //Keep track of if our current point had a collision or not so we know which direction to
            //Binary search
            boolean previousCollision = false;
            //Only do this 10 times at max to cap our while loop
            int i = 0;
            while(i < 10){
                if(previousCollision){
                    //Collision, so binary search b/t p & current
                    previous = new Point(current.x,current.y);
                    current = new Point((current.x+p.x)/2,(current.y+p.y)/2);
                } else {
                    //No Collision, so binary search b/t current & previous point of collision
                    previous = new Point(current.x,current.y);
                    current = new Point((current.x+collision.x)/2,(current.y+collision.y)/2);
                }
                this.setPosition(current);
                if(this.collidesWith(g)){
                    //Collision, so binary search b/t p & current which is pC = true
                    collision = new Point(current.x,current.y);
                    previousCollision = true;
                } else {
                    //No Collision, so binary search b/t current & previous point of collision which is pC = false
                    lastSuccess = new Point(current.x,current.y);
                    previousCollision = false;
                }
                //If we got the same point twice, break
                if(previous.x == current.x && previous.y == current.y){
                    break;
                }
                i++;
            }
            //Ultimately return our last successful point of placement
            return lastSuccess;
        }
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

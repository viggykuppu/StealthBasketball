package edu.virginia.engine.display;

import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.tween.TweenableParams;
import edu.virginia.engine.util.Direction;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/20/2016.
 */
public class PlayerSprite extends GridSprite {

    private BallSprite myBall;
    private PlayerPingEffect pingEffect;
    private int pingRadius = 400;
    PlayerState state = PlayerState.NEUTRAL;
    private long timer = System.currentTimeMillis();
    boolean dunkKeyed = false;
    Direction dunkDir;
    GridManager gridManager;
    private boolean movedBall;

    public enum PlayerState {
        NEUTRAL, DUNKING, NoBall, THROWING
    }

        public PlayerSprite(String id, String imageFileName, BallSprite myBall) {
            super(id, imageFileName,GridSpriteTypes.Player);
            gridManager = GridManager.getInstance();
            this.myBall = myBall;
            this.pingEffect = new PlayerPingEffect("Ping1",this);
            gridManager.setPlayer(this);
            this.addChild(myBall);
        }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys) {
        super.update(pressedKeys, heldKeys);

        //Dribble sound only applies when holding ball
        if(state != PlayerState.NoBall){
            this.generateSound(pingEffect.getRadius());
        }

        //Center screen to player
        GridManager.getInstance().centerPointOnScreen(getPosition().x,getPosition().y);

        if (state == PlayerState.NEUTRAL) {
            //Update player subcomponents
            //myBall.update(pressedKeys, heldKeys);
            long delta = System.currentTimeMillis();
            if (delta - timer > 1000) {
                myBall.dribble(500);
                timer = System.currentTimeMillis();
            }
            
            if (heldKeys.contains(KeyEvent.VK_Z)) {
                dunkKeyed = true;
            } else
                dunkKeyed = false;

            if (!dunkKeyed) {//Normal Movement

                if (pressedKeys.contains(KeyEvent.VK_UP)) {
                    if (moveOnGrid(0, -1, 500)) {
//                        myBall.pathToGridPoint(gridPosition, 500);
                    }
                } else if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
                    if (moveOnGrid(0, 1, 500)) {
//                        myBall.pathToGridPoint(gridPosition, 500);
                    }
                } else if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
                    if (moveOnGrid(-1, 0, 500)) {
//                        myBall.pathToGridPoint(gridPosition, 500);
                    }
                } else if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                    if (moveOnGrid(1, 0, 500)) {
//                        myBall.pathToGridPoint(gridPosition, 500);
                    }
                }
            } else {//Dunking action

//                if (pressedKeys.contains(KeyEvent.VK_UP)) {
//                    if (moveOnGrid(0, -1, 500)) {
//                        myBall.dunk(Direction.UP);
//                        state = PlayerState.NoBall;
//                    }
//                } else if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
//                    if (moveOnGrid(0, 1, 500)) {
//                        myBall.dunk(Direction.DOWN);
//                        state = PlayerState.NoBall;
//                    }
//                } else if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
//                    if (moveOnGrid(-1, 0, 500)) {
//                        myBall.dunk(Direction.LEFT);
//                        state = PlayerState.NoBall;
//                    }
//                } else if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
//                    if (moveOnGrid(1, 0, 500)) {
//                        myBall.dunk(Direction.RIGHT);
//                        state = PlayerState.NoBall;
//                    }
//                }
            }
        } else if (state == PlayerState.NoBall) {

            if (pressedKeys.contains(KeyEvent.VK_UP)) {
                moveOnGrid(0, -1, 500);
            } else if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
                moveOnGrid(0, 1, 500);
            } else if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
                moveOnGrid(-1, 0, 500);
            } else if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                moveOnGrid(1, 0, 500);
            }
        }
    }

    public void throwBall(int x, int y){
        if (state == PlayerState.NEUTRAL) {
            state = PlayerState.NoBall;
            this.removeChild(myBall);
            Point newBallPosition = new Point(this.getPosition().x + myBall.getPosition().x, this.getPosition().y + myBall.getPosition().y);
            myBall.setPosition(newBallPosition);
            Point globalBallPosition = this.globalize(newBallPosition);
            myBall.setDuck(0);
            Point relativeP = new Point(x - globalBallPosition.x, y - globalBallPosition.y);
            System.out.println(relativeP);
            GridManager.getInstance().addChild(myBall);
            myBall.throwBall(relativeP.x, relativeP.y);
        }
    }

    public void generateSound(int radius){
        ArrayList<DisplayObject> sprites = GridManager.getInstance().getChildren();
        ArrayList<GridGuardSprite> guards = new ArrayList<GridGuardSprite>();
        for(DisplayObject d : sprites){
            if(d.getId().equals("Guard")){
                guards.add((GridGuardSprite)d);
            }
        }
        for(GridGuardSprite g : guards){
            if(this.getPosition().distance(g.getPosition()) <= radius){
                g.updatePlayerLocation(this.getGridPosition());
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        pingEffect.draw(g);
    }

    @Override
    public void gridTurnUpdate() {
        //Generate ping visual
        if(this.state != PlayerState.NoBall){
            Tween t = new Tween(pingEffect);
            t.animate(TweenableParams.PING_RADIUS,0,pingRadius,300);
            t.animate(TweenableParams.ALPHA,1.0,0.0,300);
            TweenJuggler.getInstance().addTween(t);
        }
    }

    public PlayerState getState() { return this.state; }
    public void setState(PlayerState state) { this.state = state; }
    public BallSprite getBall() {
        return myBall;
    }
}

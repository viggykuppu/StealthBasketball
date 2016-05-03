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
    private Direction playerOrientation;

    private long teleportTimer = System.nanoTime();
    private static double DRIBBLE_ANIM_SPD = .45;
    // Player images defined up here for abstraction
    private static String idle_left = "player/player_idle_left.png";
    private static String idle_right = "player/player_idle_right.png";
    private static String idle_back = "player/player_idle_back.png";
    private static String idle_front = "player/player_idle_front_1.png";

    //player dribbling
    private ArrayList<String> dribble_left;
    private static String dribble_left_1 = "player/player_dribble_left_1.png";
    private static String dribble_left_2 = "player/player_dribble_left_2.png";

    private ArrayList<String> dribble_right;
    private static String dribble_right_1 = "player/player_dribble_right_1.png";
    private static String dribble_right_2 = "player/player_dribble_right_2.png";

    private ArrayList<String> dribble_back;
    private static String dribble_back_1 = "player/player_dribble_back_1.png";
    private static String dribble_back_2 = "player/player_dribble_back_2.png";

    private ArrayList<String> dribble_front;
    private static String dribble_front_1 = "player/player_dribble_front_1.png";
    private static String dribble_front_2 = "player/player_dribble_front_2.png";


    public enum PlayerState {
        NEUTRAL, DUNKING, NoBall, THROWING
    }

        public PlayerSprite(String id, BallSprite myBall) {
            super(id, idle_left,GridSpriteTypes.Player);
            playerOrientation = Direction.LEFT;
            gridManager = GridManager.getInstance();
            this.myBall = myBall;
            this.pingEffect = new PlayerPingEffect("Ping1",this);
            gridManager.setPlayer(this);
            this.addChild(myBall);

            // initialize the dribble anims
            dribble_left = new ArrayList<>();
            dribble_left.add(dribble_left_1);
            dribble_left.add(dribble_left_2);
            this.readAnimation("dribble_left", dribble_left, DRIBBLE_ANIM_SPD);

            dribble_right = new ArrayList<>();
            dribble_right.add(dribble_right_1);
            dribble_right.add(dribble_right_2);
            this.readAnimation("dribble_right", dribble_right, DRIBBLE_ANIM_SPD);

            dribble_back = new ArrayList<>();
            dribble_back.add(dribble_back_1);
            dribble_back.add(dribble_back_2);
            this.readAnimation("dribble_back", dribble_back, DRIBBLE_ANIM_SPD);

            dribble_front = new ArrayList<>();
            dribble_front.add(dribble_front_1);
            dribble_front.add(dribble_front_2);
            this.readAnimation("dribble_front", dribble_front, DRIBBLE_ANIM_SPD);
        }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys) {
        super.update(pressedKeys, heldKeys);

        //Dribble sound only applies when holding ball
        if (state != PlayerState.NoBall) {
            this.generateSound(pingEffect.getRadius());
        }

        //Center screen to player
        GridManager.getInstance().centerPointOnScreen(getPosition().x, getPosition().y);

        if (state == PlayerState.NEUTRAL) {
            //Update player subcomponents
            //myBall.update(pressedKeys, heldKeys);
            long delta = System.currentTimeMillis();
            if (delta - timer > 500) {
                myBall.dribble(500);
                timer = System.currentTimeMillis();
            }

            if (heldKeys.contains(KeyEvent.VK_Z)) {
                dunkKeyed = true;
            } else
                dunkKeyed = false;

            if (!dunkKeyed) {//Normal Movement

                if (pressedKeys.contains(KeyEvent.VK_W)) {
                    moveOnGrid(0, -1, 500);
                    playerOrientation = Direction.UP;
                } else if (pressedKeys.contains(KeyEvent.VK_S)) {
                    moveOnGrid(0, 1, 500);
                    playerOrientation = Direction.DOWN;
                } else if (pressedKeys.contains(KeyEvent.VK_A)) {
                    moveOnGrid(-1, 0, 500);
                    playerOrientation = Direction.LEFT;
                } else if (pressedKeys.contains(KeyEvent.VK_D)) {
                    moveOnGrid(1, 0, 500);
                    playerOrientation = Direction.RIGHT;
                }
            }
            // orient the correct player sprite
            switch (playerOrientation) {
                case UP:
                    if (myBall.getDribbleUp()) {
                        this.setImage(dribble_back.get(0));
                    } else {
                        this.setImage(dribble_back.get(1));
                    }
                    break;
                case DOWN:
                    if (myBall.getDribbleUp()) {
                        this.setImage(dribble_front.get(0));
                    } else {
                        this.setImage(dribble_front.get(1));
                    }
                    break;
                case LEFT:
                    if (myBall.getDribbleUp()) {
                        this.setImage(dribble_left.get(0));
                    } else {
                        this.setImage(dribble_left.get(1));
                    }
                    break;
                case RIGHT:
                    if (myBall.getDribbleUp()) {
                        this.setImage(dribble_right.get(0));
                    } else {
                        this.setImage(dribble_right.get(1));
                    }
                    break;
                default:
                    if (myBall.getDribbleUp()) {
                        this.setImage(dribble_left.get(0));
                    } else {
                        this.setImage(dribble_left.get(1));
                    }
                    break;
            }
        } else if (state == PlayerState.NoBall) {

            if (pressedKeys.contains(KeyEvent.VK_W)) {
                moveOnGrid(0, -1, 500);
                playerOrientation = Direction.UP;
            } else if (pressedKeys.contains(KeyEvent.VK_S)) {
                moveOnGrid(0, 1, 500);
                playerOrientation = Direction.DOWN;
            } else if (pressedKeys.contains(KeyEvent.VK_A)) {
                moveOnGrid(-1, 0, 500);
                playerOrientation = Direction.LEFT;
            } else if (pressedKeys.contains(KeyEvent.VK_D)) {
                moveOnGrid(1, 0, 500);
                playerOrientation = Direction.RIGHT;
            }
            switch (playerOrientation) {
                case UP:
                    this.setImage(idle_back);
                    break;
                case DOWN:
                    this.setImage(idle_front);
                    break;
                case LEFT:
                    this.setImage(idle_left);
                    break;
                case RIGHT:
                    this.setImage(idle_right);
                    break;
                default:
                    this.setImage(idle_left);
            }
        }
        if (GridManager.getInstance().getSpriteAtGridPoint(getGridPosition(),GridSpriteTypes.Teleporter) != null){
            ((TeleporterSprite)GridManager.getInstance().getSpriteAtGridPoint(getGridPosition(),GridSpriteTypes.Teleporter)).getPartner();

        }

        //Player collision checks
        for (DisplayObject g : GridManager.getInstance().getChildren()) {
            if (g.getClass().equals(GridGuardSprite.class)){
                GridGuardSprite guard = (GridGuardSprite) g;
                if (this.collidesWith(guard)){
                    if (!guard.getStunned())
                        GridManager.getInstance().levelFailed = true;
                }
            }
            if (g.getClass().equals(TeleporterSprite.class)) {
                TeleporterSprite tpS = (TeleporterSprite) g;
                if (collidesWith(tpS)) {//Teleport!
                    if (System.nanoTime() - teleportTimer > 2*GridManager.getInstance().getTurnLength()*1000*1000){
                        Point destination = new Point(tpS.getPartner().getGridPosition().x,tpS.getPartner().getGridPosition().y);
                        if (GridManager.getInstance().getSpriteAtGridPoint(destination,GridSpriteTypes.Guard) == null){
                            if (GridManager.getInstance().swapSprites(gridPosition,destination,getGridSpriteType())) {
                                TweenJuggler.getInstance().removeTweensByObject(this);
                                gridPosition.x = destination.x;
                                gridPosition.y = destination.y;
                                teleportTimer = System.nanoTime();
                            }
                        }
                    }
                }
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
//            System.out.println(relativeP);
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
            t.animate(TweenableParams.PING_RADIUS,0,pingRadius,500);
            t.animate(TweenableParams.ALPHA,1.0,0.0,500);
            TweenJuggler.getInstance().addTween(t);
        }
    }

    public PlayerState getState() { return this.state; }
    public void setState(PlayerState state) { this.state = state; }
    public BallSprite getBall() {
        return myBall;
    }
}

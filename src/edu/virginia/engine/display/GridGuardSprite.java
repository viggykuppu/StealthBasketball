package edu.virginia.engine.display;

import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.util.Direction;

import java.awt.*;
import java.awt.geom.Line2D;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by vignesh on 3/20/16.
 */
public class GridGuardSprite extends GridSprite{
    PlayerSprite player;
    //This should be the distance^2
    double sightRadius = 500;
    Point lastKnownPlayerLocation;
    GridGuardState guardState = GridGuardState.idle;
    static double STUNLENGTH = 5; // determined in seconds
    static double WALKLENGTH = 1.5;
    boolean stunned;
    boolean isWalking;
    ArrayList<GridSprite> aPath;
    GridManager gridManager;
    private Direction guardOrientation;
    private ArrayList<String> stunAnim;
    private long teleportTimer = System.nanoTime();

    private enum GridGuardState{
        playerVisible,lostPlayer,idle;
    }

    // just the idling anims
    private static String idle_back = "guard/Guard_idle_back.png";
    private static String idle_front = "guard/guard_front_idle.png";
    private static String idle_left = "guard/Guard_idle_left.png";
    private static String idle_right = "guard/Guard_idle_right.png";

    // the ko anims
    private ArrayList<String> back_ko;
    private static String ko_back = "guard/Guard_ko_back.png";
    private ArrayList<String> front_ko;
    private static String ko_front = "guard/Guard_ko_front.png";
    private ArrayList<String> left_ko;
    private static String ko_left = "guard/Guard_ko_left.png";
    private ArrayList<String> right_ko;
    private static String ko_right = "guard/Guard_ko_right.png";

    // guard walking
    private ArrayList<String> walk_back;
    private static String walk_back_1 = "guard/Guard_walk_back_1.png";
    private static String walk_back_2 = "guard/Guard_walk_back_2.png";

    private ArrayList<String> walk_front;
    private static String walk_front_1 = "guard/Guard_walk_front_1.png";
    private static String walk_front_2 = "guard/Guard_walk_front_2.png";

    private ArrayList<String> walk_left;
    private static String walk_left_1 = "guard/Guard_walk_left_1.png";
    private static String walk_left_2 = "guard/Guard_walk_left_2.png";

    private ArrayList<String> walk_right;
    private static String walk_right_1 = "guard/Guard_walk_right_1.png";
    private static String walk_right_2 = "guard/Guard_walk_right_2.png";

    public GridGuardSprite(String id, PlayerSprite player) {
        super(id, idle_right, GridSpriteTypes.Guard);
        this.player = player;
        this.stunned = false;
        isWalking = false;
        this.guardOrientation = Direction.RIGHT;
        // initialize all the stun anims
        back_ko = new ArrayList<>();
        back_ko.add(ko_back);
        back_ko.add(ko_back);
        readAnimation("back_ko", back_ko, STUNLENGTH);

        front_ko = new ArrayList<>();
        front_ko.add(ko_front);
        front_ko.add(ko_front);
        readAnimation("front_ko", front_ko, STUNLENGTH);

        left_ko = new ArrayList<>();
        left_ko.add(ko_left);
        left_ko.add(ko_left);
        readAnimation("left_ko", left_ko, STUNLENGTH);

        right_ko = new ArrayList<>();
        right_ko.add(ko_right);
        right_ko.add(ko_right);
        readAnimation("right_ko", right_ko, STUNLENGTH);

        // initialize guard walking anims
        walk_back = new ArrayList<>();
        walk_back.add(walk_back_1);
        walk_back.add(walk_back_2);
        readAnimation("walk_back", walk_back, WALKLENGTH);

        walk_front = new ArrayList<>();
        walk_front.add(walk_front_1);
        walk_front.add(walk_front_2);
        readAnimation("walk_front", walk_front, WALKLENGTH);

        walk_left = new ArrayList<>();
        walk_left.add(walk_left_1);
        walk_left.add(walk_left_2);
        readAnimation("walk_left", walk_left, WALKLENGTH);

        walk_right = new ArrayList<>();
        walk_right.add(walk_right_1);
        walk_right.add(walk_right_2);
        readAnimation("walk_right", walk_right, WALKLENGTH);

        aPath = new ArrayList<>();
        gridManager = GridManager.getInstance();
    }

    public boolean canDetectPlayer(){
        ArrayList<DisplayObject> collisions = this.checkRay(this.getPosition(),player.getPosition(),this.sightRadius);
        if(collisions.size() == 2 && collisions.contains(player)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void gridTurnUpdate(){
        if(this.canDetectPlayer()){
            this.updatePlayerLocation(player.getGridPosition());
        }
        isWalking = false;
        if (stunned) {

        }
        else if(guardState == GridGuardState.idle){

        } else {
            isWalking = true;
            aStar(lastKnownPlayerLocation);
        }
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys) {
        super.update(pressedKeys, heldKeys);
        if (stunned) {
            switch (guardOrientation) {
                case UP:
                    if (this.timeAnimate(STUNLENGTH)) {
                        stunned = false;
                    } else {
                        this.setImage(ko_back);
                    }
                    break;
                case DOWN:
                    if (this.timeAnimate(STUNLENGTH)) {
                        stunned = false;
                    } else {
                        this.setImage(ko_front);
                    }
                    break;
                case LEFT:
                    if (this.timeAnimate(STUNLENGTH)) {
                        stunned = false;
                    } else {
                        this.setImage(ko_left);
                    }
                    break;
                case RIGHT:
                    if (this.timeAnimate(STUNLENGTH)) {
                        stunned = false;
                    } else {
                        this.setImage(ko_right);
                    }
                    break;
            }
        } else if (isWalking) {
            switch (guardOrientation) {
                case UP:
                    if (this.timeAnimates(WALKLENGTH)) {
                        this.setImage(walk_back_2);
                    } else {
                        this.setImage(walk_back_1);
                    }
                    break;
                case DOWN:
                    if (this.timeAnimates(WALKLENGTH)) {
                        this.setImage(walk_front_2);
                    } else {
                        this.setImage(walk_front_1);
                    }
                    break;
                case LEFT:
                    if (this.timeAnimates(WALKLENGTH)) {
                        this.setImage(walk_left_2);
                    } else {
                        this.setImage(walk_left_1);
                    }
                    break;
                case RIGHT:
                    if (this.timeAnimates(WALKLENGTH)) {
                        this.setImage(walk_right_2);
                    } else {
                        this.setImage(walk_right_1);
                    }
                    break;
            }
        } else {
            switch (guardOrientation) {
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
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2d = (Graphics2D)g;
        for (int i = 0; i < aPath.size(); i++) {
            aPath.get(i).draw(g2d);
        }
    }

    public void playerMissingBehavior(){
        if(this.guardState == GridGuardState.playerVisible){
            this.guardState = GridGuardState.lostPlayer;
            aStar(lastKnownPlayerLocation);
        }
        if(this.guardState.equals(GridGuardState.lostPlayer)){
            aStar(lastKnownPlayerLocation);
            if(this.getGridPosition().equals(lastKnownPlayerLocation)){
                this.guardState = GridGuardState.idle;
            }
        }
    }

    public void aStar(Point playerPosition){
        //Begin A*, get the graph we're working on
        GridCell[][] graph = GridManager.getInstance().sprites;
        GridCell curr = graph[this.getGridPosition().x][this.getGridPosition().y];
        PriorityQueue<GridCell> q = new PriorityQueue<GridCell>(new Comparator<GridCell>() {
            @Override
            public int compare(GridCell gridCell, GridCell t1) {
                return gridCell.f - t1.f;
            }
        });
        HashSet<Point> closedSet = new HashSet<Point>();
        HashSet<Point> seen = new HashSet<Point>();
        HashMap<Point,Point> cameFrom = new HashMap<Point,Point>();

        q.add(curr);
        seen.add(curr.location);
        curr.g = 0;
        while(!q.isEmpty()){
            curr = q.poll();
            if(curr.location.equals(playerPosition)){
                break;
            }
            if(curr == null){
                System.err.println("A* failed");
            }
            closedSet.add(curr.location);
            for(GridCell node : curr.neighbors.values()){
                if(!closedSet.contains(node.location)){
                    if(!seen.contains(node.location)){
                        seen.add(node.location);
                        node.g = curr.g+1;
                        node.f = node.h+node.g;
                        q.add(node);
//                        System.out.println(node.g + " "+node.location);
                        cameFrom.put(node.location,curr.location);
                    } else {
                        if(curr.g+1 < node.g){
                            node.g = curr.g+1;
                            node.f = node.h+node.g;
                            q.remove(node);
                            q.add(node);
                            cameFrom.put(node.location,curr.location);
                        }
                    }
                }
            }
        }
        //Now we need to retrace our path back to the start.
        ArrayList<Point> path = new ArrayList<Point>();
        Point p = curr.location;
        while(p != null){
            path.add(p);
            p = cameFrom.get(p);
        }

        Collections.reverse(path);

//        for(Point po : path){
//            System.out.print("("+po.x + ","+po.y+") ->");
//        }
//        System.out.println();

        // generating the path from guard to player
        // TODO take this out when done testing
        aPath.clear();
//        for (int i = 0; i < path.size(); i++) {
//            GridSprite aSprite = new GridSprite("path", "coin.gif");
//            aSprite.setGridPosition(path.get(i));
//            Point normalPoint = gridManager.gridtoGamePoint(path.get(i));
//            aSprite.setPosition(normalPoint);
//            aPath.add(aSprite);
//        }
        GridSprite aSprite = new GridSprite("path", "x.png");
        aSprite.setScaleX(.5);
        aSprite.setScaleY(.5);
        Point pivot = new Point(60, 60);
        aSprite.setPivotPoint(pivot);
        aSprite.setGridPosition(path.get(path.size()-1));
        Point normalPoint = gridManager.gridtoGamePoint(path.get(path.size()-1));
        aSprite.setPosition(normalPoint);
        aPath.add(aSprite);

        //Code for checking path if you would like to do that
        resetAStarGrid();
        Point nextPosition;
        if(path.size() == 1){
            nextPosition = path.get(0);
        } else {
            nextPosition = path.get(1);
        }
        GridSprite gtp = GridManager.getInstance().sprites[nextPosition.x][nextPosition.y].getSprite(GridSpriteTypes.Teleporter);
        if(gtp != null){
            TeleporterSprite tp = (TeleporterSprite) gtp;
            if(System.nanoTime() - teleportTimer > 2*GridManager.getInstance().getTurnLength()*1000*1000){
                boolean hi = GridManager.getInstance().swapSprites(gridPosition,nextPosition,getGridSpriteType());
                if (hi) {
                    gridPosition.x = nextPosition.x;
                    gridPosition.y = nextPosition.y;
                    teleportTimer = System.nanoTime();
                }
            }
        } else {
            if (nextPosition.x > this.getGridPosition().x) {
                moveOnGrid(1, 0, 500);
                guardOrientation = Direction.RIGHT;
            } else if (nextPosition.x < this.getGridPosition().x) {
                moveOnGrid(-1, 0, 500);
                guardOrientation = Direction.LEFT;
            } else if (nextPosition.y > this.getGridPosition().y) {
                moveOnGrid(0, 1, 500);
                guardOrientation = Direction.DOWN;
            } else if (nextPosition.y < this.getGridPosition().y) {
                moveOnGrid(0, -1, 500);
                guardOrientation = Direction.UP;
            }
        }
    }

    public void resetAStarGrid(){
        GridManager.getInstance().resetAStarGrid();
    }

    public int getMDistance(Point a, Point b){
        return Math.abs(a.x-b.x) + Math.abs(a.y-b.y);
    }

    public void setSightRadius(double sightRadius){
        this.sightRadius = sightRadius;
    }

    public double getSightRadius(){
        return this.sightRadius;
    }

    public void setStun(boolean stunned) { this.stunned = stunned; }

    public boolean getStunned(){
        return stunned;
    }

    public void updatePlayerLocation(Point position){
        this.guardState = GridGuardState.lostPlayer.playerVisible;
        this.lastKnownPlayerLocation = position;
    }

}

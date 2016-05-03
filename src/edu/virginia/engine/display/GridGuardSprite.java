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
    static double STUNLENGTH = 3; // determined in seconds
    boolean stunned;
    ArrayList<GridSprite> aPath;
    GridManager gridManager;
    private ArrayList<String> stunAnim;
    private long teleportTimer = System.nanoTime();

    private enum GridGuardState{
        playerVisible,lostPlayer,idle;
    }

    public GridGuardSprite(String id, String imageFileName, PlayerSprite player) {
        super(id, imageFileName, GridSpriteTypes.Guard);
        stunAnim = new ArrayList<>();
        this.player = player;
        this.stunned = false;
        stunAnim.add("meg_stunned.png");
        stunAnim.add("meg_stunned.png");
        readAnimation("Stunned", stunAnim, STUNLENGTH);
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
        if (stunned) {

        }
        else if(guardState == GridGuardState.idle){

        } else {
            aStar(lastKnownPlayerLocation);
        }
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys) {
        super.update(pressedKeys, heldKeys);
        if (stunned) {
            if (this.animate("Stunned")) {
                stunned = false;
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
            if(nextPosition.x > this.getGridPosition().x){
                moveOnGrid(1,0,500);
            } else if(nextPosition.x < this.getGridPosition().x){
                moveOnGrid(-1,0,500);
            } else if(nextPosition.y > this.getGridPosition().y){
                moveOnGrid(0,1,500);
            } else if(nextPosition.y < this.getGridPosition().y){
                moveOnGrid(0,-1,500);
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

    public void updatePlayerLocation(Point position){
        this.guardState = GridGuardState.lostPlayer.playerVisible;
        this.lastKnownPlayerLocation = position;
    }

}

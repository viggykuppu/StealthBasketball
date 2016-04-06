package edu.virginia.engine.display;

import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.tween.TweenableParams;
import edu.virginia.engine.util.Direction;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Created by Guillaume Bailey on 3/19/2016.
 */
public class GridManager extends DisplayObjectContainer{
    //Singleton stuff
    private static GridManager ourInstance = new GridManager("gridManager");

    public static GridManager getInstance() {
        return ourInstance;
    }

    private GridManager(String id) {
        super(id);
    }

    //Overall grid scalers and size
    public int gridX;
    public int gridY;
    int gridxScale;
    int gridyScale;
    int screenX;
    int screenY;

    public GridCell[][] sprites = null;

    float turnLength = 1000;
    long previousTurnTime;
    boolean turnsActive = false;

    long scrollSpeed = 200;//Milliseconds to tween screen scrolling

    PlayerSprite player; //this is the player sprite

    @Override
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D)g;

        if(sprites !=null){
            for (int x = 0; x < sprites.length; x++){
                for (int y = 0; y < sprites[x].length; y++){
                    g2d.drawRect(x*gridxScale + getPosition().x, y*gridyScale + getPosition().y, gridxScale, gridyScale);
                }
            }
        }

        getChildren().sort(new Comparator<DisplayObject>() {
            @Override
            public int compare(DisplayObject o1, DisplayObject o2) {
                return o1.getPosition().y - o2.getPosition().y;
            }
        });

        super.draw(g);
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys){

        super.update(pressedKeys,heldKeys);

        if (turnsActive) {
            if ((System.currentTimeMillis() - previousTurnTime) >= turnLength) {//Turn ended
                turnUpdate();
                previousTurnTime = System.currentTimeMillis();
            }
        }
    }

    void turnUpdate(){

//        System.out.println("Turn go!");
        //Run the turn update on each sprite, move safe
        ArrayList<DisplayObject> spriteList = this.getChildren();
        for (DisplayObject obj : spriteList){
            GridSprite s = (GridSprite) obj;
            s.gridTurnUpdate();
            if (s.getId() == "Player"){}
                centerPointOnScreen(s.getPosition().x,s.getPosition().y);
        }
    }

    //Returns true if successful move, false otherwise
    public boolean swapSprites(Point start, Point end) {

        if (start.x >= 0 && start.x < gridX && start.y >= 0 && start.y < gridY && end.x >= 0 && end.x < gridX && end.y >= 0 && end.y < gridY) {
            GridSprite temp = sprites[end.x][end.y].getSprite();
            sprites[end.x][end.y].setSprite(sprites[start.x][start.y].getSprite());
            sprites[start.x][start.y].setSprite(temp);
            if (sprites[end.x][end.y].getSprite() != null) {
                sprites[end.x][end.y].getSprite().setPosition(gridtoGamePoint(end));
                sprites[end.x][end.y].getSprite().setGridPosition(end);
            }
            if (sprites[start.x][start.y].getSprite() != null){
                sprites[start.x][start.y].getSprite().setPosition(gridtoGamePoint(start));
                sprites[start.x][start.y].getSprite().setGridPosition(start);
            }
            return true;
        }
        return false;
    }

    public boolean swapSprites(Point start, Point end, long timems) {

        if (start.x >= 0 && start.x < gridX && start.y >= 0 && start.y < gridY && end.x >= 0 && end.x < gridX && end.y >= 0 && end.y < gridY) {
            GridSprite temp = sprites[end.x][end.y].getSprite();
            sprites[end.x][end.y].setSprite(sprites[start.x][start.y].getSprite());
            sprites[start.x][start.y].setSprite(temp);
            if (sprites[end.x][end.y].getSprite() != null) {
                sprites[end.x][end.y].getSprite().moveToPosition(gridtoGamePoint(end),timems);
                sprites[end.x][end.y].getSprite().setGridPosition(end);
            }
            if (sprites[start.x][start.y].getSprite() != null){
                sprites[start.x][start.y].getSprite().moveToPosition(gridtoGamePoint(start),timems);
                sprites[start.x][start.y].getSprite().setGridPosition(start);
            }
            return true;
        }
        return false;
    }

    public void startTurns(){
        previousTurnTime = System.currentTimeMillis();
        turnsActive = true;
    }

    public void stopTurns(){
        turnsActive = false;
    }

    public GridSprite getSpriteAtGridPoint(Point p){
        if (p.x >= 0 && p.x < gridX && p.y >= 0 && p.y < gridY)
            return sprites[p.x][p.y].getSprite();
        else
            return null;
    }

    //Grid size is total, but grid points are 0-indexed, like array rules
    public void setUpGrid(int gridX, int gridY, int gameX, int gameY, int screenX, int screenY){
        gridxScale = gameX / gridX;
        gridyScale = gameY / gridY;
        this.gridX = gridX;
        this.gridY = gridY;
        this.screenX = screenX;
        this.screenY = screenY;
        sprites = new GridCell[gridX][gridY];
        for(int i = 0; i < gridX; i++){
            for(int j = 0; j < gridY; j++){
                sprites[i][j] = new GridCell(i,j);
            }
        }

        boolean up,left,down,right;
        for(int i = 0; i < gridX; i++){
            left = i> 0;
            right = i < gridX-1;
            for(int j = 0; j < gridY; j++) {
                up = j > 0;
                down = j < gridY - 1;
                if (up)
                    sprites[i][j].neighbors.put(Direction.UP, sprites[i][j-1]);
                if (down)
                    sprites[i][j].neighbors.put(Direction.DOWN, sprites[i][j+1]);
                if (left)
                    sprites[i][j].neighbors.put(Direction.LEFT, sprites[i-1][j]);
                if (right)
                    sprites[i][j].neighbors.put(Direction.RIGHT, sprites[i+1][j]);
            }
        }
    }

    //Input a screen size (same as gameX and gameY in setUpGrid)--set the offset to center the chosen point on screen. Round down functionality is commented out, may be relevant later.
    public void centerPointOnScreen(int x,int y){

        int xOffset = screenX/2 - x;
        int yOffset = screenY/2 - y;

        if (xOffset > 0)
            xOffset = 0;
        else if (screenX - xOffset > gridX*gridxScale) {
            xOffset = screenX - gridX * gridxScale;
        }

        if (yOffset > 0)
            yOffset = 0;
        else if (screenY - yOffset > gridY*gridyScale)
            yOffset = screenY - gridY*gridyScale;

        //TODO Change this to work and add tweening
        Tween t = new Tween(this);
        t.animate(TweenableParams.X,getPosition().x,xOffset,scrollSpeed);
        t.animate(TweenableParams.Y,getPosition().y,yOffset,scrollSpeed);
        TweenJuggler.getInstance().addTweenNonRedundant(t,this);
    }

    public int gridToGameX(int x){
        return x*gridxScale + gridxScale/2;
    }

    public int gridToGameY(int y){
        return  y*gridyScale + gridyScale/2;
    }

    public Point gridtoGamePoint(Point p){
        return new Point(gridToGameX(p.x),gridToGameY(p.y));
    }

    public Point directionToGridVector(Direction d){
        switch (d) {
            case UP:
                return new Point(0,-1);
            case DOWN:
                return new Point(0,1);
            case LEFT:
                return new Point(-1,0);
            case RIGHT:
                return new Point(1,0);
        }
        return null;
    }

    public boolean existsValidPath(Point first, Direction direction){
        if(sprites[first.x][first.y].neighbors.get(direction) == null){
            return false;
        }
        return true;
    }

    public Direction gridVectorToDirection(Point p){
        if (p.equals(new Point(0,-1)))
            return Direction.UP;
        else if (p.equals(new Point(0,1)))
            return Direction.DOWN;
        else if (p.equals(new Point(-1,0)))
            return Direction.LEFT;
        else if (p.equals(new Point(1,0)))
            return Direction.RIGHT;
        return null;
    }

    //gridX is the same as sprites.length, gridY is the same as sprites[0].length
    public void addToGrid(GridSprite s, int x, int y){
        if (x >= 0 && x < gridX && y >= 0 && y < gridY){
            sprites[x][y].setSprite(s);

            s.setPosition(gridtoGamePoint(new Point(x,y)));
            s.setGridPosition(new Point(x,y));
            addChild(s);
        }
    }


    public void addWall(Point initial, Direction direction){
        GridCell original = sprites[initial.x][initial.y];
        GridCell pointer = original.neighbors.get(direction);
        if(pointer !=null){
            original.neighbors.remove(direction);
            pointer.neighbors.remove(direction.opposite());
            //Now add wall to array at right position
            boolean horizontal  = direction == Direction.DOWN || direction == Direction.UP ? true : false;
            int x = (gridToGameX(original.location.x) + gridToGameX(pointer.location.x)) /2;
            int y = (gridToGameY(original.location.y) + gridToGameY(pointer.location.y)) / 2;
            Point wallPosition = new Point(x,y);
            GridWallSprite wall = new GridWallSprite("Wall",horizontal, wallPosition);
            addChild(wall);
        } else {
            Point fakePoint = new Point(original.location.x,original.location.y);
            switch(direction){
                case LEFT:
                    fakePoint.translate(-1,0);
                    break;
                case RIGHT:
                    fakePoint.translate(1,0);
                    break;
                case UP:
                    fakePoint.translate(0,-1);
                    break;
                case DOWN:
                    fakePoint.translate(0,1);
                    break;
            }
            boolean horizontal  = direction == Direction.DOWN || direction == Direction.UP ? true : false;
            int x = (gridToGameX(original.location.x) + gridToGameX(fakePoint.x)) /2;
            int y = (gridToGameY(original.location.y) + gridToGameY(fakePoint.y)) / 2;
            Point wallPosition = new Point(x,y);
            GridWallSprite wall = new GridWallSprite("Wall",horizontal, wallPosition);
            addChild(wall);
        }
    }

    void resetAStarGrid(){
        for(int i = 0; i < gridX; i++){
            for(int j = 0; j < gridY; j++){
                sprites[i][j].resetAStarValues();
            }
        }
    }

    public void resetLevel(){
        sprites = null;
        turnsActive = false;
        for(int i = 0; i < this.getChildren().size(); i+=0){
            this.removeChildByIndex(0);
        }
    }

    public float getTurnLength() {
        return turnLength;
    }

    public void setTurnLength(float turnLength) {
        this.turnLength = turnLength;
    }

    public void setPlayer(PlayerSprite player) {
        this.player = player;
    }
}

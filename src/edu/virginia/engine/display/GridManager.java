package edu.virginia.engine.display;

import edu.virginia.engine.util.Direction;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Created by Guillaume Bailey on 3/19/2016.
 */
public class GridManager {
    //Singleton stuff
    private static GridManager ourInstance = new GridManager();

    public static GridManager getInstance() {
        return ourInstance;
    }

    private GridManager() {
    }

    //Overall grid scalers and size
    int gridX;
    int gridY;
    int gridxScale;
    int gridyScale;
    int gridxOffset;
    int gridyOffset;

    public GridCell[][] sprites = null;

    float turnLength = 1000;
    long previousTurnTime;
    boolean turnsActive = false;

    ArrayList<GridSprite> walls = new ArrayList<GridSprite>();


    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D)g;


        ArrayList<GridSprite> spriteList = new ArrayList<GridSprite>();
        spriteList.addAll(walls);

        if(sprites !=null){
            for (int x = 0; x < sprites.length; x++){
                for (int y = 0; y < sprites[x].length; y++){
                    if (sprites[x][y].getSprite() != null)
                        spriteList.add(sprites[x][y].getSprite());
                    g2d.drawRect(gridToGameX(x)-gridxScale/2,gridToGameY(y)-gridyScale/2,gridxScale,gridyScale);
                }
            }
        }

        spriteList.sort(new Comparator<GridSprite>() {
            @Override
            public int compare(GridSprite o1, GridSprite o2) {
                return o1.getPosition().y - o2.getPosition().y;
            }
        });

        for(GridSprite s : spriteList){
            s.draw(g);
        }
    }

    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys){

        //Run the frame update on each sprite, move safe
        ArrayList<GridSprite> spriteList = new ArrayList<GridSprite>();
        for (int x = 0; x < sprites.length; x++){
            for (int y = 0; y < sprites[x].length; y++) {
                if (sprites[x][y].getSprite() != null){
                    spriteList.add(sprites[x][y].getSprite());
                }
            }
        }
        for (GridSprite s : spriteList){
            s.update(pressedKeys,heldKeys);
        }

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
        ArrayList<GridSprite> spriteList = new ArrayList<GridSprite>();
        for (int x = 0; x < sprites.length; x++){
            for (int y = 0; y < sprites[x].length; y++) {
                if (sprites[x][y].getSprite() != null){
                    spriteList.add(sprites[x][y].getSprite());
                }
            }
        }
        for (GridSprite s : spriteList){
            s.gridTurnUpdate();
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
    public void setGridSize(int gridX, int gridY, int gameX, int gameY){
        gridxScale = gameX / gridX;
        gridyScale = gameY / gridY;
        this.gridX = gridX;
        this.gridY = gridY;
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

    //Input a screen size (same as gameX and gameY in setGridSize)--set the offset to center the chosen point on screen. Round down functionality is commented out, may be relevant later.
    public void centerGridPointOnScreen(int x,int y, int screenX, int screenY){
        gridxOffset = screenX/2 - (x)*gridxScale - gridxScale/2;
        //gridxOffset -= gridxOffset%gridxScale;
        gridyOffset = screenY/2 - (y)*gridyScale - gridyScale/2;
        //gridyOffset -= gridyOffset%gridyScale;
    }

    public int gridToGameX(int x){
        return x*gridyScale + gridyScale/2 + gridxOffset;
    }

    public int gridToGameY(int y){
        return  y*gridyScale + gridxScale/2 + gridyOffset;
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
        if (x < gridX && y < gridY){
            sprites[x][y].setSprite(s);
        }

        s.setPosition(gridtoGamePoint(new Point(x,y)));
        s.setGridPosition(new Point(x,y));
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
            GridWallSprite wall = new GridWallSprite("wall",horizontal, wallPosition);
            walls.add(wall);
        }
    }

    void resetAStarGrid(){
        for(int i = 0; i < gridX; i++){
            for(int j = 0; j < gridY; j++){
                sprites[i][j].resetAStarValues();
            }
        }
    }

    public float getTurnLength() {
        return turnLength;
    }

    public void setTurnLength(float turnLength) {
        this.turnLength = turnLength;
    }
}

package edu.virginia.engine.display;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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

    GridSprite[][] sprites = null;

    float turnLength = 1000;
    long previousTurnTime;
    boolean turnsActive = false;

    ArrayList<String> activeKeyPresses = new ArrayList<>();

    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D)g;

        for (int x = 0; x < sprites.length; x++){
            for (int y = 0; y < sprites[x].length; y++){
                if (sprites[x][y] != null)
                    sprites[x][y].draw(g);
                g2d.drawRect(gridToGameX(x)-gridxScale/2,gridToGameY(y)-gridyScale/2,gridxScale,gridyScale);
            }
        }
    }

    public void update(ArrayList<String> pressedKeys){

        if (turnsActive) {
            if (!(activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_LEFT)) || activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_UP))
                    || activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_RIGHT)) || activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_DOWN)))) {//No previous direction input this turn
                if (pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_UP))) {
                    activeKeyPresses.add(KeyEvent.getKeyText(KeyEvent.VK_UP));
                }
                else if (pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_DOWN))) {
                    activeKeyPresses.add(KeyEvent.getKeyText(KeyEvent.VK_DOWN));
                }
                else if (pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_LEFT))) {
                    activeKeyPresses.add(KeyEvent.getKeyText(KeyEvent.VK_LEFT));
                }
                else if (pressedKeys.contains(KeyEvent.getKeyText(KeyEvent.VK_RIGHT))) {
                    activeKeyPresses.add(KeyEvent.getKeyText(KeyEvent.VK_RIGHT));
                }
            }
            if (!activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_Z))){
                activeKeyPresses.add(KeyEvent.getKeyText(KeyEvent.VK_Z));
            }

            if ((System.currentTimeMillis() - previousTurnTime) >= turnLength) {//Turn ended
                turnUpdate();
                previousTurnTime = System.currentTimeMillis();
            }
        }
    }

    void turnUpdate(){

        System.out.println("Turn go!");
        //Run the turn update on each sprite, move safe
        ArrayList<GridSprite> spriteList = new ArrayList<>();
        for (int x = 0; x < sprites.length; x++){
            for (int y = 0; y < sprites[x].length; y++) {
                if (sprites[x][y] != null){
                    spriteList.add(sprites[x][y]);
                }
            }
        }
        for (GridSprite s : spriteList)
            s.gridTurnUpdate(activeKeyPresses);

        activeKeyPresses.clear();
    }

    //Returns true if successful move, false otherwise
    public boolean moveSprite(Point start, Point end) {

        if (start.x >= 0 && start.x < gridX && start.y >= 0 && start.y < gridY && end.x >= 0 && end.x < gridX && end.y >= 0 && end.y < gridY) {
            GridSprite temp = sprites[end.x][end.y];
            sprites[end.x][end.y] = sprites[start.x][start.y];
            sprites[start.x][start.y] = temp;
            if (sprites[end.x][end.y] != null) {
                spriteToGridPosition(sprites[end.x][end.y], end.x, end.y);
            }
            if (sprites[start.x][start.y] != null){
                spriteToGridPosition(sprites[start.x][start.y],start.x,start.y);
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
            return sprites[p.x][p.y];
        else
            return null;
    }

    //Grid size is total, but grid points are 0-indexed, like array rules
    public void setGridSize(int gridX, int gridY, int gameX, int gameY){
        gridxScale = gameX / gridX;
        gridyScale = gameY / gridY;
        this.gridX = gridX;
        this.gridY = gridY;
        sprites = new GridSprite[gridX][gridY];
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

    //gridX is the same as sprites.length, gridY is the same as sprites[0].length
    public void addToGrid(GridSprite s, int x, int y){
        if (x < gridX && y < gridY){
            sprites[x][y] = s;
        }
        spriteToGridPosition(s,x,y);
    }

    void spriteToGridPosition(GridSprite s, int x, int y){
        s.setPosition(new Point(gridToGameX(x),gridToGameY(y)));
        s.setGridPosition(new Point(x,y));
    }

    public float getTurnLength() {
        return turnLength;
    }

    public void setTurnLength(float turnLength) {
        this.turnLength = turnLength;
    }
}

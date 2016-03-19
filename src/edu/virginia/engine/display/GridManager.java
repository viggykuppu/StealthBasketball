package edu.virginia.engine.display;

import java.awt.*;

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

    Sprite[][] sprites = null;

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

    //Grid size is total, but grid points are 0-indexed, like array rules
    public void setGridSize(int gridX, int gridY, int gameX, int gameY){
        gridxScale = gameX / gridX;
        gridyScale = gameY / gridY;
        this.gridX = gridX;
        this.gridY = gridY;
        sprites = new Sprite[gridX][gridY];
        System.out.println("Y scale,offset: " + gridyScale + " " + gridyOffset);
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

    //gridX is the same as sprites.length, gridY is the same as sprites[0].length
    public void addToGrid(Sprite s, int x, int y){
        if (x < gridX && y < gridY){
            sprites[x][y] = s;
        }
        s.setPosition(new Point(gridToGameX(x),gridToGameY(y)));
    }
}

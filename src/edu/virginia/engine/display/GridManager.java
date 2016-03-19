package edu.virginia.engine.display;

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

    public void SetGridSize(int gridX, int gridY, int gameX, int gameY){
        gridxScale = gameX / gridX;
        gridxOffset = (gameX % gridX) / 2;
        gridyScale = gameY / gridY;
        gridyOffset = (gameY % gridY) / 2;
        this.gridX = gridX;
        this.gridY = gridY;
        sprites = new Sprite[gridX][gridY];
    }

    int GridToGameX(int gridX){
        return gridX*gridyScale + gridxOffset;
    }

    int GridToGameY(){
        return  gridY*gridyScale + gridyOffset;
    }

    public void AddToGrid(Sprite s, int x, int y){
        if (x < gridX && y < gridY){
            sprites[x][y] = s;
        }
    }
}

package edu.virginia.engine.display;

import edu.virginia.engine.util.Direction;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vignesh on 3/20/16.
 */
public class GridCell {
    Point location;
    //f = g + h
    int f;
    int g;
    int h;
    public Map<Direction,GridCell> neighbors =  new HashMap<Direction,GridCell>();
    private GridSprite sprite = null;


    public GridCell(int x, int y){
        location = new Point(x,y);
    }

    public void setSprite(GridSprite sprite){
            this.sprite = sprite;
    }

    public GridSprite getSprite(){
        return this.sprite;
    }

    public void removeSprite(){
        this.sprite = null;
    }

    public void resetAStarValues(){
        f = 0;
        g = 0;
        h = 0;
    }
}

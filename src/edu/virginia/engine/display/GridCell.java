package edu.virginia.engine.display;

import edu.virginia.engine.util.Direction;

import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vignesh on 3/20/16.
 */
public class GridCell {
    Point location;
    //f = g + h, values for A* map navigation
    int f;
    int g;
    int h;
    public Map<Direction,GridCell> neighbors =  new HashMap<Direction,GridCell>();

    private EnumMap<GridSpriteTypes,GridSprite> sprites = new EnumMap(GridSpriteTypes.class);


    public GridCell(int x, int y){
        location = new Point(x,y);
    }

    public void addSprite(GridSprite sprite, GridSpriteTypes spriteType){
            sprites.put(spriteType,sprite);
    }

    public GridSprite getSprite(GridSpriteTypes spriteType){
        return sprites.get(spriteType);
    }

    public EnumMap<GridSpriteTypes,GridSprite> getSprites() { return sprites; }

    public void removeSpriteAt( GridSpriteTypes spriteType ){
        sprites.remove(spriteType);
    }

    public void resetAStarValues(){
        f = 0;
        g = 0;
        h = 0;
    }
}

package edu.virginia.engine.display;

/**
 * Created by Viggy on 3/23/2016.
 */
public class GridWallSprite extends GridSprite{

    private boolean horizontal;

    public GridWallSprite(String id, boolean horizontal) {
        super(id,"wall.png");
        this.horizontal = horizontal;
    }

    public void toggleDirection(){
        this.horizontal = !this.horizontal;
    }


}

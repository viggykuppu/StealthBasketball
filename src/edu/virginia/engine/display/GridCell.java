package edu.virginia.engine.display;

/**
 * Created by vignesh on 3/20/16.
 */
public class GridCell {
    GridCell up = null;
    GridCell down = null;
    GridCell right = null;
    GridCell left  = null;
    private GridSprite sprite = null;

    public GridCell(){

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
}

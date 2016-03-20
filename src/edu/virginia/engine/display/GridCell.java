package edu.virginia.engine.display;

/**
 * Created by vignesh on 3/20/16.
 */
public class GridCell {
    GridCell up;
    GridCell down;
    GridCell right;
    GridCell left;
    private GridSprite sprite;

    public GridCell(){

    }

    public void setSprite(GridSprite sprite){
        if(sprite != null)
            this.sprite = sprite;
    }

    public GridSprite getSprite(){
        return this.sprite;
    }

    public void removeSprite(){
        this.sprite = null;
    }
}

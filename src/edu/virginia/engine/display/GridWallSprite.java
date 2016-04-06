package edu.virginia.engine.display;

import java.awt.*;

/**
 * Created by Viggy on 3/23/2016.
 */
public class GridWallSprite extends GridSprite{

    private boolean horizontal;

    public GridWallSprite(String id, boolean horizontal, Point position) {
        super(id,"wall.png");
        this.setPosition(position);
        this.setPivotPoint(new Point(50,13));
        this.horizontal = horizontal;
        if(!this.horizontal){
            this.setRotation(90);
        }
    }

    public void toggleDirection(){
        this.horizontal = !this.horizontal;
        if(this.horizontal){
            this.setRotation(0);
        } else {
            this.setRotation(90);
        }
    }

    @Override
    public void draw(Graphics g){
        super.draw(g);
    }


}

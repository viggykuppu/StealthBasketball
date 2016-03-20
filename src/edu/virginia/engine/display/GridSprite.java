package edu.virginia.engine.display;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/19/2016.
 */
public class GridSprite extends Sprite {

    Point gridPosition;

    public GridSprite(String id) {
        super(id);
    }

    public GridSprite(String id, String imageFileName) {
        super(id, imageFileName);
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);
    }

    public void gridTurnUpdate(ArrayList<Integer> activeKeyPresses){

    }

    //Returns true if successful move, false if not
    boolean moveOnGrid(int dx, int dy){

        Point destination = new Point();
        destination.x = gridPosition.x;
        destination.y = gridPosition.y;
        destination.translate(dx,dy);
        if (GridManager.getInstance().getSpriteAtGridPoint(destination) == null){
            if (GridManager.getInstance().moveSprite(gridPosition,destination))
                return true;
        }
        return false;
    }

    public Point getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(Point gridPosition) {
        this.gridPosition = gridPosition;
    }
}

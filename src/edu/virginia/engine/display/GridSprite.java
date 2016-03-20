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
    public void update(ArrayList<String> pressedKeys) {
        super.update(pressedKeys);
    }

    public void gridTurnUpdate(ArrayList<String> activeKeyPresses){

        System.out.println(activeKeyPresses);

        //Player movement code
        if (activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_UP))) {
            moveOnGrid(0,-1);
        }
        if (activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_DOWN))) {
            moveOnGrid(0,-1);
        }
        if (activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_LEFT))) {
            moveOnGrid(0,-1);
        }
        if (activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_RIGHT))) {
            moveOnGrid(0,-1);
        }
    }

    void moveOnGrid(int dx, int dy){

        Point destination = new Point();
        destination.x = gridPosition.x;
        destination.y = gridPosition.y;
        destination.translate(dx,dy);
        if (GridManager.getInstance().getSpriteAtGridPoint(destination) == null){
            GridManager.getInstance().moveSprite(gridPosition,destination);
        }
    }

    public Point getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(Point gridPosition) {
        this.gridPosition = gridPosition;
    }
}

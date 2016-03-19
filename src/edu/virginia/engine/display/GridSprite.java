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

        //Player movement code
        if (activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_UP))) {
            Point destination = gridPosition;
            destination.translate(0,-1);
            if (GridManager.getInstance().getSpriteAtGridPoint(destination) != null){
                GridManager.getInstance().moveSprite(gridPosition,destination);
                gridPosition = destination;
            }
        }
        if (activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_DOWN))) {
            Point destination = gridPosition;
            destination.translate(0,1);
            if (GridManager.getInstance().getSpriteAtGridPoint(destination) != null){
                GridManager.getInstance().moveSprite(gridPosition,destination);
                gridPosition = destination;
            }
        }
        if (activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_LEFT))) {
            Point destination = gridPosition;
            destination.translate(-1,0);
            if (GridManager.getInstance().getSpriteAtGridPoint(destination) != null){
                GridManager.getInstance().moveSprite(gridPosition,destination);
                gridPosition = destination;
            }
        }
        if (activeKeyPresses.contains(KeyEvent.getKeyText(KeyEvent.VK_RIGHT))) {
            Point destination = gridPosition;
            destination.translate(1,0);
            if (GridManager.getInstance().getSpriteAtGridPoint(destination) != null){
                GridManager.getInstance().moveSprite(gridPosition,destination);
                gridPosition = destination;
            }
        }
    }

    public Point getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(Point gridPosition) {
        this.gridPosition = gridPosition;
    }
}

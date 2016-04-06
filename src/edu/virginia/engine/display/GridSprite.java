package edu.virginia.engine.display;

import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.tween.TweenableParams;
import edu.virginia.engine.util.Direction;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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


    public void draw(Graphics g, int xOffset, int yOffset){
        Point p = getPosition();
        setPosition(new Point(p.x + xOffset,p.y + yOffset));
        super.draw(g);
        setPosition(p);
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys) { super.update(pressedKeys,heldKeys); }

    public void gridTurnUpdate(){
    }

    //Returns true if successful move, false if not
    boolean moveOnGrid(int dx, int dy){

        Point destination = new Point();
        destination.x = gridPosition.x;
        destination.y = gridPosition.y;
        destination.translate(dx,dy);
        Direction direction = GridManager.getInstance().gridVectorToDirection(new Point(dx,dy));
        if (GridManager.getInstance().getSpriteAtGridPoint(destination) == null && GridManager.getInstance().existsValidPath(gridPosition,direction)){
            if (GridManager.getInstance().swapSprites(gridPosition,destination)) {
                gridPosition.x = destination.x;
                gridPosition.y = destination.y;
                return true;
            }
        }
        return false;
    }

    boolean moveOnGrid(int dx, int dy, long timems){

        Point destination = new Point();
        destination.x = gridPosition.x;
        destination.y = gridPosition.y;
        destination.translate(dx,dy);
        Direction direction = GridManager.getInstance().gridVectorToDirection(new Point(dx,dy));
        if (GridManager.getInstance().getSpriteAtGridPoint(destination) == null && GridManager.getInstance().existsValidPath(gridPosition,direction)){
            if (GridManager.getInstance().swapSprites(gridPosition,destination,timems)) {
                gridPosition.x = destination.x;
                gridPosition.y = destination.y;
                return true;
            }
        }
        if(GridManager.getInstance().getSpriteAtGridPoint(destination) != null){
            if(GridManager.getInstance().getSpriteAtGridPoint(destination).getId().equals("Player")){
                GridManager.getInstance().levelFailed = true;
            }
        }
        return false;
    }

    public void moveToPosition(Point dest, long timems){
        Tween t = new Tween(this);
        t.animate(TweenableParams.X,getPosition().x,dest.x,timems);
        t.animate(TweenableParams.Y,getPosition().y,dest.y,timems);
        TweenJuggler.getInstance().addTweenNonRedundant(t,this);
    }

    public Point getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(Point gridPosition) {
        this.gridPosition = gridPosition;
    }

    public ArrayList<DisplayObject> checkRay(Point start, Point end){

        Line2D ray = new Line2D.Float(start,end);
        Area rayArea = new Area(ray);

        ArrayList<DisplayObject> intersectingObjects = new ArrayList<>();

        for (DisplayObject o : GridManager.getInstance().getChildren()){
            //TODO DO THIS
            if (o.collidesWith(rayArea))
                intersectingObjects.add(o);
        }

        return intersectingObjects;
    }
}

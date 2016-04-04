package edu.virginia.engine.display;

import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.tween.TweenTransitions;
import edu.virginia.engine.tween.TweenableParams;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/20/2016.
 */
public class BallSprite extends Sprite {

    Point playerOffset = new Point(0,-25);
    Tween ballFollowPlayer;
    GridManager gridManager;

    public BallSprite(String id, String imageFileName) {

        super(id, imageFileName);
        gridManager = GridManager.getInstance();
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys) {
        super.update(pressedKeys,heldKeys);

        ArrayList<DisplayObject> spriteList = gridManager.getChildren();
        for (DisplayObject obj : spriteList) {
            GridSprite s = (GridSprite) obj;
            if (this.collidesWith(s) && s.getId() != "Player") {
                rebound();
            }
        }
    }

    public void pathToGridPoint(Point gridDest, long timems) {
        Point endPosition = new Point(gridDest);
        endPosition = GridManager.getInstance().gridtoGamePoint(endPosition);
        endPosition.x += playerOffset.x;
        endPosition.y += playerOffset.y;

        ballFollowPlayer = new Tween(this);
        ballFollowPlayer.animate(TweenableParams.X,getPosition().x,endPosition.x,timems);
        ballFollowPlayer.animate(TweenableParams.Y,getPosition().y,endPosition.y,timems);
        TweenJuggler.getInstance().addTweenNonRedundant(ballFollowPlayer,this);
    }

    /*
        Either stops the ball or reverses vector
     */
    public void rebound() {

    }

    public Point getPlayerOffset() {
        return playerOffset;
    }

    public void setPlayerOffset(Point playerOffset) {
        this.playerOffset = playerOffset;
    }
}

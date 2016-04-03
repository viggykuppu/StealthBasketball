package edu.virginia.engine.display;

import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.tween.TweenTransitions;
import edu.virginia.engine.tween.TweenableParams;
import edu.virginia.engine.util.Direction;
import javafx.scene.shape.Ellipse;

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
                this.getCollisionNormal(s);
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
        Calculates which rectangle side the ball is closest to
        Returns: Direction Enum
     */
    public Direction getCollisionNormal(GridSprite s) {
        Rectangle area = s.getHitbox().getBounds();

        // the sides of the rectangle
        double left = area.getX();
        double right = left + area.getWidth();
        double up = area.getY();
        double down = up + area.getHeight();

        // get the closest side of the rectangle
        Rectangle ball = this.getHitCircle().getBounds();

        // get the center of ball
        double centerX = ball.getX() + (ball.getWidth() / 2);
        double centerY = ball.getY() + (ball.getHeight() / 2);

        // do the math
        double distLeft = Math.abs(left - centerX);
        double distRight = Math.abs(right - centerX);
        double distUp = Math.abs(up - centerY);
        double distDown = Math.abs(down - centerY);

        Direction ret = Direction.LEFT;
        double smallest = distLeft;

        if (distRight < smallest) {
            ret = Direction.RIGHT;
            smallest = distRight;
        }

        if (distUp < smallest) {
            ret = Direction.UP;
            smallest = distUp;
        }

        if (distDown < smallest) {
            ret = Direction.DOWN;
        }

        return ret;
    }

    public Point getPlayerOffset() {
        return playerOffset;
    }

    public void setPlayerOffset(Point playerOffset) {
        this.playerOffset = playerOffset;
    }
}

package edu.virginia.engine.display;

import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.tween.TweenTransitions;
import edu.virginia.engine.tween.TweenableParams;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/20/2016.
 */
public class BallSprite extends Sprite {

    Point playerOffset = new Point(0,-25);

    public BallSprite(String id, String imageFileName) {
        super(id, imageFileName);
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);
    }

    public void pathToGridPoint(Point gridDest) {
        Point endPosition = new Point(gridDest);
        endPosition = GridManager.getInstance().gridtoGamePoint(endPosition);
        endPosition.x += playerOffset.x;
        endPosition.y += playerOffset.y;

        Tween ballFollowPlayer = new Tween(this);
        ballFollowPlayer.animate(TweenableParams.X,getPosition().x,endPosition.x,1);
        ballFollowPlayer.animate(TweenableParams.Y,getPosition().y,endPosition.y,1);
        TweenJuggler.getInstance().addTweenNonRedundant(ballFollowPlayer,this);
    }

    public Point getPlayerOffset() {
        return playerOffset;
    }

    public void setPlayerOffset(Point playerOffset) {
        this.playerOffset = playerOffset;
    }
}

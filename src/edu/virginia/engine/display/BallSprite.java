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

    public BallSprite(String id) {
        super(id);
    }

    public BallSprite(String id, String imageFileName) {
        super(id, imageFileName);
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);
    }

    public void followPlayer(Point gridStart, Point gridEnd) {
        Point startPosition = new Point(gridStart);
        startPosition = GridManager.getInstance().gridtoGamePoint(startPosition);
        startPosition.x += playerOffset.x;
        startPosition.y += playerOffset.y;
        Point endPosition = new Point(gridEnd);
        endPosition = GridManager.getInstance().gridtoGamePoint(endPosition);
        endPosition.x += playerOffset.x;
        endPosition.y += playerOffset.y;

        System.out.println(startPosition + " " + endPosition);

        Tween ballFollowPlayer = new Tween(this);
        ballFollowPlayer.animate(TweenableParams.X,startPosition.x,endPosition.x,500);
        ballFollowPlayer.animate(TweenableParams.Y,startPosition.y,endPosition.y,500);
        TweenJuggler.getInstance().addTween(ballFollowPlayer);
    }

    public Point getPlayerOffset() {
        return playerOffset;
    }

    public void setPlayerOffset(Point playerOffset) {
        this.playerOffset = playerOffset;
    }
}

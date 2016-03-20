package edu.virginia.engine.display;

import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/20/2016.
 */
public class BallSprite extends Sprite {

    public BallSprite(String id) {
        super(id);
    }

    public BallSprite(String id, String imageFileName) {
        super(id, imageFileName);
    }

    @Override
    public void update(ArrayList<String> pressedKeys) {
        super.update(pressedKeys);
    }
}

package edu.virginia.engine.display;

import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 4/8/2016.
 */
public class TeleporterSprite extends GridSprite {

    ArrayList<String> animation = new ArrayList<>();
    static double animationTime = 1; //In seconds

    public TeleporterSprite(String id, String imageFileName) {
        super(id, imageFileName,GridSpriteTypes.Teleporter);
        for (int i = 0; i<24; i++)
            animation.add("Teleporter/frame_" + i + "_delay-0.04s.gif");
        readAnimation("TeleporterAnim",animation,animationTime);
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys){
        super.update(pressedKeys, heldKeys);
        looping_Animate("TeleporterAnim");
    }
}

package edu.virginia.engine.display;

import java.awt.*;

/**
 * Created by Guillaume Bailey on 4/3/2016.
 */
public class PlayerPingEffect extends DisplayObject {

    private int radius = 0;
    private DisplayObject player;

    public PlayerPingEffect(String id, DisplayObject player) {
        super(id);
        this.player = player;
    }

    @Override
    public void draw(Graphics g){

        applyTransformations((Graphics2D)g);//Necessary for alpha
        Point center = new Point(player.getPosition());
        center.x -= radius;
        center.y -= radius;
        g.drawOval(center.x,center.y,radius*2,radius*2);
        reverseTransformations((Graphics2D)g);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}

package edu.virginia.engine.display;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Created by vignesh on 3/20/16.
 */
public class GridGuardSprite extends GridSprite{
    PlayerSprite player;
    double sightRadius = 4;


    public GridGuardSprite(String id,PlayerSprite player) {
        super(id);
        this.player = player;
    }

    public GridGuardSprite(String id, String imageFileName, PlayerSprite player) {
        super(id, imageFileName);
        this.player = player;
    }

    public boolean canDetectPlayer(){
        double distance = this.getGridPosition().distanceSq(player.getGridPosition());
        return distance <= 4;
    }

    @Override
    public void gridTurnUpdate(ArrayList<Integer> activeKeyPresses){
        if(this.canDetectPlayer()){
            aStar(player.getGridPosition());
        } else {
            randomMove();
        }
    }

    public void randomMove(){

    }

    public void aStar(Point PlayerPosition){

    }

    public void setSightRadius(double sightRadius){
        this.sightRadius = sightRadius;
    }

    public double getSightRadius(){
        return this.sightRadius;
    }

}

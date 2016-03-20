package edu.virginia.engine.display;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Created by vignesh on 3/20/16.
 */
public class GridGuardSprite extends GridSprite{
    PlayerSprite player;
    double sightRadius = 4;
    Point lastKnownPlayerLocation;
    GridGuardState guardState = GridGuardState.idle;

    private enum GridGuardState{
        playerVisible,lostPlayer,idle;
    }


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
            this.guardState = GridGuardState.playerVisible;
            aStar(player.getGridPosition());
        } else {
            if(this.guardState == GridGuardState.playerVisible)
                this.guardState = GridGuardState.lostPlayer;
            aStar(lastKnownPlayerLocation);
        }
    }

    public void aStar(Point PlayerPosition){
        GridCell[][] graph = GridManager.getInstance().sprites;
        Point curr = this.getGridPosition();
        while(true){
            
        }

    }

    public int getMDistance(Point a, Point b){
        return Math.abs(a.x-b.x) + Math.abs(a.y-b.y);
    }

    public void setSightRadius(double sightRadius){
        this.sightRadius = sightRadius;
    }

    public double getSightRadius(){
        return this.sightRadius;
    }

}

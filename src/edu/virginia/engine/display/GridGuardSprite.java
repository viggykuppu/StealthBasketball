package edu.virginia.engine.display;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

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
        if(this.canDetectPlayer() || true){
            this.guardState = GridGuardState.playerVisible;
            aStar(player.getGridPosition());
        } else {
            if(this.guardState == GridGuardState.playerVisible){
                this.guardState = GridGuardState.lostPlayer;
                aStar(lastKnownPlayerLocation);
            }

        }
    }

    public void aStar(Point playerPosition){
        System.out.println("star");
        GridCell[][] graph = GridManager.getInstance().sprites;
        GridCell curr = graph[this.getGridPosition().x][this.getGridPosition().y];
        curr.cameFrom = null;
        PriorityQueue<GridCell> q = new PriorityQueue<GridCell>(new Comparator<GridCell>() {
            @Override
            public int compare(GridCell gridCell, GridCell t1) {
                return gridCell.f - t1.f;
            }
        });
        HashSet<Point> closedSet = new HashSet<Point>();
        HashSet<Point> seen = new HashSet<Point>();
        q.add(curr);
        seen.add(curr.location);
        curr.g = 0;
        while(!curr.equals(playerPosition)){
            curr = q.poll();
            System.out.println(curr.location);
            if(curr == null){
                System.err.println("A* failed");
            }
            closedSet.add(curr.location);
            for(GridCell node : curr.neighbors){
                if(!closedSet.contains(node.location)){
                    if(!seen.contains(node.location)){
                        seen.add(node.location);
                        q.add(node);
                        node.g = curr.g+1;
                        node.h = this.getMDistance(playerPosition,node.location);
                        node.f = node.h+node.g;
                        node.cameFrom = curr;
                    } else {
                        int tentative_g = curr.g + 1;
                        if(tentative_g >= node.g){
                            continue;
                        } else {
                            node.g = curr.g+1;
                            node.f = node.h+node.g;
                            node.cameFrom = curr;
                        }
                    }
                }
            }
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

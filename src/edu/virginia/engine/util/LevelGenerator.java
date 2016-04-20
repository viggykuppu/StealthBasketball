package edu.virginia.engine.util;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import edu.virginia.engine.display.*;
import org.w3c.dom.events.EventException;
import org.xml.sax.*;
import org.w3c.dom.*;
import com.sun.org.apache.xerces.internal.parsers.XMLParser;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by vignesh on 3/25/16.
 */


public class LevelGenerator {

    public Point screenSize = new Point(1000,500);

    private BallSprite ball = new BallSprite("Ball", "coin.gif");
    private PlayerSprite player = new PlayerSprite("Player", "mario.png", ball);
    private HoopSprite hoop = new HoopSprite("Hoop","hoop.png");
    private ArrayList<GridGuardSprite> guards = new ArrayList<GridGuardSprite>();
    private ArrayList<TeleporterSprite> teleporters = new ArrayList<>();
    private ArrayList<Integer> teleporterPairId = new ArrayList<>();
    private ArrayList<String> stunAnim = new ArrayList<>();
    private Map<Point,ArrayList<Direction>> m = new HashMap<Point,ArrayList<Direction>>();
    private File levelFile;
    public LevelGenerator(String fileName){
        levelFile = new File(fileName);
    }

    public void generateLevel(){
        player.setPivotPoint(new Point(player.getUnscaledWidth() / 2, player.getUnscaledHeight() / 2));
        ball.setPivotPoint(new Point(28, 28));
        stunAnim.add("floryan,mark_stunned.png");
        stunAnim.add("floryan,mark_stunned.png");
        //Parse level file
        int y = 0;
        int x = 0;
        try {
            Scanner s = new Scanner(levelFile);
            while(s.hasNextLine()){
                y++;
                s.nextLine();
            }
            s = new Scanner(levelFile);
            int ly = 0;
            while(s.hasNextLine()){
                String line = s.nextLine();
                String[] cells = line.split(";");
                x = cells.length;
                GridManager.getInstance().setUpGrid(x, y, x*100, y*100,screenSize.x,screenSize.y);
                for(int i = 0; i < x; i++){
                    String[] cellContents = cells[i].split(",");
                    for(int j = 0; j < cellContents.length; j++){
                        handleToken(cellContents[j], new Point(i,ly));
                    }
                }
                ly++;
            }

            //Add objects to grid
            GridManager.getInstance().addToGrid(player,player.getGridPosition().x,player.getGridPosition().y);
            GridManager.getInstance().addToGrid(hoop,hoop.getGridPosition().x,hoop.getGridPosition().y);
            for(GridGuardSprite g : guards){
                GridManager.getInstance().addToGrid(g,g.getGridPosition().x,g.getGridPosition().y);
            }
            int numPairs = 0;//Teleporter pairs should be numbered from 1 up
            for (int i = 0; i < teleporterPairId.size(); i++)
                if (teleporterPairId.get(i) > numPairs)
                    numPairs = teleporterPairId.get(i);
            for(int i = 1; i <= numPairs; i++) {
                int firstIndex = teleporterPairId.indexOf(i);
                int secondIndex = teleporterPairId.lastIndexOf(i);
                if (teleporters.get(firstIndex) != null && teleporters.get(secondIndex) != null && firstIndex != secondIndex) {
                    teleporters.get(firstIndex).setPartner(teleporters.get(secondIndex));
                    teleporters.get(secondIndex).setPartner(teleporters.get(firstIndex));
                    GridManager.getInstance().addToGrid(teleporters.get(firstIndex),teleporters.get(firstIndex).getGridPosition().x,teleporters.get(firstIndex).getGridPosition().y);
                    GridManager.getInstance().addToGrid(teleporters.get(secondIndex),teleporters.get(secondIndex).getGridPosition().x,teleporters.get(secondIndex).getGridPosition().y);
                }
            }

            //Add walls to grid
            for(Point p : m.keySet()){
                for(Direction d : m.get(p)){
                    GridManager.getInstance().addWall(p,d);
                }
            }
            for(int i = 0; i < x; i++){
                GridManager.getInstance().addWall(new Point(i,0),Direction.UP);
                GridManager.getInstance().addWall(new Point(i,y-1),Direction.DOWN);
            }
            for(int i = 0; i < y; i++){
                GridManager.getInstance().addWall(new Point(0,i),Direction.LEFT);
                GridManager.getInstance().addWall(new Point(x-1,i),Direction.RIGHT);
            }
            ball.setPosition(new Point (player.getPosition().x+ball.getPlayerOffset().x,player.getPosition().y+ball.getPlayerOffset().y));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handleToken(String s, Point location){
        switch(s){
            case "G":
                GridGuardSprite guard = new GridGuardSprite("Guard", "floryan,mark.png", stunAnim, player);
                guard.setPivotPoint(new Point(34, 46));
                guard.setGridPosition(location);
                guards.add(guard);
                break;
            case "P":
                player.setGridPosition(location);
                break;
            case "X":
                break;
            case "U":
                if(m.get(location) == null){
                    m.put(location, new ArrayList<Direction>());
                    m.get(location).add(Direction.UP);
                } else {
                    m.get(location).add(Direction.UP);
                }
                break;
            case "D":
                if(m.get(location) == null){
                    m.put(location, new ArrayList<Direction>());
                    m.get(location).add(Direction.DOWN);
                } else {
                    m.get(location).add(Direction.DOWN);
                }
                break;
            case "L":
                if(m.get(location) == null){
                    m.put(location, new ArrayList<Direction>());
                    m.get(location).add(Direction.LEFT);
                } else {
                    m.get(location).add(Direction.LEFT);
                }
                break;
            case "R":
                if(m.get(location) == null){
                    m.put(location, new ArrayList<Direction>());
                    m.get(location).add(Direction.RIGHT);
                } else {
                    m.get(location).add(Direction.RIGHT);
                }
                break;
            case "H":
                hoop.setGridPosition(location);
                hoop.setPivotPoint(new Point(45,45));
                break;
            default:
                if (s.substring(0,1).equals("T")) {
                    TeleporterSprite tpS = new TeleporterSprite("Teleporter", "Teleporter/frame_0_delay-0.04s.gif");
                    tpS.setPivotPoint(new Point(45, 45));
                    tpS.setGridPosition(location);
                    teleporters.add(tpS);
                    try {
                        int i = Integer.parseInt(s.substring(1));
                        teleporterPairId.add(i);
                    } catch (Exception e) {
                        System.out.println("Teleporter int parse failed");
                    }
                }
        }
    }
}

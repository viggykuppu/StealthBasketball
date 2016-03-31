package edu.virginia.engine.util;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import edu.virginia.engine.display.BallSprite;
import edu.virginia.engine.display.GridGuardSprite;
import edu.virginia.engine.display.GridManager;
import edu.virginia.engine.display.PlayerSprite;
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

    private BallSprite ball = new BallSprite("Ball", "coin.gif");
    private PlayerSprite player = new PlayerSprite("Player", "mario.png", ball);
    private ArrayList<GridGuardSprite> guards = new ArrayList<GridGuardSprite>();
    private Map<Point,ArrayList<Direction>> m = new HashMap<Point,ArrayList<Direction>>();
    private File levelFile;
    public LevelGenerator(String fileName){
        levelFile = new File(fileName);
    }

    public void generateLevel(){
        player.setPivotPoint(new Point(player.getUnscaledWidth() / 2, player.getUnscaledHeight() / 2));
        ball.setPivotPoint(new Point(28, 28));

        try {
            Scanner s = new Scanner(levelFile);
            int y = 0;
            while(s.hasNextLine()){
                y++;
                s.nextLine();
            }
            s = new Scanner(levelFile);
            int x;
            int ly = 0;
            while(s.hasNextLine()){
                String line = s.nextLine();
                String[] cells = line.split(";");
                x = cells.length;
                GridManager.getInstance().setGridSize(x, y, x*100, y*100);
                for(int i = 0; i < x; i++){
                    String[] cellContents = cells[i].split(",");
                    for(int j = 0; j < cellContents.length; j++){
                        handleToken(cellContents[j], new Point(i,ly));
                    }
                }
                ly++;
            }
            GridManager.getInstance().addToGrid(player,player.getGridPosition().x,player.getGridPosition().y);
            for(GridGuardSprite g : guards){
                GridManager.getInstance().addToGrid(g,g.getGridPosition().x,g.getGridPosition().y);
            }
            for(Point p : m.keySet()){
                for(Direction d : m.get(p)){
                    GridManager.getInstance().addWall(p,d);
                }
            }
            ball.setPosition(new Point (player.getPosition().x+ball.getPlayerOffset().x,player.getPosition().y+ball.getPlayerOffset().y));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handleToken(String s, Point location){
        switch(s){
            case "G":
                GridGuardSprite guard = new GridGuardSprite("Guard", "floryan,mark.png",player);
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
            default:
                break;
        }

    }
}
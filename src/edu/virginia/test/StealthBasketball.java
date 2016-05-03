package edu.virginia.test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.util.Direction;
import edu.virginia.engine.util.LevelGenerator;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Guillaume Bailey on 3/19/2016.
 */
public class StealthBasketball extends Game {

    BallSprite ball = new BallSprite("Ball", "Basketball.png");
    PlayerSprite player = new PlayerSprite("Player", ball);
    ArrayList<String> stunAnim = new ArrayList<>();
    private ArrayList<String> levels  = new ArrayList<String>();
    private static StealthBasketball game;
    private static Game g;
    private int levelIndex = -1;
    public boolean gameStarted = false;


    //Instantiate all sprites prior to the nullChecker
    Sprite nullChecker = new Sprite("nullChecker", "coin.gif");

    public StealthBasketball() {
        super("Stealth Basketball!", 1807, 1030);
        stunAnim.add("floryan,mark_stunned.png");
        stunAnim.add("floryan,mark_stunned.png");
        GridGuardSprite guard = new GridGuardSprite("Guard", player);
        levels.add("level1.csv");
        levels.add("level2.csv");
        levels.add("level3.csv");
        levels.add("level4.csv");
        levels.add("level5.csv");

        GridManager.getInstance().startTurns();
   }

    public StealthBasketball(boolean weird){
        super("Stealth Basketball!", 1807, 1030);
        this.setImage("Title Screen.png");
    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as strings) that are currently being pressed down
     */
    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys) {
        if(GridManager.getInstance().levelFinished){
            loadNextLevel();
        }
        if (nullChecker != null) {
            if(GridManager.getInstance().levelFailed){
                System.out.println("reloading level");
                this.reloadLevel();
            }
            GridManager.getInstance().update(pressedKeys,heldKeys);
            TweenJuggler.getInstance().nextFrame();
        }
    }

    public void loadNextLevel(){
        if(levels != null && levels.size() > 0){
            GridManager.getInstance().resetLevel();
            if(levelIndex < levels.size()-1){
                levelIndex++;
                LevelGenerator level = new LevelGenerator("levels/"+levels.get(levelIndex));
                level.generateLevel();
                GridManager.getInstance().startTurns();
                GridManager.getInstance().levelFinished = false;
            } else if(levelIndex == levels.size()-1){
                this.exitGame();
            }
        }
    }

    public void reloadLevel(){
        GridManager.getInstance().resetLevel();
        LevelGenerator level = new LevelGenerator("levels/"+levels.get(levelIndex));
        level.generateLevel();
        GridManager.getInstance().startTurns();
        GridManager.getInstance().levelFailed = false;
    }

    /**
     * Engine automatically invokes draw() every frame as well. If we want to make sure mario gets drawn to
     * the screen, we need to make sure to override this method and call mario's draw method.
     */
    @Override
    public void draw(Graphics g) {
        super.draw(g);
        if (nullChecker != null) {
            GridManager.getInstance().draw(g);
            for (DisplayObject o : GridManager.getInstance().getChildren()){
                Graphics2D g2d = (Graphics2D) g;
            }
        }
    }


    public static void main(String[] args) {
        game = new StealthBasketball(true);
        game.start();
    }


    //Sometimes if you hold your click too long you won't throw even though you think you didn't hold it too long
    //Therefore, you know throw on release/click
    //Fixes responsivesness issues
    @Override
    public void mouseReleased(MouseEvent mouseEvent){
        if(gameStarted){
            GridManager.getInstance().player.throwBall(mouseEvent.getX(),mouseEvent.getY());
        } else if(game != null){
            game = new StealthBasketball();
            game.start();
            game.gameStarted = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent){
        if(gameStarted){
            GridManager.getInstance().player.throwBall(mouseEvent.getX(),mouseEvent.getY());
        }
    }

}

package edu.virginia.engine.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


/**
 * Highest level class for creating a game in Java.
 */
public class Game extends DisplayObjectContainer implements ActionListener, KeyListener, MouseListener{

    /* Frames per second this game runs at */
    private int FRAMES_PER_SEC = 30;

    /* The main JFrame that holds this game */
    private JFrame mainFrame;

    /* Timer that this game runs on */
    private Timer gameTimer;

    /* The JPanel for this game */
    private GameScenePanel scenePanel;

    public Game(String gameId, int width, int height) {
        super(gameId);

        setUpMainFrame(gameId, width, height);

        setScenePanel(new GameScenePanel(this));
        mainFrame.addMouseListener(this);
		/* Use an absolute layout */
        scenePanel.setLayout(null);
    }


    public void setFramesPerSecond(int fps) {
        if (fps > 0) this.FRAMES_PER_SEC = fps;
    }

    public void setUpMainFrame(String gameId, int width, int height) {
        this.mainFrame = new JFrame();
        getMainFrame().setTitle(gameId);
        getMainFrame().setResizable(false);
        getMainFrame().setVisible(true);
        getMainFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getMainFrame().setBounds(0, 0, width, height);
        getMainFrame().addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        getMainFrame().addKeyListener(this);
    }

    /**
     * Starts the game
     */
    public void start() {
        if (gameTimer == null) {
            gameTimer = new Timer(1000 / FRAMES_PER_SEC, this);
            gameTimer.start();
        } else {
            gameTimer.start();
        }
    }

    @Override
    public void setImage(String string){
        super.setImage(string);
    }

    /**
     * Stops the animation.
     */
    public void stop() {
        pause();
        gameTimer = null;
    }

    public void pause() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    public void exitGame() {
        stop();
        this.mainFrame.setVisible(false);
        this.mainFrame.dispose();
    }

    /**
     * Close the window
     */
    public void closeGame() {
        this.stop();
        if (this.getMainFrame() != null) {
            this.getMainFrame().setVisible(false);
            this.getMainFrame().dispose();
        }
    }


    /**
     * Called once per frame. updates the game, redraws the screen, etc. May
     * need to optimize this if games get too slow.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        repaintGame();
    }

    /**
     * Forces a repaint
     */
    public void repaint() {
        repaintGame();
    }

    public void repaintGame() {
        if (getScenePanel() != null) {
            getScenePanel().validate();
            getScenePanel().repaint();
        }
    }

    protected void nextFrame(Graphics g) {

        try {
			/* Update all objects on the stage */
            this.update(pressedKeys,heldKeys);
            for (int key : heldKeys){
                if (pressedKeys.contains(key))
                    pressedKeys.remove((Integer) key);
            }

			/* Draw everything on the screen */
            this.draw(g);
        } catch (Exception e) {
            System.out
                    .println("Exception in nextFrame of game. Stopping game (no frames will be drawn anymore");
            stop();
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
		/* Start with no transparency */
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                1.0f));

        super.draw(g);
    }

    public JFrame getMainFrame() {
        return this.mainFrame;
    }

    public void setScenePanel(GameScenePanel scenePanel) {
        this.scenePanel = scenePanel;
        this.getMainFrame().add(this.scenePanel);
        getMainFrame().setFocusable(true);
        getMainFrame().requestFocusInWindow();
    }

    public GameScenePanel getScenePanel() {
        return scenePanel;
    }

    ArrayList<Integer> pressedKeys = new ArrayList<>();
    ArrayList<Integer> heldKeys = new ArrayList<>();

    @Override
    public void keyPressed(KeyEvent e) {
        if (!pressedKeys.contains(e.getKeyCode()) && !heldKeys.contains(e.getKeyCode()))
            pressedKeys.add(e.getKeyCode());
        if (!heldKeys.contains(e.getKeyCode()))
            heldKeys.add(e.getKeyCode());
    }


    @Override
    public void keyReleased(KeyEvent e) {
        if (pressedKeys.contains(e.getKeyCode()))
            pressedKeys.remove((Integer) e.getKeyCode());
        if (heldKeys.contains(e.getKeyCode()))
            heldKeys.remove((Integer) e.getKeyCode());
    }


    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}

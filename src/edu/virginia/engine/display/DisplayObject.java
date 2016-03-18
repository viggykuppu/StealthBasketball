package edu.virginia.engine.display;

import edu.virginia.engine.events.EventDispatcher;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A very basic display object for a java based gaming engine
 * 
 * */
public class DisplayObject extends EventDispatcher{

	/* All DisplayObject have a unique id */
	private String id;
	/* The parent of this DO in the tree*/
	private DisplayObject parent;
	/* The image that is displayed by this object */
	private BufferedImage displayImage;
	/* Boolean var to determine if image is visible or not */
	private boolean visible = true;;
	/* Point for position of image*/
	private Point position = new Point(0,0);
	/* Point for pivot of image */
	private Point pivotPoint = new Point(0,0);
	/* Var that keeps track of scale in X dimension*/
	private double scaleX = 1;
	/* Var that keeps track of scale in Y dimension*/
	private double scaleY = 1;
	/* Var that keeps track of rotation in degrees*/
	private double rotation = 0;
	/* Var that keeps track of transparency, values on the interval [0,1]*/
	private float alpha = 1;
	
	public Area getHitbox(){
		Point globalL = this.globalize(this.getPosition());
		int pX = globalL.x;
		int pY = globalL.y;
		
		Rectangle rect = new Rectangle(-this.getPivotPoint().x,-this.getPivotPoint().y,this.getUnscaledWidth(),this.getUnscaledHeight());
		AffineTransform transform = new AffineTransform();
		transform.translate(pX, pY);
		transform.rotate(Math.PI*this.getRotation()/180);	
		transform.scale(Math.abs(this.getScaleX()), Math.abs(this.getScaleY()));
		Area ra = new Area(rect);
		
		ra = ra.createTransformedArea(transform);

		
		return ra;
	}
	
	public boolean collidesWith(DisplayObject other){
		Area a = this.getHitbox();
		a.intersect(other.getHitbox());
		return !a.isEmpty();
		
	}
	
	public Point globalize(Point p){
		int x = p.x;
		int y = p.y;
		DisplayObject current = this.getParent();
		while(current != null){
			x += current.getPosition().x;
			y += current.getPosition().y;
			current = current.getParent();
		}
		return new Point(x,y);
	}
	
	
	//Given a point in the global coordinate system, 
	//it will return that point in an object's coordinate system
	public Point getRelativeRelativeLocation(Point global){
		int x = global.x;
		int y = global.y;
		DisplayObject current = this;
		while(current != null){
			x -= current.getPosition().x;
			y -= current.getPosition().y;
			current = current.getParent();
		}
		return new Point(x,y);
	}
	
	public DisplayObject getParent(){
		return this.parent;
	}
	
	public void setParent(DisplayObject parent){
		this.parent = parent;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Point getPivotPoint() {
		return pivotPoint;
	}

	public void setPivotPoint(Point pivotPoint) {
		this.pivotPoint = pivotPoint;
	}

	public double getScaleX() {
		return scaleX;
	}

	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}

	public double getScaleY() {
		return scaleY;
	}

	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
		if(this.alpha > 1){
			this.alpha = 1;
		} else if(this.alpha < 0){
			this.alpha = 0;
		}
	}

	/**
	 * Constructors: can pass in the id OR the id and image's file path and
	 * position OR the id and a buffered image and position
	 */
	public DisplayObject(String id) {
		this.setId(id);
	}

	public DisplayObject(String id, String fileName) {
		this.setId(id);
		this.setImage(fileName);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}


	/**
	 * Returns the unscaled width and height of this display object
	 * */
	public int getUnscaledWidth() {
		if(displayImage == null) return 0;
		return displayImage.getWidth();
	}
	
	public int getScaledWidth(){
		return (int)(this.getUnscaledWidth()*this.scaleX);
	}
	
	public int getScaledHeight(){
		return (int)(this.getUnscaledHeight()*this.scaleY);
	}

	public int getUnscaledHeight() {
		if(displayImage == null) return 0;
		return displayImage.getHeight();
	}

	public BufferedImage getDisplayImage() {
		return this.displayImage;
	}

	protected void setImage(String imageName) {
		if (imageName == null) {
			return;
		}
		displayImage = readImage(imageName);
		if (displayImage == null) {
			System.err.println("[DisplayObject.setImage] ERROR: " + imageName + " does not exist!");
		}
	}


	/**
	 * Helper function that simply reads an image from the given image name
	 * (looks in resources\\) and returns the bufferedimage for that filename
	 * */
	public BufferedImage readImage(String imageName) {
		BufferedImage image = null;
		try {
			String file = ("resources" + File.separator + imageName);
			image = ImageIO.read(new File(file));
		} catch (IOException e) {
			System.out.println("[Error in DisplayObject.java:readImage] Could not read image " + imageName);
			e.printStackTrace();
		}
		return image;
	}

	public void setImage(BufferedImage image) {
		if(image == null) return;
		displayImage = image;
	}


	/**
	 * Invoked on every frame before drawing. Used to update this display
	 * objects state before the draw occurs. Should be overridden if necessary
	 * to update objects appropriately.
	 * */
	protected void update(ArrayList<String> pressedKeys) {
		
	}

	/**
	 * Draws this image. This should be overloaded if a display object should
	 * draw to the screen differently. This method is automatically invoked on
	 * every frame.
	 * */
	public void draw(Graphics g) {
		
		if (displayImage != null) {
			
			/*
			 * Get the graphics and apply this objects transformations
			 * (rotation, etc.)
			 */
			Graphics2D g2d = (Graphics2D) g;
			applyTransformations(g2d);
			if(this.isVisible()){
				/* Actually draw the image, perform the pivot point translation here */
				g2d.drawImage(displayImage, -pivotPoint.x, -pivotPoint.y,
						(int) (getUnscaledWidth()),
						(int) (getUnscaledHeight()), null);
			}	
			/*
			 * undo the transformations so this doesn't affect other display
			 * objects
			 */
			reverseTransformations(g2d);
		}
	}

	/**
	 * Applies transformations for this display object to the given graphics
	 * object
	 * */
	protected void applyTransformations(Graphics2D g2d) {
		g2d.translate(this.getPosition().x, this.getPosition().y);
		g2d.rotate(Math.PI*this.getRotation()/180);
		g2d.scale(this.getScaleX(), this.getScaleY());
		int type = AlphaComposite.SRC_OVER;
	    g2d.setComposite(AlphaComposite.getInstance(type, this.getAlpha()));
	}

	/**
	 * Reverses transformations for this display object to the given graphics
	 * object, need to do in the same order that they were applied
	 * */
	protected void reverseTransformations(Graphics2D g2d) {
		int type = AlphaComposite.SRC_OVER;
	    g2d.setComposite(AlphaComposite.getInstance(type, 1.0f));
	    g2d.scale(1/this.getScaleX(), 1/this.getScaleY());
		g2d.rotate(-Math.PI*this.getRotation()/180);
		g2d.translate(-this.getPosition().x, -this.getPosition().y);
	}

}
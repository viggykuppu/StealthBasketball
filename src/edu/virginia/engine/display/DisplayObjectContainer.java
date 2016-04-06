package edu.virginia.engine.display;

import java.awt.*;
import java.util.ArrayList;

public class DisplayObjectContainer extends DisplayObject{
	private ArrayList<DisplayObject> children = new ArrayList<DisplayObject>();
	
	
	public DisplayObjectContainer(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public DisplayObjectContainer(String id, String fileName){
		super(id,fileName);
	}
	
	public void addChild(DisplayObject child){
		child.setParent(this);
		children.add(child);
	}
	
	public void addChildAtIndex(DisplayObject child, int index){
		child.setParent(this);
		children.add(index, child);
	}
	
	public void removeChild(DisplayObject child){
		child.setParent(null);
		children.remove(child);
	}
	
	public DisplayObject removeChildByIndex(int index){
		DisplayObject child = children.remove(index);
		child.setParent(null);
		return child;
	}
	
	public void removeAll(){
		for(DisplayObject child : children){
			child.setParent(null);
			children.remove(child);
		}
	}
	
	public boolean contains(DisplayObject child){
		return children.contains(child);
	}
	
	public DisplayObject getChild(int index){
		return children.get(index);
	}
	
	public DisplayObject getChild(String id){
		for(DisplayObject child : children){
			if(child.getId().equals(id)) return child;
		}
		return null;
	}
	
	public ArrayList<DisplayObject> getChildren(){
		return this.children;
	}
	
	@Override
	protected void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys) {
		super.update(pressedKeys,heldKeys);
		for(DisplayObject child : children){
			if(child !=null){
				child.update(pressedKeys,heldKeys);
			}
		}
	}
	
	@Override
	public void draw(Graphics g){
		if (this.getDisplayImage() != null) {
			
			/*
			 * Get the graphics and apply this objects transformations
			 * (rotation, etc.)
			 */
			Graphics2D g2d = (Graphics2D) g;
			applyTransformations(g2d);
			if(this.isVisible()){
				/* Actually draw the image, perform the pivot point translation here */
				g2d.drawImage(this.getDisplayImage(), -this.getPivotPoint().x, -this.getPivotPoint().y,
						(int) (getUnscaledWidth()),
						(int) (getUnscaledHeight()), null);
				//Draw all of the children now
				for(DisplayObject child : children){
					if(child != null){
						child.draw(g);
					}
				}
			}
			
			
			/*
			 * undo the transformations so this doesn't affect other display
			 * objects
			 */
			reverseTransformations(g2d);
			for(DisplayObject child : children){
				if(child != null){
					g2d.draw(child.getHitbox());
				}
			}
		} else {
			Graphics2D g2d = (Graphics2D) g;
			applyTransformations(g2d);
			for(DisplayObject child : children){
				if(child != null){
					child.draw(g);
				}
			}
			reverseTransformations(g2d);
		}
	}

}

package edu.virginia.engine.display;

import java.awt.*;
import java.util.ArrayList;

public class PhysicsSprite extends AnimatedSprite{
	private int vX;
	private int vY;
	private int aX;
	private int aY;
	private int pX;
	private int pY;
	private long t0;
	private long t;
	private long dT;
	private long g;
	
	
	public PhysicsSprite(String id, String imageFileName) {
		super(id, imageFileName);
		vX = 0;
		vY = 0;
		aX = 0;
		aY = 0;
		g = 0;
		t0 = System.currentTimeMillis()/10;
	}
	
	public int getvX() {
		return vX;
	}

	public void setvX(int vX) {
		this.vX = vX;
	}

	public int getvY() {
		return vY;
	}

	public void setvY(int vY) {
		this.vY = vY;
	}

	public int getaX() {
		return aX;
	}

	public void setaX(int aX) {
		this.aX = aX;
	}

	public int getaY() {
		return aY;
	}

	public void setaY(int aY) {
		this.aY = aY;
	}

	public void giveGravity(){
		this.g = 1;
	}
	
	@Override
	public void update(ArrayList<Integer> pressedKeys){
		t = System.currentTimeMillis()/10;
		dT = t-t0;
//		System.out.println(dT);
		super.update(pressedKeys);
		int x = this.getPosition().x;
		int y = this.getPosition().y;
		pX = x;
		pY = y;
		x = (int) (x + vX*dT);
		y = (int) (y + vY*dT);
		x = (int) (x + aX*dT*dT/2);
		y = (int) (y + (aY+g)*dT*dT/2);
		this.setPosition(new Point(x,y));
		t0 = t;
		if(aX != 0)
		aX = decrease(aX);
		aY = decrease(aY);
		vX = decrease(vX);
		vY = decrease(vY);
		
	}
	
	public int getpX() {
		return pX;
	}

	public void setpX(int pX) {
		this.pX = pX;
	}

	public int getpY() {
		return pY;
	}

	public void setpY(int pY) {
		this.pY = pY;
	}

	public int decrease(int v){
		return v == 0 ? 0 : v - v/Math.abs(v);
	}
	
	public void place(DisplayObject o){
		this.setPosition(new Point(this.getPosition().x,o.getPosition().y-this.getPivotPoint().y));
	}
	

}

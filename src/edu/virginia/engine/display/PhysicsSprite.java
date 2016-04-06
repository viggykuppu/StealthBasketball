package edu.virginia.engine.display;

import java.awt.*;
import java.util.ArrayList;

public class PhysicsSprite extends AnimatedSprite{
	private double vX;
	private double vY;
	private double aX;
	private double aY;
	private double pX;
	private double pY;
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
	
	public double getvX() {
		return vX;
	}

	public void setvX(double vX) {
		this.vX = vX;
	}

	public double getvY() {
		return vY;
	}

	public void setvY(double vY) {
		this.vY = vY;
	}

	public double getaX() {
		return aX;
	}

	public void setaX(double aX) {
		this.aX = aX;
	}

	public double getaY() {
		return aY;
	}

	public void setaY(double aY) {
		this.aY = aY;
	}

	public void giveGravity(){
		this.g = 1;
	}
	
	@Override
	public void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> heldKeys){
		t = System.currentTimeMillis()/10;
		dT = t-t0;
//		System.out.println(dT);
		super.update(pressedKeys,heldKeys);
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
//		vX = decrease(vX);
//		vY = decrease(vY);
	}
	
	public double getpX() {
		return pX;
	}

	public void setpX(double pX) {
		this.pX = pX;
	}

	public double getpY() {
		return pY;
	}

	public void setpY(double pY) {
		this.pY = pY;
	}

	public double decrease(double v){
		if(v == 0)
			return 0;
		double dV = v/70;
		return v-dV;
	}
	
	public void place(DisplayObject o){
		this.setPosition(new Point(this.getPosition().x,o.getPosition().y-this.getPivotPoint().y));
	}
}

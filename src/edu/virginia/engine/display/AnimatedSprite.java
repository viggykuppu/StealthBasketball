package edu.virginia.engine.display;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class AnimatedSprite extends Sprite {
	private ArrayList<BufferedImage> animations;
	private HashMap<String, ArrayList<Integer>> animStats;
	private ArrayList<Double> animationSpeeds;
	private int currentFrame;
	private int startIndex;
	private int endIndex;
	private int animationCounter;
	private boolean halted;

	private long startTime;
	private long startAnimsTime;
	private long allotedTime;
	private long allotedAnimsTime;


	public AnimatedSprite(String id, String defaultImg) {
		super(id, defaultImg);
		animations = new ArrayList<BufferedImage>();
		animStats = new HashMap<>();
		this.animationSpeeds = new ArrayList<>();
		currentFrame = -1;
		animationCounter = 0;
		animations.add(super.readImage(defaultImg));
		startAnimsTime = 0;
	}


	public void haltAnim(boolean bool) {
		this.halted = bool;
	}


	/*
        returns true when done
     */
	public boolean animate(String anim) {
		boolean ret = false;
		if (this.halted == false) {
			if (startTime == 0) {
				startIndex = animStats.get(anim).get(0);
				endIndex = animStats.get(anim).get(1);
				currentFrame = startIndex;
				startTime = System.nanoTime();
				allotedTime = (long)(animationSpeeds.get(animStats.get(anim).get(2)) *
						1000000000 / ((endIndex - startIndex)));
			}
			// return the animation based on the elapsed time
			if (System.nanoTime() - startTime < allotedTime) {

			}
			else if (currentFrame == endIndex) {
				currentFrame = 0;
				startTime = 0;
				ret = true;
			}
			else {
				startTime = System.nanoTime();
				currentFrame += 1;
			}

		}
		super.setImage(animations.get(currentFrame));
		return ret;
	}

	// just acts as a timer and does not actually update sprite
	public boolean timeAnimate(double time) {
		boolean ret = false;
		if (this.halted == false) {
			if (startTime == 0) {
				startTime = System.nanoTime();
				allotedTime = (long)(time *
						1000000000);
			}
			// return the animation based on the elapsed time
			long math = System.nanoTime() - startTime;
			if (System.nanoTime() - startTime < allotedTime) {

			}
			else {
				ret = true;
				startTime = 0;
			}

		}
		return ret;
	}

	// basically false if one anim 1, true if anim 2
	// only works for animations of length 2
	public boolean timeAnimates(double time) {
		boolean ret = false;
		if (this.halted == false) {
			if (startAnimsTime == 0) {
				startAnimsTime = System.nanoTime();
				allotedAnimsTime = (long)(time *
						1000000000);
			}
			// return the animation based on the elapsed time
			long math = System.nanoTime() - startAnimsTime;
			if (System.nanoTime() - startAnimsTime < allotedAnimsTime / 2) {

			}
			else if (System.nanoTime() - startAnimsTime > allotedAnimsTime){
				ret = true;
				startAnimsTime = 0;
			} else {
				ret = true;
			}

		}
		return ret;
	}
	/*
		If you want your animation looped (w/o using default sprite), use this version
	 */
	public boolean looping_Animate(String anim) {
		boolean ret = false;
		if (this.halted == false) {
			if (startTime == 0) {
				startIndex = animStats.get(anim).get(0);
				endIndex = animStats.get(anim).get(1);
				currentFrame = startIndex;
				startTime = System.nanoTime();
				allotedTime = (long)(animationSpeeds.get(animStats.get(anim).get(2)) *
						1000000000 / ((endIndex - startIndex)));
			}
			// return the animation based on the elapsed time
			if (System.nanoTime() - startTime < allotedTime) {

			}
			else if (currentFrame == endIndex) {
				currentFrame = startIndex;
				startTime = 0;
				ret = true;
			}
			else {
				startTime = System.nanoTime();
				currentFrame += 1;
			}

		}
		super.setImage(animations.get(currentFrame));
		return ret;
	}
	/*
        Length is given in seconds
     */
	public void readAnimation(String anim, ArrayList<String> animation, double length) {
		int startFrame = this.animations.size();
		for (int i = 0; i < animation.size(); i++) {
			// add each image into the animation
			this.animations.add(super.readImage(animation.get(i)));
		}
		ArrayList<Integer> stats = new ArrayList<Integer>();
		stats.add(startFrame);
		stats.add(this.animations.size() - 1); // end frame

		animationSpeeds.add(length);
		stats.add(animationCounter);
		this.animStats.put(anim, stats);
	}

	public void setAnimSpeed(String anim, double spd) {
		animationSpeeds.set(animStats.get(anim).get(2), spd);
	}

	public void setDefaultImg() { super.setImage(animations.get(0)); }

	public boolean isHalted() {
		return halted;
	}
}

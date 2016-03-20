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
	private long allotedTime;


	public AnimatedSprite(String id, String defaultImg) {
		super(id, defaultImg);
		animations = new ArrayList<BufferedImage>();
		animStats = new HashMap<>();
		this.animationSpeeds = new ArrayList<>();
		currentFrame = -1;
		animationCounter = 0;
		animations.add(super.readImage(defaultImg));
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


	public boolean isHalted() {
		return halted;
	}
}

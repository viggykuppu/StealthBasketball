package edu.virginia.engine.display;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;

public class AnimatedSprite extends Sprite {
	private ArrayList<BufferedImage> frames = new ArrayList<BufferedImage>();
	private int startIndex = 0;
	private int endIndex = 0;
	private int currentFrame = 0;
	private int defaultIndex;
	private int frameCount = 0;
	private int w0 = -1;
	private int w1 = 0;
	private int j0 = -1;
	private int j1 = 0;
	public boolean jumping = false;

	public AnimatedSprite(String id, String imageFileName) {
		super(id, imageFileName);
		imageFileName = imageFileName.substring(0, imageFileName.length() - 4);
		Path dir = Paths.get("./resources");
		ArrayList<String> files = new ArrayList<String>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for (Path file : stream) {
				String fileName = file.toString().replace("."+File.separator+"resources"+File.separator, "");
				files.add(fileName);
			}
		} catch (IOException | DirectoryIteratorException x) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can only be thrown by newDirectoryStream.
			System.err.println(x);
		}
		Collections.sort(files);
		for (String fileName : files) {
//			System.out.println(fileName);
			if (fileName.contains(imageFileName)) {
				if (fileName.equals(imageFileName+".png")) {
					frames.add(readImage(fileName));
//					System.out.println("default"+fileName);
					defaultIndex = frames.size() - 1;
					currentFrame = defaultIndex;
					// This is our default image
				}
				if (fileName.contains("walk")) {
					frames.add(readImage(fileName));
					if (w0 == -1)
						w0 = frames.size() - 1;
					w1 = frames.size() - 1;
				}
				if (fileName.contains("jump")) {
					frames.add(readImage(fileName));
					if (j0 == -1)
						j0 = frames.size() - 1;
					j1 = frames.size() - 1;
				}
			}
		}
//		System.out.println(w0+" "+w1);
	}

	public void animate(String animation) {
		switch (animation) {
		case "jump":
			startIndex = j0;
			endIndex = j1+1;
			currentFrame = startIndex;
			break;
		case "walkL":
			this.setScaleX(-1*Math.abs(this.getScaleX()));
			startIndex = w0;
			endIndex = w1+1;
			currentFrame = startIndex;
			break;	
		case "walkR":
			this.setScaleX(Math.abs(this.getScaleX()));
			startIndex = w0;
			endIndex = w1+1;
			currentFrame = startIndex;
			break;
		}
	}

	@Override
	public void update(ArrayList<String> pressedKeys) {
		super.update(pressedKeys);
			if(frames.size()>0){
				this.setImage(frames.get(currentFrame));
				if (currentFrame < endIndex && frameCount == 10) {
					currentFrame++;
					frameCount = 0;
				}
				if (currentFrame < endIndex && frameCount < 10) {
					frameCount++;
				}
				if (currentFrame == endIndex) {
					frameCount = 0;
					startIndex = defaultIndex;
					endIndex = defaultIndex;
					currentFrame = defaultIndex;
				}
			}
		}
}

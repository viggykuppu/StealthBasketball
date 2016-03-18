package edu.virginia.engine.util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
	Map<String,String> sounds = new HashMap<String,String>();
	
	
	public void LoadSoundEffect(String id, String fileName){
		sounds.put(id, fileName);
	}
	
	public void PlaySoundEffect(String id){
		AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(SoundManager.class.getResource(sounds.get(id)));
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void LoadMusic(String id, String fileName){
		sounds.put(id, fileName);
	}
	
	public void playMusic(String id){
		AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(SoundManager.class.getResource(sounds.get(id)));
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.loop(clip.LOOP_CONTINUOUSLY);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

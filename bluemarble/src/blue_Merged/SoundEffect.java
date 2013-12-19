package blue_Merged;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum SoundEffect {
	CONSTRUCT("construct.wav"),	
	DICE("dice1.wav");
	//SHOOT("shoot.wav");
	
	public static enum Volume {
		MUTE, LOW, MEDIUM, HIGH
	}
	
	public static Volume volume = Volume.MEDIUM;
	
	private Clip clip;
	
	SoundEffect(String soundFileName) {
		System.out.println(soundFileName);
		try {			
			File f = new File("./sound/"+soundFileName);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f);
			clip = AudioSystem.getClip(); 
			clip.open(audioInputStream);
		}catch(UnsupportedAudioFileException e) { 
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (LineUnavailableException e) {		
			e.printStackTrace();
		}
	}
	
	public void play() {
		if (volume != Volume.MUTE) {
			if(clip.isRunning()) clip.stop();
			clip.setFramePosition(0);
			clip.start();
		}
	}
	
	public void init() {
		values();
	}
	
}

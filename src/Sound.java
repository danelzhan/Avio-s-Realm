
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	Clip clip;
	URL soundURL [] = new URL[30];
	
	public Sound () {
		
		soundURL[0] = getClass().getResource("sounds/music1.wav");
		
		soundURL[29] = getClass().getResource("sounds/collectedSE.wav");
		soundURL[28] = getClass().getResource("sounds/speak1.wav");
		soundURL[27] = getClass().getResource("sounds/speak2.wav");
		soundURL[25] = getClass().getResource("sounds/dashSE1.wav");
		soundURL[24] = getClass().getResource("sounds/attackHitSE.wav");
		soundURL[23] = getClass().getResource("sounds/attackSwingSE.wav");
		
	} // Constructor
	
	public void setFile (int i) {
		
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
		} // try catch
		
	} // setFile
	
	public void play () {
		clip.start();
	} // play
	
	public void loop () {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	} // loop
	
	public void stop () {
		clip.stop();
	} // stop
	
} // Sound

package handlers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundHandler extends Handler {

	private HashMap<String, SoundPlayer> sounds;

	@Override
	public void setup() {
		sounds = new HashMap<String, SoundPlayer>();
	}

	@Override
	public void update() {
		String[] soundNames = sounds.keySet().toArray(new String[sounds.size()]);
		for(int i = 0, j = soundNames.length; i < j; i++) {
			 
		}
	}

	class SoundPlayer implements Runnable, LineListener {

		private File audioFile;
		private boolean completed;
		private int errno;

		public SoundPlayer(String songPath) {
			audioFile = new File(songPath);
			errno = 0;
		}
		
		/**
		 * Play a song
		 */
		@Override
		public void run() {
			try {
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
				AudioFormat format = audioStream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				Clip audioClip = (Clip) AudioSystem.getLine(info);
				audioClip.addLineListener(this);
				audioClip.open(audioStream);
				audioClip.start();

				while(!completed) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException  e) {
						errno = -4;
					}
				}
			} catch (LineUnavailableException e) {
				errno = -1;
			} catch (UnsupportedAudioFileException e) {
				errno = -2;
			} catch (IOException e) {
				errno = -3;
			}
		}
		
		// GETTERS
		public int getErrno() { return errno; }
		public boolean isCompleted() { return completed; }

		@Override
		public void update(LineEvent e) {
			if (e.getType() == LineEvent.Type.STOP) {
				completed = true;
			}
		}
	}

	@Override
	public void onAdd() {}

	@Override
	public void onRemove() {}
}

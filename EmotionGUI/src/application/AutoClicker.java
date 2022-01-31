package application;

import java.awt.AWTException;
import java.awt.Robot;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class AutoClicker implements Runnable {
	
	private Robot robot;
	private int button;
	private volatile boolean alive = true;
	private MediaPlayer player;
	private Status status;
	
	public AutoClicker(int button, MediaPlayer player) {
		this.button = button;
		this.player = player;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void stopClicking() {
		this.alive = false;
	}
	
	public void startClicking() {
		this.alive = true;
	}
	
	@Override
	public void run() {
		status = player.getStatus();
		
	while(this.alive) {
		robot.mousePress(button);
		robot.mouseRelease(button);
		robot.delay(500);
	}
	
	System.out.println("Finsihed annotation");
	
	}

}

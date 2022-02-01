package application;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class AutoClicker implements Runnable {
	
	private Thread clicker;
	private Robot robot;
	private int button;
	private AtomicBoolean running = new AtomicBoolean(false);
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
		running.set(false);
	}
	
	public void startClicking() {
		clicker = new Thread(this);
		clicker.start();
	}
	
	@Override
	public void run() {
		
		running.set(true);
		
	while(running.get()) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("Thread interrupted");
		}
		robot.mousePress(button);
		robot.mouseRelease(button);
		robot.delay(500);
	}
	
	System.out.println("Finsihed annotation");
	
	}

}

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
	private int delayTime = 500;
	private AtomicBoolean running = new AtomicBoolean(false);
	private boolean threadPause = false;

	public AutoClicker(int button, MediaPlayer player) {
		this.button = button;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}

	public int getDelayTime() {
		return delayTime;
	}

	public void stopClicking() {
		running.set(false);
	}

	public void startClicking() {
		clicker = new Thread(this);
		clicker.setName("Clicker");
		clicker.start();
	}

	public void pauseClicking() {
		threadPause = true;
	}
	
	public synchronized void resumeClicking(boolean pPlay) {
		if(!pPlay) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		threadPause = false;
		notify();
	}

	@Override
	public void run() {

		running.set(true);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println("Thread interrupted");
		}

		while (running.get()) {
			try {
				synchronized (this) {
					while (threadPause) {
						wait();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		robot.mousePress(button);
		robot.mouseRelease(button);
		robot.delay(delayTime);
	}
	}

}

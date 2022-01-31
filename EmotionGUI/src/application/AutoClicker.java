package application;

import java.awt.AWTException;
import java.awt.Robot;

public class AutoClicker implements Runnable {
	
	private Robot robot;
	private int button;
	private volatile boolean alive = true;
	
	public AutoClicker(int button) {
		this.button = button;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void stopClicking() {
		this.alive = false;
	}
	
	@Override
	public void run() {
		
	while(alive) {
		robot.mousePress(button);
		robot.mouseRelease(button);
		robot.delay(500);
	}
	}

}

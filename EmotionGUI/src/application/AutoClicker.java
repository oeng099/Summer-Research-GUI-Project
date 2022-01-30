package application;

import java.awt.AWTException;
import java.awt.Robot;

public class AutoClicker implements Runnable {
	
	private Robot robot;
	private int button;
	
	public AutoClicker(int button) {
		this.button = button;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void run() {
		
	while(true) {
		robot.mousePress(button);
		robot.mouseRelease(button);
		robot.delay(500);
	}
		
	}

}

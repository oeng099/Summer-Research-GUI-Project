package application;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.concurrent.atomic.AtomicBoolean;


public class AutoClicker implements Runnable {

	private Thread clicker;
	private Robot robot;
	private int button;
	private int delayTime = 500;
	private AtomicBoolean running = new AtomicBoolean(false);
	private boolean threadPause = false;

	//Constructor for the AutoClicker class
	public AutoClicker(int button) {
		this.button = button;
		try {
			//Initialises a robot variable
			robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}

	//Method to return the delay time
	public int getDelayTime() {
		return delayTime;
	}

	//Method to stops the autoclicker
	public void stopClicking() {
		running.set(false);
	}

	//Method to start the autoclicker
	public void startClicking() {
		//Starts a new thread with the clicker
		clicker = new Thread(this);
		clicker.setName("Clicker");
		clicker.start();
	}

	//Method to pause the autoclicker
	public void pauseClicking() {
		threadPause = true;
	}
	
	//Method to resume the autoclicker
	public synchronized void resumeClicking(boolean pPlay) {
		//Checks if resume was caused by p key, if so autoclicker immediately resumes, else there is a small delay
		//to allow the user to move their mouse back to the model
		if(!pPlay) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Resets the threadPause variable
		threadPause = false;
		notify();
	}

	//Method to annotate while autoclicker is running
	@Override
	public void run() {

		running.set(true);
		//Initial wait for five seconds to allow the user to move the mouse onto the model prior to
		//annotation
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println("Thread interrupted");
		}

		//Runs while the autoclicker is running
		while (running.get()) {
			try {
				synchronized (this) {
					//Will pause the while loop if threadPause is changed to true
					while (threadPause) {
						wait();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		//Acts an automatic press and release of the mouse to simulate a click, then there is a delay before the next
		//click, acts as method to annotate
		robot.mousePress(button);
		robot.mouseRelease(button);
		robot.delay(delayTime);
	}
	}

}

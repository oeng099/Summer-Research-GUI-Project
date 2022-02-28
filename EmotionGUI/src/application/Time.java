package application;

public class Time {

	private int seconds;
	private int minutes;
	private int hours;
	
	//Constructor for Time class with only one input for total seconds
	public Time(int totalSeconds) {
		//Calculates the hours and removes the corresponding seconds from totalSeconds
		this.hours = totalSeconds / 3600;
		if (this.hours > 0) {
			totalSeconds -= this.hours * 3600;
		}
		
		//Calculates the minutes and removes the corresponding seconds from totalSeconds
		this.minutes = totalSeconds / 60;
		if (this.minutes > 0) {
			totalSeconds -= this.minutes * 60;
		}
		
		this.seconds = totalSeconds;
	}
	
	//Method to get the seconds variable
	public int getSeconds() {
		return this.seconds;
	}
	
	//Method to get the minutes variable
	public int getMinutes() {
		return this.minutes;
	}
	
	//Method to get the hours variable
	public int getHours() {
		return this.hours;
	}
}

package model;

public class BeepSample {

	
	final private int duration;
	final private int frequency;
	
	public BeepSample(int duration, int frequency) {
		this.duration = duration;
		this.frequency = frequency;
	}

	public int getDuration() {
		return duration;
	}
	public int getFrequency() {
		return frequency;
	}
	
}

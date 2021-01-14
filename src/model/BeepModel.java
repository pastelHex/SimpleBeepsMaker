package model;
import java.util.List;

public class BeepModel {

	final private int sampleRate;
	final private List<BeepSample> beepList;
	
	public BeepModel(int sampleRate, List<BeepSample> beepList) {
		this.sampleRate = sampleRate;
		this.beepList = beepList;
	}
	
	public int getSampleRate() {
		return sampleRate;
	}
	public List<BeepSample> getBeepList() {
		return beepList;
	}
}

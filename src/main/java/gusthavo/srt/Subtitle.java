package main.java.gusthavo.srt;

public class Subtitle {

	public int id;
	public String startTime;
	public String endTime;
	public String text;
	public long timeIn;
	public long timeOut;
	public Subtitle nextSubtitle;

	@Override
	public String toString() {
		return "Subtitle [id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", text=" + text
				+ ", timeIn=" + timeIn + ", timeOut=" + timeOut + "]";
	}

}
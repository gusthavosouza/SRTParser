package com.gustavo.srt;

public class Subtitle {

	public int id;
	public String startTime;
	public String endTime;
	public String text;

	@Override
	public String toString() {
		return "Subtitle [id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", text=" + text + "]";
	}
}
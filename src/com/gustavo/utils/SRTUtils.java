package com.gustavo.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gustavo.srt.Subtitle;

public class SRTUtils {	
	
	public final static long MILLIS_IN_SECOND = 1000;
	public final static long MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60; // 60000
	public final static long MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60; // 3600000
	
	protected final static Pattern PATTERN_TIME = Pattern.compile("([\\d]{2}):([\\d]{2}):([\\d]{2}),([\\d]{3})");
	
	public static long textTimeToMillis (final String time) throws Exception {
		
		Matcher matcher = PATTERN_TIME.matcher(time);
		if (time == null || time.isEmpty() || !matcher.find())
			throw new Exception("incorret time format...");

		long msTime = 0;
		short hours = Short.parseShort(matcher.group(1));
		byte min = Byte.parseByte(matcher.group(2));
		byte sec = Byte.parseByte(matcher.group(3));
		short millis = Short.parseShort(matcher.group(4));
		
		if (hours > 0)
			msTime += hours * MILLIS_IN_HOUR;
		if (min > 0)
			msTime += min * MILLIS_IN_MINUTE;
		if (sec > 0)
			msTime += sec * MILLIS_IN_SECOND;
		
		msTime += millis;
		
		return msTime;
	}
	
	public static Subtitle findSubtitle (ArrayList<Subtitle> listSubtitles, long timeMillis) {
		
		if (listSubtitles == null || listSubtitles.isEmpty())
			return null;
		
		// probability is first index 
		if (timeMillis == 0 || timeMillis < 1000)
			return listSubtitles.get(0);
			
		for (int i = 0; i < listSubtitles.size(); i++) {
			Subtitle sub = listSubtitles.get(i);
			if (timeMillis >= sub.timeIn && timeMillis <= sub.timeOut)
				return sub;

			if (sub.nextSubtitle != null && sub.nextSubtitle.timeIn >= timeMillis)
				return sub.nextSubtitle;
			else if (listSubtitles.size() < i+1) // is last?
				continue;

			// get next element for test if is a legend
			sub = listSubtitles.get(i+1);
			if (sub.timeIn >= timeMillis)
				return sub;
		}
		return null;
	}
	
	public static Subtitle findSubtitle (final Subtitle subtitle, long time) {
		
		if (subtitle == null)
			return subtitle;
		
		Subtitle subAux = null;
		
		while ((subAux = subtitle.nextSubtitle) != null)
			if (time >= subAux.timeIn && time <= subAux.timeOut)
				return subAux;
		
//		while ((subAux = subAux.previousSubtitle) != null)
//			if (time >= subAux.timeIn && time <= subAux.timeOut)
//				return  subAux;
//		
		return subAux;
	}
}
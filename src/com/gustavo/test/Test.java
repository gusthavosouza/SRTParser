package com.gustavo.test;

import java.util.ArrayList;

import com.gustavo.srt.SRTReader;
import com.gustavo.srt.Subtitle;
import com.gustavo.utils.SRTUtils;

public class Test {

	public static void main(String[] args) {
		
		try {
			System.out.println(SRTUtils.textTimeToMillis("01:00:00,360"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String path = System.getProperty("user.dir") + "\\files\\sub.srt";
		
		System.out.println(path);
		
		ArrayList<Subtitle> subtitles = SRTReader
				.getSubtitlesFromFile(path, false);
		
//		3600360
//		3578992
		System.out.println(SRTUtils.findSubtitle(subtitles, 000100)); 
//		
//		for (Subtitle subtitle : subtitles) {
//			System.out.println(subtitle);
//		}
	}
}
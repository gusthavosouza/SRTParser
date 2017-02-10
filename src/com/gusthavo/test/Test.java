package com.gusthavo.test;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.gusthavo.srt.SRTParser;
import com.gusthavo.srt.Subtitle;
import com.gusthavo.utils.SRTUtils;

public class Test {

	static Logger logger = Logger.getLogger(Test.class);

	public static void main(String[] args) {
		
		try {
			logger.info(SRTUtils.textTimeToMillis("01:00:00,360"));
		} catch (Exception e) {
			logger.error("error parsing time", e);
		}
		
		String path = System.getProperty("user.dir") + "\\files\\sub.srt";
		
		logger.info(path);
		
		ArrayList<Subtitle> subtitles = SRTParser
				.getSubtitlesFromFile(path, false);

		logger.info(SRTUtils.findSubtitle(subtitles, 000100));
//		
//		for (Subtitle subtitle : subtitles) {
//			System.out.println(subtitle);
//		}
	}
}
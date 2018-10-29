package com.gusthavo.test;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.gusthavo.srt.SRTParser;
import com.gusthavo.srt.Subtitle;
import com.gusthavo.utils.SRTUtils;

public class Test {

	static Logger logger = Logger.getLogger(Test.class);

	public static void main(String[] args) {
		
//		System.out.println(String.format("%02d:%02d:%02d,%03d", 1, 2, 30, 600));
		
//		long millis = 2600l;
//		System.out.println(SRTUtils.millisToText(millis));
		
		try {
			logger.info(SRTUtils.textTimeToMillis("01:00:00,360"));
		} catch (Exception e) {
			logger.error("error parsing time", e);
		}
		
		String path = System.getProperty("user.dir") + "\\files\\sub.srt";
		

		logger.info(path);
		
		ArrayList<Subtitle> subtitles = SRTParser
				.getSubtitlesFromFile(path, false);
		
		
		String pathOut = System.getProperty("user.dir") + "\\files\\new_sub.srt";

		long syncMillis = -1000;
		boolean b = SRTUtils.speedSynchronization(subtitles, syncMillis, new File(pathOut));
		logger.info("sync new file:" + pathOut + " millis:" + syncMillis);
		logger.info("sync file status:" + b);

		logger.info(SRTUtils.findSubtitle(subtitles, 000100));
		
		for (Subtitle subtitle : subtitles) {
			System.out.println(subtitle);
		}
	}
}
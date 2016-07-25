package com.gustavo.test;
import java.util.ArrayList;

import com.gustavo.srt.SRTReader;
import com.gustavo.srt.Subtitle;

public class Test {

	public static void main(String[] args) {
		
		String path = System.getProperty("user.dir") + "\\files\\jiro.srt";
		
		System.out.println(path);
		
		ArrayList<Subtitle> subtitles = SRTReader
				.getSubtitlesFromFile(path, true);

		for (Subtitle subtitle : subtitles) {
			System.out.println(subtitle);
		}
	}
}
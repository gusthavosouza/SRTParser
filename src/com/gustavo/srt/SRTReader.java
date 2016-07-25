package com.gustavo.srt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SRTReader {

	private static final Pattern PATTERN_NUMBERS = Pattern.compile("(\\d+?)");
	private static final Pattern PATTERN_TIME = Pattern.compile("([\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3}).*([\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3})");
//	private static final Pattern PATTERN_TEXT = Pattern.compile("(.*)");
	
//	private boolean twm; // Text with multiline '\n' 
	
	public static ArrayList<Subtitle> getSubtitlesFromFile (String path) {
		return getSubtitlesFromFile(path, false);
	}
	
	public static ArrayList<Subtitle> getSubtitlesFromFile(String path, boolean twm) {
		
		BufferedReader br = null;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		File file = null;

		ArrayList<Subtitle> subtitles = null;
		Subtitle sub = null;
		StringBuilder srt = null;
		try {
			
			file = new File(path);
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			br = new BufferedReader(isr);

			subtitles = new ArrayList<>();
			sub = new Subtitle();
			srt = new StringBuilder();
			
			while (br.ready()) {
				
				String line = br.readLine();
				
//				if (validateLine(line))
//					continue;
				
				Matcher matcher = PATTERN_NUMBERS.matcher(line); 

				if (matcher.find()) {
					sub.id = Integer.parseInt(matcher.group(1)); // index
					line = br.readLine();
				}
				
				matcher = PATTERN_TIME.matcher(line);
				
				if (matcher.find()) {
					sub.startTime = matcher.group(1);
					sub.endTime = matcher.group(2);
				}
				
				String aux;
				while ((aux = br.readLine()) != null && !aux.isEmpty()) {
					srt.append(aux);
					if (twm)
						srt.append("\n");
					else {
						if (!line.endsWith(" ")) // for new lines '\n' removed from BufferedReader
							srt.append(" ");
					}
				}

				srt.delete(srt.length()-1, srt.length()); // remove '\n' or space from end string

				line = srt.toString();
				srt.setLength(0);

				if (line != null && !line.isEmpty())
					line = line.replaceAll("<[^>]*>", ""); // clear all tags
				
				sub.text = line;
				
				subtitles.add(sub);
				sub = new Subtitle();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { fis.close(); } catch (IOException e) {}
			try { isr.close(); } catch (IOException e) {}
			try { br.close(); } catch (IOException e) {}
		}
		return subtitles;
	}
	
	public static boolean validateLine (String line) {
		return line == null || line.isEmpty() || line.equals("\n") || line.equals("\r");
	}
}
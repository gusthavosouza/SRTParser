package main.java.gusthavo.srt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import main.java.gusthavo.utils.SRTUtils;

public final class SRTParser {

	private static final Pattern PATTERN_TIME = Pattern.compile("([\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3}).*([\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3})");
	private static final Pattern PATTERN_NUMBERS = Pattern.compile("(\\d+)");
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	
	private final static Logger logger = Logger.getLogger(SRTParser.class);
	
	/**
	 * Metodo responsavel por fazer parse de um arquivos de legenda. <br>
	 * Obs. O texto n�o vai conter quebra de linhas e n�o � usado Node {@link SRTParser#getSubtitlesFromFile(String, boolean, boolean)}}
	 * @param path
	 * @return
	 */
	public static ArrayList<Subtitle> getSubtitlesFromFile (String path) {
		return getSubtitlesFromFile(path, false, false);
	}
	
	/**
	 * Metodo responsavel por fazer parse de um arquivos de legenda. <br>
	 * Obs. O texto pode ou nao conter quebra de linhas e n�o � usado Node {@link SRTParser#getSubtitlesFromFile(String, boolean, boolean)}}
	 * @param path
	 * @return
	 */
	public static ArrayList<Subtitle> getSubtitlesFromFile (String path, boolean twm) {
		return getSubtitlesFromFile(path, twm, false);
	}
	
	/**
	 * Metodo responsavel por fazer parse de um arquivos de legenda. <br>
	 * Obs. O texto n�o vai conter quebra de linhas e pode ser usado Node
	 * @param path
	 * @return
	 */
	public static ArrayList<Subtitle> getSubtitlesFromFile (String path, boolean twm, boolean usingNodes) {
		
		ArrayList<Subtitle> subtitles = null;
		Subtitle sub = null;
		StringBuilder srt = null;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), DEFAULT_CHARSET))) {
			
			subtitles = new ArrayList<>();
			sub = new Subtitle();
			srt = new StringBuilder();
			
			while (br.ready()) {
				
				String line = br.readLine();
				
				Matcher matcher = PATTERN_NUMBERS.matcher(line); 

				if (matcher.find()) {
					sub.id = Integer.parseInt(matcher.group(1)); // index
					line = br.readLine();
				}
				
				matcher = PATTERN_TIME.matcher(line);
				
				if (matcher.find()) {
					sub.startTime = matcher.group(1); // start time
					sub.timeIn = SRTUtils.textTimeToMillis(sub.startTime);
					sub.endTime = matcher.group(2); // end time
					sub.timeOut = SRTUtils.textTimeToMillis(sub.endTime);
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

				if (usingNodes) {
					sub.nextSubtitle = new Subtitle();
					sub = sub.nextSubtitle;
				} else {
					sub = new Subtitle();
				}
			}
		} catch (Exception e) {
			logger.error("error parsing srt file", e);
		} 
		return subtitles;
	}
}
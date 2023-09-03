package com.github.gusthavo.srt;

import com.github.gusthavo.utils.SRTUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SRTParser {

	private final static Logger logger = LogManager.getLogger(SRTParser.class);

	private static final Pattern PATTERN_TIME = Pattern.compile("([\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3}).*([\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3})");
	private static final Pattern PATTERN_NUMBERS = Pattern.compile("(\\d+)");
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private static final String REGEX_REMOVE_TAGS = "<[^>]*>";

	private static final int PATTERN_TIME_REGEX_GROUP_START_TIME = 1;
	private static final int PATTERN_TIME_REGEX_GROUP_END_TIME = 2;

	/**
	 *
	 * This method is responsible for parsing a STR file.
	 *
	 * This method will not have any new line and also will not make the use of nodes see: Node {@link SRTParser#getSubtitlesFromFile(String, boolean, boolean)}}
	 *
	 * Metodo responsavel por fazer parse de um arquivos de legenda. <br>
	 * Obs. O texto nao vai conter quebra de linhas e nao é utilizado Node {@link SRTParser#getSubtitlesFromFile(String, boolean, boolean)}}
	 * @param path
	 * @return
	 */
	public static ArrayList<Subtitle> getSubtitlesFromFile (String path) {
		return getSubtitlesFromFile(path, false, false);
	}

	/**
	 *
	 * This method is responsible for parsing a STR file.
	 *
	 * This method will not have any new line and also will not make the use of nodes see: Node {@link SRTParser#getSubtitlesFromFile(String, boolean, boolean)}}
	 *
	 * Metodo responsavel por fazer parse de um arquivos de legenda. <br>
	 * Obs. O texto nao vai conter quebra de linhas e nao é utilizado Node {@link SRTParser#getSubtitlesFromFile(String, boolean, boolean)}}
	 * @param path
	 * @return
	 */
	public static ArrayList<Subtitle> getSubtitlesFromFile (String path, boolean keepNewlinesEscape) {
		return getSubtitlesFromFile(path, keepNewlinesEscape, false);
	}

	/**
	 *
	 * This method is responsible for parsing a STR file.
	 *
	 * This method will not have any new line and also will not make the use of nodes see: Node {@link SRTParser#getSubtitlesFromFile(String, boolean, boolean)}}
	 * Note that you can configure if you want to make the use of Nodes: by setting the parameter usingNodes to true
	 *
	 * Metodo responsavel por fazer parse de um arquivos de legenda. <br>
	 *
	 * @param path
	 * @param keepNewlinesEscape
	 * @param usingNodes
	 * @return
	 */
	public static ArrayList<Subtitle> getSubtitlesFromFile (String path, boolean keepNewlinesEscape, boolean usingNodes) {

		ArrayList<Subtitle> subtitles = null;
		Subtitle subtitle;
		StringBuilder srt;

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), DEFAULT_CHARSET))) {

			subtitles = new ArrayList<>();
			subtitle = new Subtitle();
			srt = new StringBuilder();

			while (bufferedReader.ready()) {

				String line = bufferedReader.readLine();

				Matcher matcher = PATTERN_NUMBERS.matcher(line);

				if (matcher.find()) {
					subtitle.id = Integer.parseInt(matcher.group(1)); // index
					line = bufferedReader.readLine();
				}

				matcher = PATTERN_TIME.matcher(line);

				if (matcher.find()) {
					subtitle.startTime = matcher.group(PATTERN_TIME_REGEX_GROUP_START_TIME); // start time
					subtitle.timeIn = SRTUtils.textTimeToMillis(subtitle.startTime);
					subtitle.endTime = matcher.group(PATTERN_TIME_REGEX_GROUP_END_TIME); // end time
					subtitle.timeOut = SRTUtils.textTimeToMillis(subtitle.endTime);
				}

				String aux;
				while ((aux = bufferedReader.readLine()) != null && !aux.isEmpty()) {
					srt.append(aux);
					if (keepNewlinesEscape)
						srt.append("\n");
					else {
						if (!line.endsWith(" ")) // for any new lines '\n' removed from BufferedReader
							srt.append(" ");
					}
				}

				srt.delete(srt.length()-1, srt.length()); // remove '\n' or space from end string

				line = srt.toString();
				srt.setLength(0); // Clear buffer

				if (line != null && !line.isEmpty())
					line = line.replaceAll(REGEX_REMOVE_TAGS, ""); // clear all tags

				subtitle.text = line;
				subtitles.add(subtitle);

				if (usingNodes) {
					subtitle.nextSubtitle = new Subtitle();
					subtitle = subtitle.nextSubtitle;
				} else {
					subtitle = new Subtitle();
				}
			}
		} catch (Exception e) {
			logger.error("error parsing srt file", e);
		}
		return subtitles;
	}
}

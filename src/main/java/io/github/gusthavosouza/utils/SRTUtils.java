package io.github.gusthavo.utils;

import io.github.gusthavo.srt.Subtitle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SRTUtils {

	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private final static long MILLIS_IN_SECOND = 1000;
	private final static long MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60; // 60000
	private final static long MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60; // 3600000

	private final static Pattern PATTERN_TIME = Pattern.compile("([\\d]{2}):([\\d]{2}):([\\d]{2}),([\\d]{3})");

	private final static int PATTER_TIME_GROUP_HOURS = 1;
	private final static int PATTER_TIME_GROUP_MINUTES = 2;
	private final static int PATTER_TIME_GROUP_SECONDS = 3;
	private final static int PATTER_TIME_GROUP_MILLISECONDS = 4;

	private final static String SCAPE_TIME_TO_TIME = " --> ";

	private final static Logger logger = LogManager.getLogger(SRTUtils.class);

	/**
	 * This method is going to convert a String (time) input to milliseconds
	 * Metodo responsavel por converter uma String com o formato de tempo HH:mm:ss,SSS em millis
	 * @param time
	 * @return texto convertido em millis
	 * @throws Exception
	 */

	public static long textTimeToMillis (final String time) throws Exception {

		if (time == null) throw new NullPointerException("Time should not be null");

		Matcher matcher = PATTERN_TIME.matcher(time);
		if (time.isEmpty() || !matcher.find()) throw new Exception("incorrect time format...");

		long msTime = 0;
		short hours = Short.parseShort(matcher.group(PATTER_TIME_GROUP_HOURS));
		byte min = Byte.parseByte(matcher.group(PATTER_TIME_GROUP_MINUTES));
		byte sec = Byte.parseByte(matcher.group(PATTER_TIME_GROUP_SECONDS));
		short millis = Short.parseShort(matcher.group(PATTER_TIME_GROUP_MILLISECONDS));

		if (hours > 0) msTime += hours * MILLIS_IN_HOUR;
		if (min > 0) msTime += min * MILLIS_IN_MINUTE;
		if (sec > 0) msTime += sec * MILLIS_IN_SECOND;

		return msTime + millis;
	}

	/**
	 *
	 * Metodo responsavel por converter millis em texto formato HH:mm:ss,SSS
	 * @param millisToText
	 * @return
	 */

	public static String millisToText(final long millisToText) {

		int millisToSeconds = (int) millisToText / 1000;
		long hours = millisToSeconds / 3600;
		long minutes = (millisToSeconds % 3600) / 60;
		long seconds = millisToSeconds % 60;
		long millis = millisToText % 1000;

		if (hours < 0) hours = 0;
		if (minutes < 0) minutes = 0;
		if (seconds < 0) seconds = 0;
		if (millis < 0) millis = 0;

		return String.format("%02d:%02d:%02d,%03d", hours, minutes, seconds, millis);
	}

	/**
	 * Método responsavel por converter millisIn e millisOut em texto formato HH:mm:ss,SSS --> HH:mm:ss,SSS
	 * @param millisIn
	 * @param millisOut
	 * @return
	 */

	public static String millisToText(final long millisIn, final long millisOut) {
		return millisToText(millisIn) + SCAPE_TIME_TO_TIME + millisToText(millisOut);
	}

	/**
	 * Metodo responsavel por buscar um Subtitle em uma lista a partir do tempo passado <b>timeMillis</b>
	 * @param listSubtitles
	 * @param timeMillis
	 * @return um Subtitle ou null no caso de nada encontrado
	 */
	public static Subtitle findSubtitle (ArrayList<Subtitle> listSubtitles, long timeMillis) {
		if (listSubtitles == null || listSubtitles.isEmpty()) return null;

		// most likely is first index
		if (timeMillis < 1000) return listSubtitles.get(0);

		for (int i = 0; i < listSubtitles.size(); i++) {
			Subtitle sub = listSubtitles.get(i);
			if (inTime(sub, timeMillis)) return sub;

			if (sub.nextSubtitle != null && sub.nextSubtitle.timeIn >= timeMillis)
				return sub.nextSubtitle;
			else if (listSubtitles.size() <= i+1) // check if is the last element
				continue;

			// get next element to test
			sub = listSubtitles.get(i+1);
			if (sub.timeIn >= timeMillis)
				return sub;
		}
		return null;
	}

	/**
	 * Metodo responsavel por buscar um Subtitle a partir de um {@link Subtitle}, utilizando node<br>
	 * Obs. Deve ser configurado no load do arquivo para utilizar Node #{@link SRTParser#getSubtitlesFromFile(String, boolean, boolean)}
	 * @param subtitle
	 * @param timeMillis
	 * @return
	 */
	public static Subtitle findSubtitle (final Subtitle subtitle, long timeMillis) {
		if (subtitle == null) return null;

		Subtitle subAux = subtitle;
		while (( subAux = subAux.nextSubtitle ) != null)
			if (inTime(subAux, timeMillis))
				return subAux;

		return null;
	}

	/**
	 * This method is going to check if a given subtitle is between the given timeMillis of your player
	 * Method responsavel por testar se um subtititulo est� dentro do tempo buscado.
	 * @param subtitle
	 * @param timeMillis
	 * @return
	 */
	private static boolean inTime(final Subtitle subtitle, long timeMillis) {
		return timeMillis >= subtitle.timeIn && timeMillis <= subtitle.timeOut;
	}

	/**
	 * This method will sync your srt file adding or subtracting the value in @param timeInMillis
	 * Método responsavel por realizar a sincronização do subtitulo e escrever um novo arquivo com o novo tempo sincronizado no arquivo
	 * @param listSubtitles
	 * @param timeInMillis
	 * @return
	 */
	public static boolean speedSynchronization (final ArrayList<Subtitle> listSubtitles, long timeInMillis, File fileOut) {

		if (listSubtitles == null || listSubtitles.isEmpty() || timeInMillis == 0 || fileOut == null)
			return false;

		try (
				FileOutputStream fos = new FileOutputStream(fileOut);
				OutputStreamWriter osw = new OutputStreamWriter(fos, DEFAULT_CHARSET);
				BufferedWriter bos = new BufferedWriter(osw);
				) {

			for (Subtitle subtitle : listSubtitles) {
				bos.write(String.valueOf(subtitle.id));
				bos.newLine();
				bos.write(SRTUtils.millisToText(subtitle.timeIn + timeInMillis, subtitle.timeOut + timeInMillis));
				bos.newLine();
				bos.write(subtitle.text);
				bos.newLine();
			}
			bos.flush();
			return true;
		} catch (Exception e) {
			logger.error("error writing a new srt file", e);
		}
		return false;
	}
}

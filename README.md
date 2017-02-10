# SRTParser

## Java SRTParser.

SRTParser is a project Java 1.7+, maven.

##### What is this project for?
To parse a .srt file and return an ArrayList<Subtitle> of subtitles and to find a subtitle text by a time.

##### Use
For testing, we have a class called Test.java in package com.gustavo.test, or look code bellow

```	
	String path = System.getProperty("user.dir") + "\\files\\sub.srt";
	
	ArrayList<Subtitle> subtitles = SRTReader
			.getSubtitlesFromFile(path, true);

	for (Subtitle subtitle : subtitles) {
		logger.info(subtitle); // look toString
	}
```

Version 0.0.1

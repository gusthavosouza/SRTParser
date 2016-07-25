# SRTReader

## Java SRTReader.

This projec is a Java SRT Reader, for read srt files and return a Subtitle object.

For testing, we have a class called Test.java in package com.gustavo.test, or look code bellow

```	
	String path = System.getProperty("user.dir") + "\\files\\sub.srt";
	
	ArrayList<Subtitle> subtitles = SRTReader
			.getSubtitlesFromFile(path, true);

	for (Subtitle subtitle : subtitles) {
		System.out.println(subtitle); // look toString
	}
```

Version 1.0
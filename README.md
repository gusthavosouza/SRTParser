# SRTParser

SRTParser is a small project/library for dealing with SRT files.

## Installation

As a light weight version of the code that has been optimized for Android devices, the best way to make use of it is cloning the code and adding it to your project.

##### What is this project for?

The purpose of this project is to give Java or Android developers an easy, fast, performant and light weight srt library.

You can easily read srt files, re-sync and search for a specific subtitle in your player, or anything that is needed a subtitle.

##### The usage of the project:
For testing, we have a class called Test.java in package com.gustavo.test, or look code bellow

```	
    // Example file under
    
    private final static Logger logger = LogManager.getLogger(MyClass.class);
    
	String path = System.getProperty("user.dir") + "\\files\\sub.srt";
	
	boolean keepNewLinesEscaped = true;
	
	ArrayList<Subtitle> subtitles = SRTParser.getSubtitlesFromFile(path, keepNewLinesEscaped);

	for (Subtitle subtitle : subtitles) {
		logger.info(subtitle);
		// Print out: Subtitle [id=1, startTime=00:00:01,500, endTime=00:00:02,500, text=Testing str file., timeIn=1500, timeOut=2500]
	}
```

Version 0.0.2

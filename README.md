# SRTParser

SRTParser is everything that you need to deal with SRT files, legend files.

## Installation

As a light weight version of the code that has been optimized for Android devices, the best way to make use of it is cloning the code and adding it to your project.

## Contributing
Pull requests are welcome.

Please make sure to update tests as appropriate.

## What is this project for?

The purpose of this project is to give Java or Android developers an easy, fast, performant and light weight srt library.

You can easily read srt files, re-sync and search for a specific subtitle in your player, or anything that is needed a subtitle.

## The usage of the project:

```	
import main.gusthavo.srt.SRTParser;
import main.gusthavo.srt.Subtitle;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MyClass {

    private final static Logger logger = Logger.getLogger(MyClass.class.getName());

    public static void main(String[] args) {
        String path = System.getProperty("user.dir") + File.separator + "files" + File.separator +"sub.srt";

        boolean keepNewLinesEscaped = true;

        ArrayList<Subtitle> subtitles = SRTParser.getSubtitlesFromFile(path, keepNewLinesEscaped);

        for (Subtitle subtitle : subtitles) {
            logger.info(subtitle.toString());
            // Print out: Subtitle [id=1, startTime=00:00:01,500, endTime=00:00:02,500, text=Testing str file., timeIn=1500, timeOut=2500]
        }
    }
}
```

Version 0.0.2

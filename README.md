[![badge-jdk](https://img.shields.io/badge/jdk-8-green.svg)](https://www.oracle.com/java/technologies/downloads/)
[![License badge](https://img.shields.io/badge/license-MIT-green.svg)](https://opensource.org/license/mit)

# jtube
Java library for parsing: videos,shorts,playlists,channels and downloading videos
 
## Description

I don't like web downloaders with adds and low info that's why I present to you jtube.

*jtube* is a lightweight library written in Java. It has some dependencies, but that's not a problem.

*jtube* allows you to get information about videos and more, and then download them with the ability to track the status.

## Features

- Support for video,shorts,playlist,channel
- Support for downloading video,subtitles,audio tracks with special language
- Ability to capture thumbnail URL
- Extensively documented source code

## Quickstart
Will be later

### Installation

jtube requires an installation of Java 8+

To install from Gradle:

```gradle
implementation 'io.github.x45iq:jtube:1.0.1'
```
To install from Maven:

```maven
<dependency>
    <groupId>io.github.x45iq</groupId>
    <artifactId>jtube</artifactId>
    <version>1.0.1</version>
</dependency>
```

### Using jtube

To get video by url

```java
if(VideoParser.isUrlSupported(url)){
    Video video = new VideoParser().parse(url);
}
```

To get all videos in playlist

```java
if(PlaylistParser.isUrlSupported(url)) {
    Playlist playlist = new PlaylistParser().parse(url);
    playlist.videos().forEach(System.out::println);
}
```

To get all videos in channel

```java
if(ChannelParser.isUrlSupported(url)) {
    Channel channel = new ChannelParser().parse(url);
    channel.videos().forEach(System.out::println);
}
```

To download any StreamingData from video

```java
StreamingData streamingData = ...;
File folderToSave = new File("...");
File result = new StreamingDataDownloader.Builder()
        .streamingData(streamingData)
        .folder(folderToSave)
        .progressCallback(System.out::println)
        .build()
        .download();
```

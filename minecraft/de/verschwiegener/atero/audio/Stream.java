package de.verschwiegener.atero.audio;

public class Stream {

  private final Provider provider;
  private final String channelName;
  private final String channelURL;
  private final String title;
  private final String artist;
  private final String coverURL;

  public Stream(
      Provider provider,
      String channelName,
      String channelURL,
      String title,
      String artist,
      String coverURL) {
    this.provider = provider;
    this.channelName = channelName;
    this.channelURL = channelURL;
    this.title = title;
    this.artist = artist;
    this.coverURL = coverURL;
  }

  public String getChannelName() {
    return channelName;
  }

  public String getChannelURL() {
    return channelURL;
  }

  public String getTitle() {
    return title;
  }

  public String getArtist() {
    return artist;
  }

  public String getCoverURL() {
    return coverURL;
  }

  public enum Provider {
    ILoveMusik
  }
}

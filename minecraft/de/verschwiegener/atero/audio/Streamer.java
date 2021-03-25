package de.verschwiegener.atero.audio;

import java.io.IOException;
import java.net.URL;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class Streamer {
  BasicPlayer bp;

  public Streamer() {
    bp = new BasicPlayer();
  }

  public synchronized void play(double gain) {
    try {
      bp.stop();
      bp.open(new URL("https://streams.ilovemusic.de/iloveradio109.mp3").openStream());
      bp.play();
      bp.setGain(gain);
    } catch (BasicPlayerException | IOException e) {
      e.printStackTrace();
    }
  }

  public synchronized void stop() {
    try {
      bp.pause();
    } catch (BasicPlayerException e) {
      e.printStackTrace();
    }
  }
}

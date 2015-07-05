package com.mypodcasts.player;

import android.media.MediaPlayer;

import com.mypodcasts.podcast.models.Episode;

import java.io.IOException;

public interface AudioPlayer {
  MediaPlayer play(Episode episode) throws IOException;

  void release();

  boolean isPlaying();
}

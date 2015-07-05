package com.mypodcasts.player;

import android.media.MediaPlayer;

import com.mypodcasts.podcast.models.Episode;

import java.io.IOException;

public interface AudioPlayer {
  MediaPlayer play(Episode episode) throws IOException;

  MediaPlayer pause();

  MediaPlayer unPause(Episode episode);

  MediaPlayer play(Episode episode, int seekTo);

  void release();

  boolean isPlaying();
}

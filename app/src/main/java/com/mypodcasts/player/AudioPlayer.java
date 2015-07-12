package com.mypodcasts.player;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.widget.MediaController.MediaPlayerControl;

import com.mypodcasts.podcast.models.Episode;

import java.io.IOException;

public interface AudioPlayer extends MediaPlayerControl, OnPreparedListener {
  MediaPlayer play(Episode episode) throws IOException;

  void release();
}

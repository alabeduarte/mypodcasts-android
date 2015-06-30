package com.mypodcasts.player;

import android.media.MediaPlayer;
import android.os.Bundle;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;

public class AudioPlayerActivity extends RoboActivity {

  @Inject
  private MediaPlayer mediaPlayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    startPlayer();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mediaPlayer.reset();
    mediaPlayer.release();
  }

  private void startPlayer() {
    if (!mediaPlayer.isPlaying()) {
      mediaPlayer.start();
    }
  }
}

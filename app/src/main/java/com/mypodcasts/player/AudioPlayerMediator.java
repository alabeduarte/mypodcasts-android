package com.mypodcasts.player;

import android.content.Context;
import android.content.Intent;

import com.mypodcasts.podcast.models.Episode;

import javax.inject.Inject;

public class AudioPlayerMediator {

  private final AudioPlayer audioPlayer;

  @Inject
  public AudioPlayerMediator(AudioPlayer audioPlayer) {
    this.audioPlayer = audioPlayer;
  }

  public boolean isPlaying() {
    return audioPlayer.isPlaying();
  }

  public AudioPlayerMediator startService(Context context, Episode episode) {
    Intent intent = new Intent(context, AudioPlayerService.class);

    intent.putExtra(Episode.class.toString(), episode);
    context.startService(intent);

    return this;
  }

  public void togglePlayPauseFor(Episode episode) {
    if (audioPlayer.isPlaying()) {
      audioPlayer.pause();
    } else {
      audioPlayer.unPause(episode);
    }
  }
}

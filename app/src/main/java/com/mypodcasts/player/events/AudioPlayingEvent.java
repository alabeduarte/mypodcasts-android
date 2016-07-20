package com.mypodcasts.player.events;

import com.mypodcasts.player.AudioPlayer;

public class AudioPlayingEvent {
  private final AudioPlayer audioPlayer;

  public AudioPlayingEvent(AudioPlayer audioPlayer) {
    this.audioPlayer = audioPlayer;
  }

  public AudioPlayer getAudioPlayer() {
    return audioPlayer;
  }
}

package com.mypodcasts.player;

public class AudioPlayingEvent {
  private final AudioPlayerService audioPlayerService;

  public AudioPlayingEvent(AudioPlayerService audioPlayerService) {
    this.audioPlayerService = audioPlayerService;
  }
}

package com.mypodcasts.player;

import android.media.MediaPlayer;

import com.mypodcasts.podcast.models.Episode;

import java.io.IOException;

import javax.inject.Inject;

import static android.media.AudioManager.STREAM_MUSIC;

public class AudioPlayerStreaming implements AudioPlayer {

  private final MediaPlayer mediaPlayer;

  @Inject
  public AudioPlayerStreaming(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
    this.mediaPlayer.setAudioStreamType(STREAM_MUSIC);
  }

  @Override
  public MediaPlayer play(Episode episode) throws IOException {
    mediaPlayer.setDataSource(episode.getAudio().getUrl());
    mediaPlayer.prepare();
    mediaPlayer.start();

    return mediaPlayer;
  }

  @Override
  public MediaPlayer pause() {
    mediaPlayer.pause();

    return mediaPlayer;
  }

  @Override
  public MediaPlayer unPause(Episode episode) {
    return play(episode, mediaPlayer.getCurrentPosition());
  }

  @Override
  public MediaPlayer play(Episode episode, int millisecond) {
    mediaPlayer.seekTo(millisecond);
    mediaPlayer.start();

    return mediaPlayer;
  }

  @Override
  public void release() {
    mediaPlayer.reset();
    mediaPlayer.release();
  }

  @Override
  public boolean isPlaying() {
    return mediaPlayer.isPlaying();
  }
}
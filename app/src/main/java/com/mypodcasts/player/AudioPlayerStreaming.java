package com.mypodcasts.player;

import android.media.MediaPlayer;

import com.mypodcasts.podcast.models.Episode;

import java.io.IOException;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

import static android.media.AudioManager.STREAM_MUSIC;

public class AudioPlayerStreaming implements AudioPlayer {

  private final MediaPlayer mediaPlayer;
  private final EventBus eventBus;

  @Inject
  public AudioPlayerStreaming(MediaPlayer mediaPlayer, EventBus eventBus) {
    this.mediaPlayer = mediaPlayer;
    this.eventBus = eventBus;
    this.mediaPlayer.setAudioStreamType(STREAM_MUSIC);
  }

  @Override
  public MediaPlayer play(Episode episode) throws IOException {
    mediaPlayer.setDataSource(episode.getAudio().getUrl());
    mediaPlayer.prepare();
    mediaPlayer.setOnPreparedListener(this);

    return mediaPlayer;
  }

  @Override
  public void release() {
    mediaPlayer.reset();
    mediaPlayer.release();
  }

  @Override
  public void start() {
    mediaPlayer.start();
  }

  @Override
  public void pause() {
    mediaPlayer.pause();
  }

  @Override
  public int getDuration() {
    return mediaPlayer.getDuration();
  }

  @Override
  public int getCurrentPosition() {
    return mediaPlayer.getCurrentPosition();
  }

  @Override
  public void seekTo(int pos) {
    mediaPlayer.seekTo(pos);
  }

  @Override
  public boolean isPlaying() {
    return mediaPlayer.isPlaying();
  }

  @Override
  public int getBufferPercentage() {
    return 0;
  }

  @Override
  public boolean canPause() {
    return true;
  }

  @Override
  public boolean canSeekBackward() {
    return true;
  }

  @Override
  public boolean canSeekForward() {
    return true;
  }

  @Override
  public int getAudioSessionId() {
    return mediaPlayer.getAudioSessionId();
  }

  @Override
  public void onPrepared(MediaPlayer mediaPlayer) {
    mediaPlayer.start();
    eventBus.post(new AudioPlayingEvent(this));
  }
}

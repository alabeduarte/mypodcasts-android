package com.mypodcasts.player;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.widget.MediaController.MediaPlayerControl;

import com.mypodcasts.episodes.EpisodeFile;
import com.mypodcasts.player.events.AudioPlayingEvent;
import com.mypodcasts.repositories.models.Episode;

import java.io.IOException;

import javax.inject.Inject;

import org.greenrobot.eventbus.EventBus;

import static android.media.AudioManager.STREAM_MUSIC;

public class AudioPlayer implements MediaPlayerControl, OnPreparedListener {

  private final MediaPlayer mediaPlayer;
  private final EventBus eventBus;
  private final EpisodeFile episodeFile;

  @Inject
  public AudioPlayer(MediaPlayer mediaPlayer, EventBus eventBus, EpisodeFile episodeFile) {
    this.mediaPlayer = mediaPlayer;
    this.eventBus = eventBus;
    this.episodeFile = episodeFile;
  }

  public MediaPlayer play(Episode episode) throws IOException {
    mediaPlayer.setAudioStreamType(STREAM_MUSIC);
    mediaPlayer.setDataSource(episodeFile.getAudioFilePath(episode));
    mediaPlayer.setOnPreparedListener(this);

    mediaPlayer.prepareAsync();
    return mediaPlayer;
  }

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

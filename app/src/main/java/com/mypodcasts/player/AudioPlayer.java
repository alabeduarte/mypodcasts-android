package com.mypodcasts.player;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.widget.MediaController.MediaPlayerControl;

import com.mypodcasts.podcast.models.Episode;
import com.mypodcasts.support.ExternalPublicFileLookup;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

import static android.media.AudioManager.STREAM_MUSIC;
import static android.os.Environment.DIRECTORY_PODCASTS;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class AudioPlayer implements MediaPlayerControl, OnPreparedListener {

  private final MediaPlayer mediaPlayer;
  private final EventBus eventBus;
  private final ExternalPublicFileLookup externalPublicFileLookup;

  @Inject
  public AudioPlayer(MediaPlayer mediaPlayer, EventBus eventBus, ExternalPublicFileLookup externalPublicFileLookup) {
    this.mediaPlayer = mediaPlayer;
    this.eventBus = eventBus;
    this.externalPublicFileLookup = externalPublicFileLookup;
  }

  public MediaPlayer play(Episode episode) throws IOException {
    mediaPlayer.setAudioStreamType(STREAM_MUSIC);
    mediaPlayer.setDataSource(getAudioFilePath(episode));

    mediaPlayer.prepare();
    mediaPlayer.setOnPreparedListener(this);

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

  private String getAudioFilePath(Episode episode) {
    File directory = getExternalStoragePublicDirectory(DIRECTORY_PODCASTS);
    if (externalPublicFileLookup.exists(directory, episode.getAudioFilePath())) {
      return directory + "/" + episode.getAudioFilePath();
    }

    return episode.getAudio().getUrl();
  }
}

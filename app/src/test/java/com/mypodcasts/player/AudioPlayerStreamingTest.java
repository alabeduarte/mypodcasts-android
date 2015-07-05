package com.mypodcasts.player;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.mypodcasts.podcast.models.Audio;
import com.mypodcasts.podcast.models.Episode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AudioPlayerStreamingTest {

  AudioPlayerStreaming audioPlayerStreaming;

  MediaPlayer mediaPlayerMock = mock(MediaPlayer.class);

  Episode episode = new Episode() {
    @Override
    public Audio getAudio() {
      return new Audio() {
        @Override
        public String getUrl() {
          return "http://example.com/audio.mp3";
        }
      };
    }
  };

  @Before
  public void setup() {
    audioPlayerStreaming = new AudioPlayerStreaming(mediaPlayerMock);
  }

  @Test
  public void itSetsAudioStreamType() throws IOException {
    verify(mediaPlayerMock).setAudioStreamType(AudioManager.STREAM_MUSIC);
  }

  @Test
  public void itPlaysGivenEpisodeUrl() throws IOException {
    audioPlayerStreaming.play(episode);

    InOrder order = inOrder(mediaPlayerMock);

    order.verify(mediaPlayerMock).setDataSource(episode.getAudio().getUrl());
    order.verify(mediaPlayerMock).prepare();
    order.verify(mediaPlayerMock).start();
  }

  @Test
  public void itPausesMediaPlayer() {
    audioPlayerStreaming.pause();

    verify(mediaPlayerMock).pause();
  }

  @Test
  public void itPlaysGivenEpisodeUrlAndSeekToMilliseconds() throws IOException {
    int msec = 1000;
    audioPlayerStreaming.play(episode, msec);

    InOrder order = inOrder(mediaPlayerMock);

    order.verify(mediaPlayerMock, never()).setDataSource(episode.getAudio().getUrl());
    order.verify(mediaPlayerMock, never()).prepare();

    order.verify(mediaPlayerMock).seekTo(msec);
    order.verify(mediaPlayerMock).start();
  }

  @Test
  public void itUnPausesGivenCurrentPosition() throws IOException {
    int msec = 1000;
    when(mediaPlayerMock.getCurrentPosition()).thenReturn(msec);

    audioPlayerStreaming.unPause(episode);

    InOrder order = inOrder(mediaPlayerMock);
    order.verify(mediaPlayerMock, never()).setDataSource(episode.getAudio().getUrl());
    order.verify(mediaPlayerMock, never()).prepare();
    order.verify(mediaPlayerMock).seekTo(msec);
    order.verify(mediaPlayerMock).start();
  }

  @Test
  public void itReleasesMediaPlayer() {
    audioPlayerStreaming.release();

    InOrder order = inOrder(mediaPlayerMock);

    order.verify(mediaPlayerMock).reset();
    order.verify(mediaPlayerMock).release();
  }

  @Test
  public void itReturnsFalseIfMediaPlayerIsNotPlaying() {
    when(mediaPlayerMock.isPlaying()).thenReturn(false);

    assertThat(audioPlayerStreaming.isPlaying(), is(false));
  }

  @Test
  public void itReturnsTrueIfMediaPlayerIsPlaying() {
    when(mediaPlayerMock.isPlaying()).thenReturn(true);

    assertThat(audioPlayerStreaming.isPlaying(), is(true));
  }
}
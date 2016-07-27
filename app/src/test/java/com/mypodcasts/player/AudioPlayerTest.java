package com.mypodcasts.player;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.episodes.EpisodeFile;
import com.mypodcasts.player.events.AudioPlayingEvent;
import com.mypodcasts.repositories.models.Audio;
import com.mypodcasts.repositories.models.Episode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import de.greenrobot.event.EventBus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AudioPlayerTest {

  AudioPlayer audioPlayer;

  MediaPlayer mediaPlayerMock = mock(MediaPlayer.class);
  EventBus eventBusMock = mock(EventBus.class);

  EpisodeFile episodeFileMock = mock(EpisodeFile.class);

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

    @Override
    public String getAudioFilePath() {
      return "audio.mp3";
    }
  };

  @Before
  public void setup() {
    audioPlayer = new AudioPlayer(mediaPlayerMock, eventBusMock, episodeFileMock);
  }

  @Test
  public void itSetsAudioStreamType() throws IOException {
    audioPlayer.play(episode);

    verify(mediaPlayerMock).setAudioStreamType(AudioManager.STREAM_MUSIC);
  }

  @Test
  public void itSetsDataSourceAndPreparesMediaPlayerGivenAnEpisodeAudioPath() throws IOException {
    String episodeAutioPath = "audio.mp3";

    when(episodeFileMock.getAudioFilePath(episode)).thenReturn(episodeAutioPath);

    audioPlayer.play(episode);

    InOrder order = inOrder(mediaPlayerMock);

    order.verify(mediaPlayerMock).setDataSource(episodeAutioPath);
    order.verify(mediaPlayerMock).prepareAsync();
  }

  @Test
  public void itStartsMediaPlayerOnPrepared() {
    audioPlayer.onPrepared(mediaPlayerMock);

    verify(mediaPlayerMock).start();
  }

  @Test
  public void itBroadcastsEventThatAudioIsPlaying() throws IOException {
    audioPlayer.onPrepared(mediaPlayerMock);

    verify(eventBusMock).post(any(AudioPlayingEvent.class));
  }

  @Test
  public void itStartsMediaPlayer() throws IOException {
    audioPlayer.start();

    verify(mediaPlayerMock).start();
  }

  @Test
  public void itPausesMediaPlayer() {
    audioPlayer.pause();

    verify(mediaPlayerMock).pause();
  }

  @Test
  public void itReturnsDuration() {
    audioPlayer.getDuration();

    verify(mediaPlayerMock).getDuration();
  }

  @Test
  public void itReturnsCurrentPosition() {
    audioPlayer.getCurrentPosition();

    verify(mediaPlayerMock).getCurrentPosition();
  }

  @Test
  public void itAppliesSeekTo() {
    int msec = 1000;
    audioPlayer.seekTo(msec);

    verify(mediaPlayerMock).seekTo(msec);
  }

  @Test
  public void itAllowsPause() {
    assertThat(audioPlayer.canPause(), is(true));
  }

  @Test
  public void itAllowsSeekBackward() {
    assertThat(audioPlayer.canSeekBackward(), is(true));
  }

  @Test
  public void itAllowsSeekForward() {
    assertThat(audioPlayer.canSeekForward(), is(true));
  }

  @Test
  public void itReturnsAudioSessionId() {
    audioPlayer.getAudioSessionId();

    verify(mediaPlayerMock).getAudioSessionId();
  }

  @Test
  public void itReleasesMediaPlayer() {
    audioPlayer.release();

    InOrder order = inOrder(mediaPlayerMock);

    order.verify(mediaPlayerMock).reset();
    order.verify(mediaPlayerMock).release();
  }

  @Test
  public void itReturnsFalseIfMediaPlayerIsNotPlaying() {
    when(mediaPlayerMock.isPlaying()).thenReturn(false);

    assertThat(audioPlayer.isPlaying(), is(false));
  }

  @Test
  public void itReturnsTrueIfMediaPlayerIsPlaying() {
    when(mediaPlayerMock.isPlaying()).thenReturn(true);

    assertThat(audioPlayer.isPlaying(), is(true));
  }
}
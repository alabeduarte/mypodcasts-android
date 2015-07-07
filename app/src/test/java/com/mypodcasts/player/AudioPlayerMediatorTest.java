package com.mypodcasts.player;

import android.app.Activity;
import android.content.Intent;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.podcast.models.Episode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AudioPlayerMediatorTest {

  AudioPlayerMediator audioPlayerMediator;
  AudioPlayer audioPlayerMock = mock(AudioPlayer.class);

  Episode episode = new Episode();

  @Before
  public void setup() {
    audioPlayerMediator = new AudioPlayerMediator(audioPlayerMock);
  }

  @Test
  public void itPlaysAudioStreamingGivenAnEpisode() throws IOException {
    Activity activity = createActivity();

    audioPlayerMediator.startService(activity, episode);

    Intent intent = shadowOf(activity).peekNextStartedService();
    assertThat(
        AudioPlayerService.class.getCanonicalName(),
        is(intent.getComponent().getClassName())
    );
  }

  @Test
  public void itReturnsIfIsPlaying() {
    audioPlayerMediator.isPlaying();

    verify(audioPlayerMock).isPlaying();
  }

  @Test
  public void itPausesIfPlaying() {
    when(audioPlayerMock.isPlaying()).thenReturn(true);

    audioPlayerMediator.togglePlayPauseFor(episode);

    verify(audioPlayerMock).pause();
  }

  @Test
  public void itUnPausesIfNotPlaying() {
    when(audioPlayerMock.isPlaying()).thenReturn(false);

    audioPlayerMediator.togglePlayPauseFor(episode);

    verify(audioPlayerMock).unPause(episode);
  }

  private Activity createActivity() {
    return buildActivity(Activity.class).create().get();
  }

}
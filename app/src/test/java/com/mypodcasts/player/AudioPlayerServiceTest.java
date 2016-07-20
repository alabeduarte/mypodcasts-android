package com.mypodcasts.player;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.episodes.feeds.FeedEpisodesActivity;
import com.mypodcasts.repositories.models.Episode;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildService;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;
import static roboguice.RoboGuice.Util.reset;
import static roboguice.RoboGuice.overrideApplicationInjector;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AudioPlayerServiceTest {
  Episode episode = new Episode();

  AudioPlayer audioPlayerMock = mock(AudioPlayer.class);

  @Before
  public void setup() {
    overrideApplicationInjector(application, new MyTestModule());
  }

  @After
  public void teardown() {
    reset();
  }

  @Test
  public void itPlaysAudioOnActionPlay() throws IOException {
    createService(intentWithAction(AudioPlayerService.ACTION_PLAY));

    verify(audioPlayerMock).play(episode);
  }

  @Test
  public void itDoesNotPlayAudioWhenActionPlayIsNotFired() throws IOException {
    createService(intentWithAction("UNKNOWN_ACTION"));

    verify(audioPlayerMock, never()).play(episode);
  }

  @Test
  public void itPausesAudioOnActionPause() throws IOException {
    createService(intentWithAction(AudioPlayerService.ACTION_PAUSE));

    verify(audioPlayerMock).pause();
  }

  @Test
  public void itRewindsAudioOnActionRewind() throws IOException {
    createService(intentWithAction(AudioPlayerService.ACTION_REWIND));

    int currentPosition = 0;

    verify(audioPlayerMock).seekTo(currentPosition - AudioPlayerService.POSITION);
  }

  @Test
  public void itForwardsAudioOnActionFastForward() throws IOException {
    createService(intentWithAction(AudioPlayerService.ACTION_FAST_FORWARD));

    int currentPosition = 0;

    verify(audioPlayerMock).seekTo(currentPosition + AudioPlayerService.POSITION);
  }

  @Test
  public void itPausesAudioAndReleaseNotificationOnActionStop() throws IOException {
    createService(intentWithAction(AudioPlayerService.ACTION_STOP));

    verify(audioPlayerMock).pause();
  }

  @Test
  public void itReleasesAudioPlayerOnDestroy() {
    Intent intent = intentWithAction(AudioPlayerService.ACTION_PLAY);
    buildService(AudioPlayerService.class).withIntent(intent).create().destroy().get();

    verify(audioPlayerMock).release();
  }

  private Service createService(Intent intent) {
    return buildService(AudioPlayerService.class)
      .withIntent(intent)
      .create()
      .startCommand(0, 1)
      .get();
  }

  private Intent intentWithAction(String action) {
    Intent intent = new Intent(application, AudioPlayerService.class);
    intent.putExtra(Episode.class.toString(), episode);
    intent.setAction(action);

    return intent;
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(AudioPlayer.class).toInstance(audioPlayerMock);
    }
  }
}
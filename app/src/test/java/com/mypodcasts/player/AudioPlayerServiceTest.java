package com.mypodcasts.player;

import android.content.Intent;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.repositories.models.Episode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildService;
import static org.robolectric.RuntimeEnvironment.application;
import static roboguice.RoboGuice.Util.reset;
import static roboguice.RoboGuice.overrideApplicationInjector;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AudioPlayerServiceTest {
  AudioPlayerService service;
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
    service = buildService(AudioPlayerService.class).withIntent(intent).create().destroy().get();

    verify(audioPlayerMock).release();
  }

  private void createService(Intent intent) {
    service = buildService(AudioPlayerService.class)
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
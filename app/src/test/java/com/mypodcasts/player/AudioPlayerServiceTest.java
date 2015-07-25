package com.mypodcasts.player;

import android.app.Notification;
import android.content.Intent;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.podcast.models.Episode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
  Notification.Builder notificationBuilderMock = mock(Notification.Builder.class);
  Notification notificationMock = mock(Notification.class);

  @Before
  public void setup() {
    overrideApplicationInjector(application, new MyTestModule());

    when(notificationBuilderMock.setContentTitle(
        (CharSequence) anyObject())
    ).thenReturn(notificationBuilderMock);
    when(notificationBuilderMock.setContentText(
            (CharSequence) anyObject())
    ).thenReturn(notificationBuilderMock);

    when(notificationBuilderMock.build())
        .thenReturn(notificationMock);
  }

  @After
  public void teardown() {
    reset();
  }

  @Test
  public void itSetsNotificationContentTitle() {
    createService();

    verify(notificationBuilderMock).setContentTitle("My Podcasts");
  }

  @Test
  public void itSetsNotificationContentText() {
    createService();

    verify(notificationBuilderMock).setContentText("Some awesome podcast!");
  }

  @Test
  public void itPlaysAudioStreamingGivenAnEpisode() throws IOException {
    createService();

    verify(audioPlayerMock).play(episode);
  }

  @Test
  public void itReleasesAudioPlayerOnDestroy() {
    Intent intent = getIntent();
    service = buildService(AudioPlayerService.class).withIntent(intent).create().destroy().get();

    verify(audioPlayerMock).release();
  }

  private void createService() {
    Intent intent = getIntent();

    service = buildService(AudioPlayerService.class)
      .withIntent(intent)
      .create()
      .startCommand(0, 1)
      .get();
  }

  private Intent getIntent() {
    Intent intent = new Intent(application, AudioPlayerService.class);
    intent.putExtra(Episode.class.toString(), episode);
    return intent;
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(AudioPlayer.class).toInstance(audioPlayerMock);
      bind(Notification.Builder.class).toInstance(notificationBuilderMock);
    }
  }
}
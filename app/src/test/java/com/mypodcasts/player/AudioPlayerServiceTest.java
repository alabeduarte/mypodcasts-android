package com.mypodcasts.player;

import android.app.Service;
import android.content.Intent;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.player.events.AudioStoppedEvent;
import com.mypodcasts.repositories.models.Episode;

import org.greenrobot.eventbus.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import org.greenrobot.eventbus.EventBus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildService;
import static org.robolectric.RuntimeEnvironment.application;
import static roboguice.RoboGuice.Util.reset;
import static roboguice.RoboGuice.overrideApplicationInjector;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AudioPlayerServiceTest {
  Episode episode = new Episode();

  AudioPlayer audioPlayerMock = mock(AudioPlayer.class);
  EventBus eventBus;

  @Before
  public void setup() {
    eventBus = EventBus.getDefault();

    overrideApplicationInjector(application, new MyTestModule());
  }

  @After
  public void teardown() {
    reset();
  }

  @Test
  public void itPlaysAudioOnActionPlay() throws IOException {
    createService(buildIntent(episode, AudioPlayerService.ACTION_PLAY));

    verify(audioPlayerMock).play(episode);
  }

  @Test
  public void itDoesNotPlayAudioWhenActionPlayIsNotFired() throws IOException {
    createService(buildIntent(episode, "UNKNOWN_ACTION"));

    verify(audioPlayerMock, never()).play(episode);
  }

  @Test
  public void itPausesAudioOnActionPause() throws IOException {
    createService(buildIntent(episode, AudioPlayerService.ACTION_PAUSE));

    verify(audioPlayerMock).pause();
  }

  @Test
  public void itRewindsAudioOnActionRewind() throws IOException {
    createService(buildIntent(episode, AudioPlayerService.ACTION_REWIND));

    int currentPosition = 0;

    verify(audioPlayerMock).seekTo(currentPosition - AudioPlayerService.POSITION);
  }

  @Test
  public void itForwardsAudioOnActionFastForward() throws IOException {
    createService(buildIntent(episode, AudioPlayerService.ACTION_FAST_FORWARD));

    int currentPosition = 0;

    verify(audioPlayerMock).seekTo(currentPosition + AudioPlayerService.POSITION);
  }

  @Test
  public void itPausesAudioAndTriggerAudioStoppedEventOnActionStop() throws IOException {
    CustomBroadcastReceiver customBroadcastReceiver = new CustomBroadcastReceiver(eventBus);
    assertThat(customBroadcastReceiver.isMessageReceived(), is(false));

    createService(buildIntent(episode, AudioPlayerService.ACTION_STOP));

    verify(audioPlayerMock).pause();
    assertThat(customBroadcastReceiver.isMessageReceived(), is(true));
  }

  @Test
  public void itReleasesAudioPlayerOnDestroy() {
    Intent intent = buildIntent(episode, AudioPlayerService.ACTION_PLAY);
    buildService(AudioPlayerService.class, intent).create().destroy().get();

    verify(audioPlayerMock).release();
  }

  private Service createService(Intent intent) {
    return buildService(AudioPlayerService.class, intent)
      .create()
      .startCommand(0, 1)
      .get();
  }

  private Intent buildIntent(Episode episode, String action) {
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

  public class CustomBroadcastReceiver {
    private boolean isMessageReceived;

    public CustomBroadcastReceiver(EventBus eventBus) {
      eventBus.register(this);
    }

    @Subscribe
    public void onEvent(AudioStoppedEvent event) {
      this.isMessageReceived = true;
    }

    public boolean isMessageReceived() {
      return isMessageReceived;
    }
  }
}
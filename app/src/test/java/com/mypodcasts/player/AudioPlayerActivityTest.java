package com.mypodcasts.player;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Episode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import de.greenrobot.event.EventBus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;
import static roboguice.RoboGuice.Util.reset;
import static roboguice.RoboGuice.overrideApplicationInjector;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AudioPlayerActivityTest {

  AudioPlayerActivity activity;
  Episode episode = new Episode();

  ProgressDialog progressDialogMock = mock(ProgressDialog.class);
  AudioPlayerService audioPlayerServiceMock = mock(AudioPlayerService.class);
  AudioPlayer audioPlayerMock = mock(AudioPlayer.class);

  EventBus eventBusMock = mock(EventBus.class);
  AudioPlayerController mediaControllerMock = mock(AudioPlayerController.class);

  @Before
  public void setup() {
    overrideApplicationInjector(application, new MyTestModule());
  }

  @After
  public void teardown() {
    reset();
  }

  @Test
  public void itRegistersItselfToEventBusOnStart() {
    activity = buildActivity(AudioPlayerActivity.class).create().get();

    verify(eventBusMock).register(activity);
  }

  @Test
  public void itUnregistersItselfToEventBusOnStop() {
    activity = buildActivity(AudioPlayerActivity.class).create().stop().get();

    verify(eventBusMock).unregister(activity);
  }

  @Test
  public void itShowsAndHideProgressDialog() {
    when(progressDialogMock.isShowing()).thenReturn(true);
    String message = application.getString(R.string.loading_episode);

    createActivity();
    activity.onEvent(new AudioPlayingEvent(audioPlayerMock));

    InOrder order = inOrder(progressDialogMock);

    order.verify(progressDialogMock).show();
    order.verify(progressDialogMock).setMessage(message);

    order.verify(progressDialogMock).cancel();
  }

  @Test
  public void itDoNotCancelProgressDialogIfItIsNotShowing() {
    when(progressDialogMock.isShowing()).thenReturn(false);

    createActivity();

    InOrder order = inOrder(progressDialogMock);

    order.verify(progressDialogMock).show();
    order.verify(progressDialogMock, never()).cancel();
  }

  @Test
  public void itStartsAudioServiceGivenAnEpisode() throws IOException {
    createActivity();

    Intent intent = shadowOf(activity).peekNextStartedService();
    assertThat(
        AudioPlayerService.class.getCanonicalName(),
        is(intent.getComponent().getClassName())
    );
  }

  @Test
  public void itShowsMediaControlWhenAudioStartToPlay() {
    createActivity();

    activity.onEvent(new AudioPlayingEvent(audioPlayerMock));

    verify(mediaControllerMock).show();
  }

  @Test
  public void itShowsMediaControlOnTouchEvent() {
    createActivity();

    MotionEvent motionEvent = mock(MotionEvent.class);
    activity.onTouchEvent(motionEvent);

    verify(mediaControllerMock).show();
  }

  @Test
  public void itBindsAudioPlayerWithMediaControl() {
    createActivity();

    activity.onEvent(new AudioPlayingEvent(audioPlayerMock));

    verify(mediaControllerMock).setMediaPlayer(audioPlayerMock);
  }

  @Test
  public void itAnchorsMediaControllerToAudioView() {
    createActivity();

    View audioView = activity.findViewById(R.id.audio_view);
    activity.onEvent(new AudioPlayingEvent(audioPlayerMock));

    verify(mediaControllerMock).setAnchorView(audioView);
  }

  private void createActivity() {
    Intent intent = getIntent();

    activity = buildActivity(AudioPlayerActivity.class).withIntent(intent).create().get();
  }

  private Intent getIntent() {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.putExtra(Episode.class.toString(), episode);
    return intent;
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(EventBus.class).toInstance(eventBusMock);
      bind(ProgressDialog.class).toInstance(progressDialogMock);
      bind(AudioPlayerService.class).toInstance(audioPlayerServiceMock);
      bind(AudioPlayer.class).toInstance(audioPlayerMock);
      bind(AudioPlayerController.class).toInstance(mediaControllerMock);
    }
  }
}
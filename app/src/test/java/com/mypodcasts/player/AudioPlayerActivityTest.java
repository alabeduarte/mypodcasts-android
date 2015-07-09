package com.mypodcasts.player;

import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Button;

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

import static java.lang.String.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
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
  AudioPlayerMediator audioPlayerMediatorMock = mock(AudioPlayerMediator.class);
  private EventBus eventBusMock = mock(EventBus.class);

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
    String message = application.getString(R.string.loading_episode);

    createActivity();

    InOrder order = inOrder(progressDialogMock);

    order.verify(progressDialogMock).show();
    order.verify(progressDialogMock).setMessage(message);

    order.verify(progressDialogMock).cancel();
  }

  @Test
  public void itStartsAudioServiceGivenAnEpisode() throws IOException {
    createActivity();

    verify(audioPlayerMediatorMock).startService(activity, episode);
  }

  @Test
  public void itShowsPlayButtonBeforeSTartPlayingAudio() {
    when(audioPlayerMediatorMock.isPlaying()).thenReturn(false);

    createActivity();

    Button button = playPauseButton();
    String play = application.getResources().getString(R.string.play);

    assertThat(valueOf(button.getText()), is(play));
  }

  @Test
  public void itShowsPauseButtonWhenPlayingAudio() {
    when(audioPlayerMediatorMock.isPlaying()).thenReturn(true);

    createActivity();

    Button button = playPauseButton();
    String pause = application.getResources().getString(R.string.pause);

    assertThat(valueOf(button.getText()), is(pause));
  }

  @Test
  public void itChangesButtonLabelToPlayOnTouchTheButton() {
    when(audioPlayerMediatorMock.isPlaying()).thenReturn(true);
    createActivity();

    Button button = playPauseButton();
    when(audioPlayerMediatorMock.isPlaying()).thenReturn(false);

    button.performClick();

    String play = application.getResources().getString(R.string.play);

    assertThat(valueOf(button.getText()), is(play));
  }

  @Test
  public void itChangesButtonLabelToPauseOnTouchTheButton() {
    when(audioPlayerMediatorMock.isPlaying()).thenReturn(false);
    createActivity();

    Button button = playPauseButton();
    when(audioPlayerMediatorMock.isPlaying()).thenReturn(true);

    button.performClick();

    String pause = application.getResources().getString(R.string.pause);

    assertThat(valueOf(button.getText()), is(pause));
  }

  @Test
  public void itTogglesPlayPauseForEpisodeOnTouchTheButton() {
    createActivity();

    Button button = playPauseButton();
    button.performClick();

    verify(audioPlayerMediatorMock).togglePlayPauseFor(episode);
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

  private Button playPauseButton() {
    return (Button) activity.findViewById(R.id.play_pause_button);
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(EventBus.class).toInstance(eventBusMock);
      bind(ProgressDialog.class).toInstance(progressDialogMock);
      bind(AudioPlayerMediator.class).toInstance(audioPlayerMediatorMock);
    }
  }
}
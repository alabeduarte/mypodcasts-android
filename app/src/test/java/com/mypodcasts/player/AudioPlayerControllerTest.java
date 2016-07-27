package com.mypodcasts.player;

import android.app.Activity;
import android.view.KeyEvent;

import com.mypodcasts.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AudioPlayerControllerTest {

  AudioPlayerController audioPlayerController;

  Activity activity;
  KeyEvent event = mock(KeyEvent.class);

  @Before
  public void setUp() {
    activity = buildActivity(Activity.class).create().get();

    audioPlayerController = new AudioPlayerController(activity);
  }

  @Test
  public void itFinishesActivityWhenKeyCodeBackIsPerformed() {
    when(event.getKeyCode()).thenReturn(KeyEvent.KEYCODE_BACK);

    audioPlayerController.dispatchKeyEvent(event);

    assertThat(activity.isFinishing(), is(true));
  }

  @Test
  public void itDoesNotFinishActivityWhenAnotherKeyCodeIsPerformed() {
    when(event.getKeyCode()).thenReturn(KeyEvent.KEYCODE_UNKNOWN);

    audioPlayerController.dispatchKeyEvent(event);

    assertThat(activity.isFinishing(), is(false));
  }
}
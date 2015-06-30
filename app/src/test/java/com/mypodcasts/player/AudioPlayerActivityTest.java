package com.mypodcasts.player;

import android.content.Intent;
import android.media.MediaPlayer;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.podcast.models.Episode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.RuntimeEnvironment.application;
import static roboguice.RoboGuice.Util.reset;
import static roboguice.RoboGuice.overrideApplicationInjector;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AudioPlayerActivityTest {

  AudioPlayerActivity activity;
  Episode episode = new Episode();

  MediaPlayer mediaPlayerMock = mock(MediaPlayer.class);

  @Before
  public void setup() {
    overrideApplicationInjector(application, new MyTestModule());
  }

  @After
  public void teardown() {
    reset();
  }

  @Test
  public void itStartsPlayerOnActivityCreation() {
    when(mediaPlayerMock.isPlaying()).thenReturn(false);

    createActivity();

    verify(mediaPlayerMock).start();
  }

  @Test
  public void itDoNotStartIfItsAlreadyPlaying() {
    when(mediaPlayerMock.isPlaying()).thenReturn(true);

    createActivity();

    verify(mediaPlayerMock, never()).start();
  }

  @Test
  public void itResetsMediaPlayerOnActivityPause() {
    Intent intent = getIntent();
    activity = buildActivity(AudioPlayerActivity.class).withIntent(intent).create().pause().get();

    verify(mediaPlayerMock).reset();
  }

  @Test
  public void itReleasesMediaPlayerOnActivityPause() {
    Intent intent = getIntent();
    activity = buildActivity(AudioPlayerActivity.class).withIntent(intent).create().pause().get();

    verify(mediaPlayerMock).release();
  }

  private void createActivity() {
    Intent intent = getIntent();

    activity = buildActivity(AudioPlayerActivity.class).withIntent(intent).create().get();
  }

  private Intent getIntent() {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.putExtra(episode.toString(), episode);
    return intent;
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(MediaPlayer.class).toInstance(mediaPlayerMock);
    }
  }
}
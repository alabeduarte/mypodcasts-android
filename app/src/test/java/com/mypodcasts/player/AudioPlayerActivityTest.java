package com.mypodcasts.player;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Audio;
import com.mypodcasts.podcast.models.Episode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.mockito.Mockito.inOrder;
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

  ProgressDialog progressDialogMock = mock(ProgressDialog.class);
  AudioPlayerStreaming audioPlayerStreamingMock = mock(AudioPlayerStreaming.class);

  @Before
  public void setup() {
    overrideApplicationInjector(application, new MyTestModule());
  }

  @After
  public void teardown() {
    reset();
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
  public void itPlaysAudioStreamingGivenAnEpisode() throws IOException {
    createActivity();

    verify(audioPlayerStreamingMock).play(episode);
  }

  @Test
  public void itReleasesAudioPlayerOnActivityPause() {
    Intent intent = getIntent();
    activity = buildActivity(AudioPlayerActivity.class).withIntent(intent).create().pause().get();

    verify(audioPlayerStreamingMock).release();
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
      bind(ProgressDialog.class).toInstance(progressDialogMock);
      bind(AudioPlayerStreaming.class).toInstance(audioPlayerStreamingMock);
    }
  }
}
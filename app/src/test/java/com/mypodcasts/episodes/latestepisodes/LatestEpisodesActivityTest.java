package com.mypodcasts.episodes.latestepisodes;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.episodes.EpisodeList;
import com.mypodcasts.episodes.EpisodeListFragment;
import com.mypodcasts.episodes.EpisodeListHeaderInfo;
import com.mypodcasts.episodes.EpisodeViewInflater;
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.UserPodcasts;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.RuntimeEnvironment.application;
import static roboguice.RoboGuice.Util.reset;
import static roboguice.RoboGuice.overrideApplicationInjector;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LatestEpisodesActivityTest {

  LatestEpisodesActivity activity;
  EpisodeListFragment episodeListFragment = new EpisodeListFragment();

  EpisodeViewInflater episodeViewInflaterMock = mock(EpisodeViewInflater.class);
  UserPodcasts userPodcastsMock = mock(UserPodcasts.class);
  ProgressDialog progressDialogMock = mock(ProgressDialog.class);
  FragmentManager fragmentManager = mock(FragmentManager.class);
  FragmentTransaction transaction = mock(FragmentTransaction.class);

  List<Episode> emptyList = Collections.<Episode>emptyList();

  @Before
  public void setup() {
    overrideApplicationInjector(application, new MyTestModule());
  }

  @After
  public void teardown() {
    reset();
  }

  @Test
  public void itReplacesContentFrameByLatestEpisodesFragment() {
    activity = createActivity();

    InOrder order = inOrder(fragmentManager, transaction);

    order.verify(fragmentManager).beginTransaction();
    order.verify(transaction).replace(R.id.content_frame, episodeListFragment);
    order.verify(transaction).commitAllowingStateLoss();
  }

  @Test
  public void itSetsFragmentHeader() {
    activity = createActivity();

    EpisodeListHeaderInfo headerInfo = (EpisodeListHeaderInfo) episodeListFragment.getArguments()
        .getSerializable(EpisodeList.HEADER);

    assertThat(headerInfo.getTitle(), is(application.getString(R.string.latest_episodes)));
  }

  @Test
  public void itSetsFragmentEpisodeList() {
    activity = createActivityWith(emptyList);

    Bundle arguments = episodeListFragment.getArguments();
    Serializable serializable = arguments.getSerializable(EpisodeList.LIST);
    EpisodeList episodeList = (EpisodeList) serializable;

    assertThat(episodeList.getEpisodes(), is(emptyList));
  }

  @Test
  public void itShowsAndHideProgressDialog() {
    when(progressDialogMock.isShowing()).thenReturn(true);
    String message = application.getString(R.string.loading_latest_episodes);

    activity = createActivityWith(emptyList);

    InOrder order = inOrder(progressDialogMock);

    order.verify(progressDialogMock).show();
    order.verify(progressDialogMock).setMessage(message);

    order.verify(progressDialogMock).cancel();
  }

  @Test
  public void itDoesNotCancelProgressDialogIfItIsNotShowing() {
    when(progressDialogMock.isShowing()).thenReturn(false);

    activity = createActivityWith(emptyList);

    InOrder order = inOrder(progressDialogMock);

    order.verify(progressDialogMock).show();
    order.verify(progressDialogMock, never()).cancel();
  }

  LatestEpisodesActivity createActivityWith(List<Episode> episodes) {
    when(fragmentManager.beginTransaction())
        .thenReturn(transaction);

    when(transaction.replace(R.id.content_frame, episodeListFragment))
        .thenReturn(transaction);

    when(userPodcastsMock.getLatestEpisodes()).thenReturn(episodes);

    return buildActivity(LatestEpisodesActivity.class).create().get();
  }

  private LatestEpisodesActivity createActivity() {
    return createActivityWith(emptyList);
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(ProgressDialog.class).toInstance(progressDialogMock);
      bind(UserPodcasts.class).toInstance(userPodcastsMock);
      bind(FragmentManager.class).toInstance(fragmentManager);
      bind(EpisodeListFragment.class).toInstance(episodeListFragment);
      bind(EpisodeViewInflater.class).toInstance(episodeViewInflaterMock);
    }
  }
}
package com.mypodcasts.feeds.episodes;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.podcast.EpisodeList;
import com.mypodcasts.podcast.EpisodeListFragment;
import com.mypodcasts.podcast.models.Episode;
import com.mypodcasts.podcast.models.Feed;
import com.mypodcasts.podcast.repositories.FeedPodcasts;
import com.mypodcasts.podcast.repositories.UserPodcasts;

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

import static java.util.Arrays.asList;
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
public class FeedEpisodesActivityTest {

  FeedEpisodesActivity activity;
  EpisodeListFragment episodeListFragment = new EpisodeListFragment();

  UserPodcasts userPodcastsMock = mock(UserPodcasts.class);
  FeedPodcasts feedPodcastsMock = mock(FeedPodcasts.class);
  ProgressDialog progressDialogMock = mock(ProgressDialog.class);
  FragmentManager fragmentManager = mock(FragmentManager.class);
  FragmentTransaction transaction = mock(FragmentTransaction.class);

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
    order.verify(transaction).commit();
  }

  @Test
  public void itShowsAndHideProgressDialog() {
    when(progressDialogMock.isShowing()).thenReturn(true);
    String message = application.getString(R.string.loading_latest_episodes);

    activity = createActivityWith(aFeed());

    InOrder order = inOrder(progressDialogMock);

    order.verify(progressDialogMock).show();
    order.verify(progressDialogMock).setMessage(message);

    order.verify(progressDialogMock).cancel();
  }

  @Test
  public void itDoNotCancelProgressDialogIfItIsNotShowing() {
    when(progressDialogMock.isShowing()).thenReturn(false);

    activity = createActivityWith(aFeed());

    InOrder order = inOrder(progressDialogMock);

    order.verify(progressDialogMock).show();
    order.verify(progressDialogMock, never()).cancel();
  }

  @Test
  public void itSetsFragmentTitle() {
    String expectedTitle = "Some title";
    activity = createActivityWith(aFeed(expectedTitle));

    assertThat(
        episodeListFragment.getArguments().getString(EpisodeList.TITLE),
        is(expectedTitle)
    );
  }

  @Test
  public void itSetsFragmentEpisodeList() {
    List<Episode> episodes = asList(anEpisode());
    activity = createActivityWith(aFeedWith(episodes));

    Bundle arguments = episodeListFragment.getArguments();
    Serializable serializable = arguments.getSerializable(EpisodeList.LIST);
    EpisodeList episodeList = (EpisodeList) serializable;

    assertThat(episodeList.getEpisodes(), is(episodes));
  }

  private FeedEpisodesActivity createActivityWith(Feed feed) {
    when(fragmentManager.beginTransaction())
        .thenReturn(transaction);

    when(transaction.replace(R.id.content_frame, episodeListFragment))
        .thenReturn(transaction);

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.putExtra(Feed.class.toString(), feed);

    when(feedPodcastsMock.getFeed(feed.getId())).thenReturn(feed);

    return buildActivity(FeedEpisodesActivity.class)
        .withIntent(intent)
        .create()
        .get();
  }

  private FeedEpisodesActivity createActivity() {
    return createActivityWith(aFeed());
  }

  private Feed aFeed(final String title) {
    return aFeed(title, Collections.<Episode>emptyList());
  }

  private Feed aFeedWith(final List<Episode> episodes) {
    return aFeed(null, episodes);
  }

  private Feed aFeed() {
    return aFeed("Awesome Podcast");
  }

  private Feed aFeed(final String title, final List<Episode> episodes) {
    return new Feed() {
      @Override
      public String getId() {
        return "123";
      }

      @Override
      public String getTitle() {
        return title;
      }

      @Override
      public List<Episode> getEpisodes() {
        return episodes;
      }
    };
  }

  private Episode anEpisode() {
    return new Episode();
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(ProgressDialog.class).toInstance(progressDialogMock);
      bind(UserPodcasts.class).toInstance(userPodcastsMock);
      bind(FeedPodcasts.class).toInstance(feedPodcastsMock);
      bind(FragmentManager.class).toInstance(fragmentManager);
      bind(EpisodeListFragment.class).toInstance(episodeListFragment);
    }
  }
}
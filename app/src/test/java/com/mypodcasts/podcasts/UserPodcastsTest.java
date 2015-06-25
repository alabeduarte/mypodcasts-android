package com.mypodcasts.podcasts;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.rss.Episode;
import com.mypodcasts.rss.Feed;
import com.mypodcasts.stubs.StubbedEpisode;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class UserPodcastsTest {

  UserPodcasts userPodcasts;

  FeedManager feedManagerMock = mock(FeedManager.class);
  Feed feedMock = mock(Feed.class);

  @Before
  public void setup() {
    userPodcasts = new UserPodcasts();
  }

  @Test
  @Ignore
  public void itReturnsLatestEpisodes() {
    List<Episode> expectedEpisodes = new ArrayList<Episode>() {{
      add(new StubbedEpisode());
      add(new StubbedEpisode());
    }};

    when(feedManagerMock.getFeeds()).thenReturn(asList(feedMock));
//    when(feedMock.getEpisodes()).thenReturn(expectedEpisodes);

    userPodcasts = new UserPodcasts();
    List<Episode> latestEpisodes = userPodcasts.getLatestEpisodes();

    assertThat(latestEpisodes, is(expectedEpisodes));
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(FeedManager.class).toInstance(feedManagerMock);
    }
  }

}
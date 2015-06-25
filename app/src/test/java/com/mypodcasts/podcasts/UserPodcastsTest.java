package com.mypodcasts.podcasts;

import com.mypodcasts.rss.Episode;
import com.mypodcasts.rss.Feed;
import com.mypodcasts.stubs.StubbedEpisode;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserPodcastsTest {

  UserPodcasts userPodcasts;

  FeedManager feedManagerMock = mock(FeedManager.class);
  Feed feedMock = mock(Feed.class);

  @Test
  public void itReturnsLatestEpisodes() {
    Episode latestEpisode = new StubbedEpisode();

    when(feedManagerMock.getFeeds()).thenReturn(asList(feedMock));
    when(feedMock.getLatestEpisode()).thenReturn(latestEpisode);

    userPodcasts = new UserPodcasts(feedManagerMock);
    List<Episode> latestEpisodes = userPodcasts.getLatestEpisodes();

    assertThat(latestEpisodes, is(asList(latestEpisode)));
  }
}
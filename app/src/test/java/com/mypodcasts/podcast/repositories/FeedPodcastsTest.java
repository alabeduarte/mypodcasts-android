package com.mypodcasts.podcast.repositories;

import android.content.res.Resources;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Episode;
import com.mypodcasts.podcast.models.Feed;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import retrofit.RestAdapter;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FeedPodcastsTest {

  FeedPodcasts feedPodcasts;

  Resources resources = mock(Resources.class);

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(1111);
  final String feed_path = "/api/feeds/123456";
  final String expectedId = "123456";

  @Before
  public void setup() {
    givenThat(get(urlEqualTo(feed_path))
        .willReturn(aResponse()
            .withStatus(200)
            .withBodyFile("feed.json")));
    when(resources.getString(R.string.base_url)).thenReturn("http://localhost:1111");

    feedPodcasts = new FeedPodcasts(resources, new RestAdapter.Builder());
  }

  @Test
  public void itReturnsFeedId() {
    assertThat(feedPodcasts.getFeed(expectedId).getId(), is(expectedId));
  }

  @Test
  public void itReturnsFeedEpisodes() {
    final Episode newestEpisode = new Episode() {
      @Override
      public String getTitle() {
        return "Newest Episode!";
      }
    };
    final Episode anotherEpisode = new Episode() {
      @Override
      public String getTitle() {
        return "Newest Episode from another podcast";
      }
    };

    Feed feed = new Feed() {
      @Override
      public List<Episode> getEpisodes() {
        return asList(newestEpisode, anotherEpisode);
      }
    };

    assertThat(
        feedPodcasts.getFeed(expectedId).getEpisodes().get(0).getTitle(),
        is(feed.getEpisodes().get(0).getTitle())
    );

    assertThat(
        feedPodcasts.getFeed(expectedId).getEpisodes().get(1).getTitle(),
        is(feed.getEpisodes().get(1).getTitle())
    );
  }
}
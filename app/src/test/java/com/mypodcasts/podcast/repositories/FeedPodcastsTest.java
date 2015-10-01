package com.mypodcasts.podcast.repositories;

import android.content.res.Resources;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Audio;
import com.mypodcasts.podcast.models.Episode;
import com.mypodcasts.podcast.models.Feed;
import com.mypodcasts.podcast.models.Image;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import retrofit.RestAdapter;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FeedPodcastsTest {

  FeedPodcasts feedPodcasts;
  HttpClient httpClient;

  Resources resources = mock(Resources.class);

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(1111);
  final String feed_path = "/api/feeds/123456";
  final String expectedId = "123456";
  private int firstPosition = 0;

  @Before
  public void setup() {
    givenThat(get(urlEqualTo(feed_path))
        .willReturn(aResponse()
            .withStatus(200)
            .withBodyFile("feed.json")));
    when(resources.getString(R.string.base_url)).thenReturn("http://localhost:1111");

    httpClient = new HttpClient(resources, new RestAdapter.Builder());
    feedPodcasts = new FeedPodcasts(httpClient);
  }

  @Test
  public void itReturnsFeedId() {
    Feed feed = feedPodcasts.getFeed(expectedId);

    assertThat(feed.getId(), is(expectedId));
  }

  @Test
  public void itReturnsTitleWhenGetFeeds() {
    Feed feed = feedPodcasts.getFeed(expectedId);
    Episode episode = feed.getEpisodes().get(firstPosition);

    Episode expectedEpisode = new Episode() {
      @Override
      public String getTitle() {
        return "Newest Episode!";
      }
    };

    assertThat(episode.getTitle(), is(expectedEpisode.getTitle()));
  }

  @Test
  public void itReturnsAudioUrlWhenGetFeeds() {
    Feed feed = feedPodcasts.getFeed(expectedId);
    Episode episode = feed.getEpisodes().get(firstPosition);

    Episode expectedEpisode = new Episode() {
      @Override
      public Audio getAudio() {
        return new Audio() {
          @Override
          public String getUrl() {
            return "http://example.com/newest_episode.mp3";
          }
        };
      }
    };

    assertThat(episode.getAudio().getUrl(), is(expectedEpisode.getAudio().getUrl()) );
  }

  @Test
  public void itReturnsImageUrlWhenGetFeeds() {
    Feed feed = feedPodcasts.getFeed(expectedId);
    Episode episode = feed.getEpisodes().get(firstPosition);

    Episode expectedEpisode = new Episode() {
      @Override
      public Image getImage() {
        return new Image() {
          @Override
          public String getUrl() {
            return "http://example.com/episode_image.png";
          }
        };
      }
    };

    assertThat(episode.getImage().getUrl(), is(expectedEpisode.getImage().getUrl()) );
  }
}
package com.mypodcasts.repositories;

import android.content.res.Resources;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mypodcasts.R;
import com.mypodcasts.repositories.models.Audio;
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Feed;
import com.mypodcasts.repositories.models.Image;

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

public class UserPodcastsTest {

  UserPodcasts userPodcasts;
  HttpClient httpClient;

  Resources resources = mock(Resources.class);
  int firstPosition = 0;

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(1111);
  final String latest_episodes_path = "/api/user/johndoe/latest_episodes";
  final String feeds_path = "/api/user/johndoe/feeds";

  @Before
  public void setup() {
    givenThat(get(urlEqualTo(latest_episodes_path))
        .willReturn(aResponse()
            .withStatus(200)
            .withBodyFile("latest_episodes.json")));
    when(resources.getString(R.string.base_url)).thenReturn("http://localhost:1111");

    givenThat(get(urlEqualTo(feeds_path))
        .willReturn(aResponse()
            .withStatus(200)
            .withBodyFile("user_feeds.json")));
    when(resources.getString(R.string.base_url)).thenReturn("http://localhost:1111");

    httpClient = new HttpClient(resources, new RestAdapter.Builder());
    userPodcasts = new UserPodcasts(httpClient);
  }

  @Test
  public void itReturnsTitleWhenGetLatestEpisodes() {
    Episode episode = userPodcasts.getLatestEpisodes().get(firstPosition);

    Episode expectedEpisode = new Episode() {
      @Override
      public String getTitle() {
        return "Newest Episode!";
      }
    };

    assertThat(episode.getTitle(), is(expectedEpisode.getTitle()));
  }

  @Test
  public void itReturnsAudioUrlWhenGetLatestEpisodes() {
    Episode episode = userPodcasts.getLatestEpisodes().get(firstPosition);

    Episode expectedEpisode = new Episode() {
      @Override
      public Audio getAudio() {
        return new Audio() {
          @Override
          public String getUrl() {
            return "http://example.com/episode_audio.mp3";
          }
        };
      }
    };

    assertThat(episode.getAudio().getUrl(), is(expectedEpisode.getAudio().getUrl()) );
  }

  @Test
  public void itReturnsImageUrlWhenGetLatestEpisodes() {
    Episode episode = userPodcasts.getLatestEpisodes().get(firstPosition);

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

  @Test
  public void itReturnsFeedIdWhenGetFeeds() {
    Feed feed = userPodcasts.getFeeds().get(firstPosition);

    Feed expectedFeed = new Feed() {
      @Override
      public String getId() {
        return "123456";
      }
    };

    assertThat(feed.getId(), is(expectedFeed.getId()));
  }

  @Test
  public void itReturnsFeedTitleWhenGetFeeds() {
    Feed feed = userPodcasts.getFeeds().get(firstPosition);

    Feed expectedFeed = new Feed() {
      @Override
      public String getTitle() {
        return "Some podcast";
      }
    };

    assertThat(feed.getTitle(), is(expectedFeed.getTitle()));
  }

  @Test
  public void itReturnsFeedImageUrlWhenGetFeeds() {
    Feed feed = userPodcasts.getFeeds().get(firstPosition);

    Feed expectedFeed = new Feed() {
      @Override
      public Image getImage() {
        return new Image() {
          @Override
          public String getUrl() {
            return "http://example.com/feed_image.jpg";
          }
        };
      }
    };

    assertThat(feed.getImage().getUrl(), is(expectedFeed.getImage().getUrl()));
  }
}
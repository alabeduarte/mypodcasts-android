package com.mypodcasts.podcast.repositories;

import android.content.res.Resources;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mypodcasts.R;
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
            .withBodyFile("user_podcasts.json")));
    when(resources.getString(R.string.base_url)).thenReturn("http://localhost:1111");

    givenThat(get(urlEqualTo(feeds_path))
        .willReturn(aResponse()
            .withStatus(200)
            .withBodyFile("user_feeds.json")));
    when(resources.getString(R.string.base_url)).thenReturn("http://localhost:1111");

    httpClient = new HttpClient(resources, new RestAdapter.Builder());
    userPodcasts = new UserPodcasts(httpClient);
  }

  private Episode getEpisode() {
    return userPodcasts.getLatestEpisodes().get(firstPosition);
  }

  private Feed getFeed() {
    return userPodcasts.getFeeds().get(firstPosition);
  }

  @Test
  public void itReturnsTitle() {
    Episode episode = new Episode() {
      @Override
      public String getTitle() {
        return "Newest Episode!";
      }
    };

    assertThat(getEpisode().getTitle(), is(episode.getTitle()));
  }

  @Test
  public void itReturnsImageUrl() {
    Episode episode = new Episode() {
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

    assertThat(getEpisode().getImage().getUrl(), is(episode.getImage().getUrl()) );
  }

  @Test
  public void itReturnsFeedId() {
    Feed feed = new Feed() {
      @Override
      public String getId() {
        return "123456";
      }
    };

    assertThat(getFeed().getId(), is(feed.getId()));
  }

  @Test
  public void itReturnsFeedTitle() {
    Feed feed = new Feed() {
      @Override
      public String getTitle() {
        return "Some podcast";
      }
    };

    assertThat(getFeed().getTitle(), is(feed.getTitle()));
  }

  @Test
  public void itReturnsFeedImageUrl() {
    Feed firstFeed = new Feed() {
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

    assertThat(getFeed().getImage().getUrl(), is(firstFeed.getImage().getUrl()));
  }
}
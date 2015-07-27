package com.mypodcasts.podcast;

import android.content.res.Resources;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Episode;
import com.mypodcasts.podcast.models.Feed;

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

  Resources resources = mock(Resources.class);

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

    userPodcasts = new UserPodcasts(resources, new RestAdapter.Builder());
  }

  @Test
  public void itReturnsLatestEpisodes() {
    Episode newestEpisode = new Episode() {
      @Override
      public String getTitle() {
        return "Newest Episode!";
      }
    };
    Episode anotherEpisode = new Episode() {
      @Override
      public String getTitle() {
        return "Newest Episode from another podcast";
      }
    };

    assertThat(
        userPodcasts.getLatestEpisodes().get(0).getTitle(),
        is(newestEpisode.getTitle())
    );

    assertThat(
        userPodcasts.getLatestEpisodes().get(1).getTitle(),
        is(anotherEpisode.getTitle())
    );
  }

  @Test
  public void itReturnsAllFeedsId() {
    int firstPosition = 0;

    Feed firstFeed = new Feed() {
      @Override
      public String getId() {
        return "123456";
      }
    };

    assertThat(
        userPodcasts.getFeeds().get(firstPosition).getId(),
        is(firstFeed.getId())
    );
  }

  @Test
  public void itReturnsAllFeedsTitle() {
    int firstPosition = 0;

    Feed firstFeed = new Feed() {
      @Override
      public String getTitle() {
        return "Some podcast";
      }
    };

    assertThat(
        userPodcasts.getFeeds().get(firstPosition).getTitle(),
        is(firstFeed.getTitle())
    );
  }
}
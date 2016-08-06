package com.mypodcasts.repositories;

import android.content.res.Resources;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mypodcasts.R;
import com.mypodcasts.repositories.models.Episode;
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

public class UserLatestEpisodesRepositoryTest {

  UserLatestEpisodesRepository repository;
  HttpClient httpClient;

  Resources resources = mock(Resources.class);
  int firstPosition = 0;

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(1111);
  final String latestEpisodesPath = "/api/user/johndoe/latest_episodes";

  @Before
  public void setup() {
    givenThat(get(urlEqualTo(latestEpisodesPath))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody("[" +
                "  {" +
                "    \"title\": \"Newest Episode!\"," +
                "    \"publishedDate\": \"2015-09-23T15:00:00.000Z\"," +
                "    \"description\": \"Newest episode description\"," +
                "    \"image\": {" +
                "      \"url\": \"http://example.com/episode_image.png\"" +
                "    }," +
                "    \"audio\": {" +
                "      \"url\": \"http://example.com/episode_audio.mp3\"," +
                "      \"length\": \"60000000\"," +
                "      \"type\": \"audio/mpeg\"" +
                "    }," +
                "    \"podcast\":{" +
                "      \"title\": \"MyPodcasts\"," +
                "      \"image\":{" +
                "        \"url\": \"http://example.com/feed_image.jpg\"" +
                "      }" +
                "    }" +
                "  }," +
                "  {" +
                "    \"title\": \"Newest Episode from another podcast\"," +
                "    \"publishedDate\": \"2015-05-15T15:00:00.000Z\"," +
                "    \"description\": \"Another newest episode description\"," +
                "    \"image\": {" +
                "      \"url\": \"http://example.com/another_episode_image.png\"" +
                "    }," +
                "    \"enclosure\": {" +
                "      \"url\": \"http://example.com/another_newest_episode.mp3\"," +
                "      \"length\": \"52417026\"," +
                "      \"type\": \"audio/mpeg\"" +
                "    }," +
                "    \"podcast\": {" +
                "      \"title\": \"Another podcasts\"," +
                "      \"image\": {" +
                "        \"url\": \"http://example.com/another_feed_image.jpg\"" +
                "      }" +
                "    }" +
                "  }" +
                "]")
        )
    );
    when(resources.getString(R.string.base_url)).thenReturn("http://localhost:1111");

    httpClient = new HttpClient(resources, new RestAdapter.Builder());
    repository = new UserLatestEpisodesRepository(httpClient);
  }

  @Test
  public void itReturnsTitleWhenGetLatestEpisodes() {
    Episode episode = repository.getLatestEpisodes().get(firstPosition);

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
    Episode episode = repository.getLatestEpisodes().get(firstPosition);

    Episode expectedEpisode = new Episode() {
      @Override
      public String getAudioUrl() {
        return "http://example.com/episode_audio.mp3";
      }
    };

    assertThat(episode.getAudioUrl(), is(expectedEpisode.getAudioUrl()));
  }

  @Test
  public void itReturnsImageUrlWhenGetLatestEpisodes() {
    Episode episode = repository.getLatestEpisodes().get(firstPosition);

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
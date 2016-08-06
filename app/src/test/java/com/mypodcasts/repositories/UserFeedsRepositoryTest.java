package com.mypodcasts.repositories;

import android.content.res.Resources;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mypodcasts.R;
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Feed;
import com.mypodcasts.repositories.models.Image;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import retrofit.RestAdapter;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.is;
import static org.joda.time.format.ISODateTimeFormat.dateTimeParser;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserFeedsRepositoryTest {

  UserFeedsRepository repository;
  HttpClient httpClient;

  Resources resources = mock(Resources.class);

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(1111);
  final String userFeedsPath = "/api/user/johndoe/feeds";
  final String userFeedPath = "/api/user/johndoe/feeds/123456/episodes";
  final String expectedId = "123456";
  private int firstPosition = 0;

  @Before
  public void setup() {
    givenThat(get(urlEqualTo(userFeedsPath))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody("[" +
                "  {" +
                "    \"id\": \"123456\"," +
                "    \"title\": \"Some podcast\"," +
                "    \"image\": {" +
                "      \"url\": \"http://example.com/feed_image.jpg\"" +
                "    }" +
                "  }," +
                "  {" +
                "    \"id\": \"98765\"," +
                "    \"title\": \"Another podcast\"," +
                "    \"image\": {" +
                "      \"url\": \"http://example.com/another_feed_image.jpg\"" +
                "    }" +
                "  }" +
                "]")
        )
    );
    when(resources.getString(R.string.base_url)).thenReturn("http://localhost:1111");

    givenThat(get(urlEqualTo(userFeedPath))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody("{" +
                "  \"id\": \"123456\"," +
                "  \"title\": \"Some podcast\"," +
                "  \"image\":{" +
                "    \"url\": \"http://example.com/feed_image.jpg\"" +
                "  }," +
                "  \"episodes\": [" +
                "    {" +
                "      \"title\": \"Newest Episode!\"," +
                "      \"publishedDate\": \"2015-09-23T15:00:00.000Z\"," +
                "      \"description\": \"Newest episode description\"," +
                "      \"duration\": \"01:01:07\"," +
                "      \"image\": {" +
                "        \"url\": \"http://example.com/episode_image.png\"" +
                "      }," +
                "      \"audio\": {" +
                "        \"url\": \"http://example.com/newest_episode.mp3\"," +
                "        \"length\": \"60000000\"," +
                "        \"type\": \"audio/mpeg\"" +
                "      }," +
                "      \"podcast\":{" +
                "        \"title\": \"MyPodcasts\"," +
                "        \"image\":{" +
                "          \"url\": \"http://example.com/feed_image.jpg\"" +
                "        }" +
                "      }" +
                "    }," +
                "    {" +
                "      \"title\": \"Newest Episode from another podcast\"," +
                "      \"publishedDate\": \"2015-05-15T15:00:00.000Z\"," +
                "      \"description\": \"Another newest episode description\"," +
                "      \"duration\": \"01:21:38\"," +
                "      \"image\": {" +
                "        \"url\": \"http://example.com/episode_image.png\"" +
                "      }," +
                "      \"audio\": {" +
                "        \"url\": \"http://example.com/another_newest_episode.mp3\"," +
                "        \"length\": \"52417026\"," +
                "        \"type\": \"audio/mpeg\"" +
                "      }," +
                "      \"podcast\": {" +
                "        \"title\": \"Another podcasts\"," +
                "        \"image\": {" +
                "          \"url\": \"http://example.com/another_feed_image.jpg\"" +
                "        }" +
                "      }" +
                "    }" +
                "  ]" +
                "}")
        )
    );
    when(resources.getString(R.string.base_url)).thenReturn("http://localhost:1111");

    httpClient = new HttpClient(resources, new RestAdapter.Builder());
    repository = new UserFeedsRepository(httpClient);
  }

  @Test
  public void itReturnsFeedId() {
    Feed feed = repository.getFeed(expectedId);

    assertThat(feed.getId(), is(expectedId));
  }

  @Test
  public void itReturnsTitleWhenGetFeeds() {
    Feed feed = repository.getFeed(expectedId);
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
    Feed feed = repository.getFeed(expectedId);
    Episode episode = feed.getEpisodes().get(firstPosition);

    Episode expectedEpisode = new Episode() {
      @Override
      public String getAudioUrl() {
        return "http://example.com/newest_episode.mp3";
      }
    };

    assertThat(episode.getAudioUrl(), is(expectedEpisode.getAudioUrl()));
  }

  @Test
  public void itReturnsImageUrlWhenGetFeeds() {
    Feed feed = repository.getFeed(expectedId);
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

    assertThat(episode.getImage().getUrl(), is(expectedEpisode.getImage().getUrl()));
  }

  @Test
  public void itReturnsPublishedDateWhenGetFeeds() {
    Feed feed = repository.getFeed(expectedId);
    Episode episode = feed.getEpisodes().get(firstPosition);

    Episode expectedEpisode = new Episode() {
      @Override
      public Date getPublishedDate() {
        DateTime date = dateTimeParser().parseDateTime("2015-09-23T15:00:00.000Z");

        return date.toDate();
      }
    };

    assertThat(episode.getPublishedDate(), is(expectedEpisode.getPublishedDate()));
  }

  @Test
  public void itReturnsDurationWhenGetFeeds() {
    Feed feed = repository.getFeed(expectedId);
    Episode episode = feed.getEpisodes().get(firstPosition);

    Episode expectedEpisode = new Episode() {
      @Override
      public String getDuration() {
        return "01:01:07";
      }
    };

    assertThat(episode.getDuration(), is(expectedEpisode.getDuration()));
  }

  @Test
  public void itReturnsDescriptionWhenGetFeeds() {
    Feed feed = repository.getFeed(expectedId);
    Episode episode = feed.getEpisodes().get(firstPosition);

    Episode expectedEpisode = new Episode() {
      @Override
      public String getDescription() {
        return "Newest episode description";
      }
    };

    assertThat(episode.getDescription(), is(expectedEpisode.getDescription()));
  }

  @Test
  public void itReturnsFeedIdWhenGetFeeds() {
    Feed feed = repository.getFeeds().get(firstPosition);

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
    Feed feed = repository.getFeeds().get(firstPosition);

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
    Feed feed = repository.getFeeds().get(firstPosition);

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
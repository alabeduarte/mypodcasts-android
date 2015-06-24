package com.mypodcasts.rss;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.rometools.fetcher.FetcherException;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RegularEpisodeTest {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(1111);

  RegularEpisode regularEpisode;
  final String url = "http://localhost:1111/rss";

  @Before
  public void setup() throws IOException, FetcherException, FeedException {
    givenThat(get(urlEqualTo("/rss"))
        .willReturn(aResponse()
            .withStatus(200)
            .withBodyFile("rss.xml")));

    Feed feed = new Feed(new URL(url));
    SyndEntry syndEntry = feed.getEpisodes().get(0);
    regularEpisode = new RegularEpisode(syndEntry);
  }

  @Test
  public void itReturnsTitle() {
    assertThat(regularEpisode.getTitle(), is("123 – My Podcasts"));
  }

  @Test
  public void itReturnsDescription() {
    assertThat(regularEpisode.getDescription(), is("Some description"));
  }

  @Test
  public void itReturnsPublishedDate() throws ParseException {
    DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
    Date expectedDate = formatter.parse("Fri Jun 19 00:10:01 BRT 2015");

    assertThat(regularEpisode.getPublishedDate(), is(expectedDate));
  }

  @Test
  public void itReturnsAudioUrl() {
    assertThat(regularEpisode.getAudioUrl(), is("http://example.com/my_first_podcast.mp3"));
  }

  @Test
  public void itReturnsAudioLength() {
    assertThat(regularEpisode.getAudioLength(), is(52417026L));
  }
}
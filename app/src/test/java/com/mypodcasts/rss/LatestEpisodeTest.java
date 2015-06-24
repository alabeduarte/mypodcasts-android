package com.mypodcasts.rss;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.rometools.fetcher.FetcherException;
import com.rometools.rome.io.FeedException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LatestEpisodeTest {

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(1111);

  LatestEpisode latestEpisode;
  final String url = "http://localhost:1111/rss";

  @Before
  public void setup() throws IOException, FetcherException, FeedException {
    givenThat(get(urlEqualTo("/rss"))
        .willReturn(aResponse()
            .withStatus(200)
            .withBodyFile("rss.xml")));

    Feed feed = new Feed(new URL(url));
    latestEpisode = new LatestEpisode(feed);
  }

  @Test
  public void itReturnsTitle() {
    assertThat(latestEpisode.getTitle(), is("123 – My Podcasts"));
  }
}
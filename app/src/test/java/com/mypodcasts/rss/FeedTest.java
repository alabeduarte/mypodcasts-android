package com.mypodcasts.rss;

import com.rometools.fetcher.FetcherException;
import com.rometools.rome.io.FeedException;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FeedTest {
  private Feed feed;

  @Before
  public void setUp() throws IOException, FetcherException, FeedException {
    feed= new Feed(new URL("http://feed.nerdcast.com.br"));
  }

  @Test
  public void itReturnsTitle() {
    assertThat(feed.getTitle(), is("Nerdcast"));
  }
}
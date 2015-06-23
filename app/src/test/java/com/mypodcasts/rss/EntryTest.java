package com.mypodcasts.rss;

import com.rometools.fetcher.FetcherException;
import com.rometools.rome.io.FeedException;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntryTest {
  private Entry entry;

  @Before
  public void setUp() throws FetcherException, IOException, FeedException {
    Feed feed = new Feed(new URL("http://feed.nerdcast.com.br"));

    entry = new Entry(feed);
  }

  @Test
  public void itReturnsLatestEntry() {
    assertThat(entry.getTitle(), is("Nerdcast 470 – Expresso Empreendedor 5"));
  }
}
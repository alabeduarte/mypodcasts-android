package com.mypodcasts.rss;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.HashMapFeedInfoCache;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Feed {
  private final SyndFeed feed;

  public Feed(URL url) throws IOException, FetcherException, FeedException {
    FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
    FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache);

    feed = feedFetcher.retrieveFeed(url);
  }

  public String getTitle() {
    return feed.getTitle();
  }

  public List<SyndEntry> getEntries() {
    return feed.getEntries();
  }
}

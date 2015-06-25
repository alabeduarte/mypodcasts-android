package com.mypodcasts.rss;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
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
import java.util.ArrayList;
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

  public List<Episode> getEpisodes() {
    return FluentIterable
        .from(feed.getEntries())
        .transform(new Function<SyndEntry, Episode>() {
          @Override
          public Episode apply(SyndEntry entry) {
            return new RegularEpisode(entry);
          }
        })
        .toList();
  }
  public Episode getLatestEpisode() {
    return getEpisodes().get(0);
  }

  public String getImageUrl() {
    return feed.getImage().getUrl();
  }
}

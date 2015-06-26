package com.mypodcasts.podcasts;

import com.mypodcasts.rss.Feed;
import com.rometools.fetcher.FetcherException;
import com.rometools.rome.io.FeedException;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class FeedManager {
  public List<Feed> getFeeds() {
    try {
      return asList(new Feed(new URL("http://feed.nerdcast.com.br")));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (FetcherException e) {
      e.printStackTrace();
    } catch (FeedException e) {
      e.printStackTrace();
    }

    return Collections.<Feed>emptyList();
  }
}

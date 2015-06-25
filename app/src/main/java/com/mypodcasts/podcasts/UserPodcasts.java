package com.mypodcasts.podcasts;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.mypodcasts.rss.Episode;
import com.mypodcasts.rss.Feed;

import java.util.List;

import javax.inject.Inject;

public class UserPodcasts {

  private final FeedManager feedManager;

  @Inject
  public UserPodcasts(FeedManager feedManager) {
    this.feedManager = feedManager;
  }

  public List<Episode> getLatestEpisodes() {
    List<Feed> feeds = feedManager.getFeeds();
    
    return FluentIterable.from(feeds).transform(new Function<Feed, Episode>() {
      @Override
      public Episode apply(Feed feed) {
        return feed.getLatestEpisode();
      }
    }).toList();
  }
}

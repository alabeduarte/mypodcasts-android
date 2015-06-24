package com.mypodcasts.rss;

public class LatestEpisode {
  private final Feed feed;
  private final Integer POSITION = 0;

  public LatestEpisode(Feed feed) {
    this.feed = feed;
  }

  public String getTitle() {
    return feed.getEpisodes().get(POSITION).getTitle();
  }
}

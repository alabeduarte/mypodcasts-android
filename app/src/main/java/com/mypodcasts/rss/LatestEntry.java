package com.mypodcasts.rss;

public class LatestEntry {
  private final Feed feed;
  private final Integer POSITION = 0;

  public LatestEntry(Feed feed) {
    this.feed = feed;
  }

  public String getTitle() {
    return feed.getEntries().get(POSITION).getTitle();
  }
}

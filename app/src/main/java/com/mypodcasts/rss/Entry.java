package com.mypodcasts.rss;

public class Entry {
  private Feed feed;

  public Entry(Feed feed) {
    this.feed = feed;
  }

  public String getTitle() {
    return feed.getEntries().get(0).getTitle();
  }
}

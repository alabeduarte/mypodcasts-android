package com.mypodcasts.rss;

import java.util.Date;

public class LatestEpisode implements Episode {
  private final Integer POSITION = 0;
  private final Episode latestEpisode;

  public LatestEpisode(Feed feed) {
    this.latestEpisode = new RegularEpisode(feed.getEpisodes().get(POSITION));
  }

  @Override
  public String getTitle() {
    return latestEpisode.getTitle();
  }

  @Override
  public String getDescription() {
    return latestEpisode.getDescription();
  }

  @Override
  public Date getPublishedDate() {
    return latestEpisode.getPublishedDate();
  }

  public String getAudioUrl() {
    return latestEpisode.getAudioUrl();
  }

  public Long getAudioLength() {
    return latestEpisode.getAudioLength();
  }
}

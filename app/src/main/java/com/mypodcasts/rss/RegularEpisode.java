package com.mypodcasts.rss;

import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;

import java.util.Date;

public class RegularEpisode implements Episode {

  private final SyndEntry entry;
  private final SyndEnclosure audio;

  public RegularEpisode(SyndEntry entry) {
    this.entry = entry;
    audio = entry.getEnclosures().get(0);
  }

  @Override
  public String getTitle() {
    return entry.getTitle();
  }

  @Override
  public String getDescription() {
    return entry.getDescription().getValue().trim();
  }

  @Override
  public Date getPublishedDate() {
    return entry.getPublishedDate();
  }

  @Override
  public String getAudioUrl() {
    return audio.getUrl();
  }

  @Override
  public Long getAudioLength() {
    return audio.getLength();
  }
}

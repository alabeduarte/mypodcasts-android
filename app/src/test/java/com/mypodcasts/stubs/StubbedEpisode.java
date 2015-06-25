package com.mypodcasts.stubs;

import com.mypodcasts.rss.Episode;

import java.util.Date;

public class StubbedEpisode implements Episode {

  @Override
  public String getTitle() {
    return null;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public Date getPublishedDate() {
    return null;
  }

  @Override
  public String getAudioUrl() {
    return null;
  }

  @Override
  public Long getAudioLength() {
    return null;
  }
}

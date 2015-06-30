package com.mypodcasts.podcast.models;

import java.io.Serializable;

public class Episode implements Serializable {

  private String title;
  private String publishedDate;
  private String description;
  private Audio audio;
  private Podcast podcast;

  public String getTitle() {
    return title;
  }

  public String getPublishedDate() {
    return publishedDate;
  }

  public String getDescription() {
    return description;
  }

  public Audio getAudio() {
    return audio;
  }

  public Podcast getPodcast() {
    return podcast;
  }
}

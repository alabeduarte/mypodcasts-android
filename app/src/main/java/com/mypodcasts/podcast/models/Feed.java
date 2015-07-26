package com.mypodcasts.podcast.models;

import java.io.Serializable;
import java.util.List;

public class Feed implements Serializable {
  private String id;
  private String title;
  private List<Episode> episodes;

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public List<Episode> getEpisodes() {
    return episodes;
  }

}

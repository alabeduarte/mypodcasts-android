package com.mypodcasts.podcast;

import com.mypodcasts.podcast.models.Episode;

import java.io.Serializable;
import java.util.List;

public class EpisodeList implements Serializable {
  public static final String TITLE = "EpisodeList#title";
  public static final String LIST = "EpisodeList#list";

  private final List<Episode> episodes;

  public EpisodeList(List<Episode> episodes) {
    this.episodes = episodes;
  }

  public List<Episode> getEpisodes() {
    return episodes;
  }
}

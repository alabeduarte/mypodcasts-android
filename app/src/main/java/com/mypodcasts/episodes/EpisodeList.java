package com.mypodcasts.episodes;

import com.mypodcasts.repositories.models.Episode;

import java.io.Serializable;
import java.util.List;

public class EpisodeList implements Serializable {
  public static final String HEADER = "EpisodeList#header";
  public static final String LIST = "EpisodeList#list";

  private final List<Episode> episodes;

  public EpisodeList(List<Episode> episodes) {
    this.episodes = episodes;
  }

  public List<Episode> getEpisodes() {
    return episodes;
  }
}

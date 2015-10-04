package com.mypodcasts.episodes;

import com.mypodcasts.repositories.models.Image;

import java.io.Serializable;

public class EpisodeListHeaderInfo implements Serializable {
  private String title;
  private String imageUrl;

  public EpisodeListHeaderInfo(String title, String imageUrl) {
    this.title = title;
    this.imageUrl = imageUrl;
  }

  public EpisodeListHeaderInfo(String title, Image image) {
    this.title = title;
    this.imageUrl = image.getUrl();
  }

  public EpisodeListHeaderInfo(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}

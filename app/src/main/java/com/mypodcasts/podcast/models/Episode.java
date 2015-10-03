package com.mypodcasts.podcast.models;

import java.io.Serializable;

public class Episode implements Serializable {

  private String title;
  private String publishedDate;
  private String description;
  private Audio audio;
  private Podcast podcast;
  private Image image;

  public Image getImage() {
    return image;
  }

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
    if (audio == null) {
      return new Audio() {
        @Override
        public String getUrl() {
          return "";
        }
      };
    } else {
      return audio;
    }
  }

  public Podcast getPodcast() {
    if (podcast == null) {
      return new Podcast();
    } else {
      return podcast;
    }
  }

  public String getAudioFilePath() {
    return getPodcast().getId() + "/" + getTitle() + ".mp3";
  }
}

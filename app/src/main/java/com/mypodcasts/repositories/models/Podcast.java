package com.mypodcasts.repositories.models;

import java.io.Serializable;

public class Podcast implements Serializable {
  private String id;
  private String title;
  private Image image;

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public Image getImage() {
    return image;
  }
}

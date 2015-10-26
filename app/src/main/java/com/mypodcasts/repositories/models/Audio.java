package com.mypodcasts.repositories.models;

import java.io.Serializable;

public class Audio implements Serializable {
  private String url;
  private Long length;
  private String type;

  public String getUrl() {
    return url;
  }

  public Long getLength() {
    return length;
  }

  public String getType() {
    return type;
  }
}

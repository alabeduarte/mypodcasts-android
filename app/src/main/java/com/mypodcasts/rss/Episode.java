package com.mypodcasts.rss;

import java.util.Date;

public interface Episode {

  String getTitle();

  String getDescription();

  Date getPublishedDate();

  String getAudioUrl();

  Long getAudioLength();
}

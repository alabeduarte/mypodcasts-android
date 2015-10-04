package com.mypodcasts.repositories;

import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Feed;

import java.util.List;

import javax.inject.Inject;

public class UserPodcasts {

  private final HttpClient httpClient;

  @Inject
  public UserPodcasts(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public List<Episode> getLatestEpisodes() {
    return httpClient.getApi().getLatestEpisodes();
  }

  public List<Feed> getFeeds() {
    return httpClient.getApi().getUserFeeds();
  }
}

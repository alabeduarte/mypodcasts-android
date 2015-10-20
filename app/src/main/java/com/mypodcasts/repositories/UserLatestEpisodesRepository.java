package com.mypodcasts.repositories;

import com.mypodcasts.repositories.models.Episode;

import java.util.List;

import javax.inject.Inject;

public class UserLatestEpisodesRepository {

  private final HttpClient httpClient;

  @Inject
  public UserLatestEpisodesRepository(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public List<Episode> getLatestEpisodes() {
    return httpClient.getApi().getLatestEpisodes();
  }
}

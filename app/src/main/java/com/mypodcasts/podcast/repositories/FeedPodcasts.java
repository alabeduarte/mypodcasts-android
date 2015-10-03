package com.mypodcasts.podcast.repositories;

import com.mypodcasts.podcast.models.Feed;

import javax.inject.Inject;

public class FeedPodcasts {

  private final HttpClient httpClient;

  @Inject
  public FeedPodcasts(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public Feed getFeed(String id) {
    return httpClient.getApi().getFeed(id);
  }
}

package com.mypodcasts.repositories;

import com.mypodcasts.repositories.models.Feed;

import java.util.List;

import javax.inject.Inject;

public class UserFeedsRepository {

  private final HttpClient httpClient;

  @Inject
  public UserFeedsRepository(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public List<Feed> getFeeds() {
    return httpClient.getApi().getUserFeeds();
  }

  public Feed getFeed(String id) {
    return httpClient.getApi().getFeed(id);
  }
}
package com.mypodcasts.podcast.repositories;

import android.content.res.Resources;

import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Feed;

import javax.inject.Inject;

import retrofit.RestAdapter;

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

package com.mypodcasts.podcast.repositories;

import android.content.res.Resources;

import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Feed;

import javax.inject.Inject;

import retrofit.RestAdapter;

public class FeedPodcasts {

  private final Resources resources;
  private final RestAdapter.Builder restAdapterBuilder;

  @Inject
  public FeedPodcasts(Resources resources, RestAdapter.Builder restAdapterBuilder) {
    this.resources = resources;
    this.restAdapterBuilder = restAdapterBuilder;
  }

  public Feed getFeed(String id) {
    return getHttpClient().getFeed(id);
  }

  private HttpClient getHttpClient() {
    String endPoint = resources.getString(R.string.base_url);
    return restAdapterBuilder
        .setEndpoint(endPoint)
        .build()
        .create(HttpClient.class);
  }
}

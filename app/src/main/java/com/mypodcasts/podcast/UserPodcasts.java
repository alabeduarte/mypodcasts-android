package com.mypodcasts.podcast;

import com.mypodcasts.podcast.models.Episode;

import java.util.List;

import javax.inject.Inject;

import retrofit.RestAdapter;

public class UserPodcasts {

  private RestAdapter.Builder restAdapterBuilder;

  @Inject
  public UserPodcasts(RestAdapter.Builder restAdapterBuilder) {
    this.restAdapterBuilder = restAdapterBuilder;
  }

  public List<Episode> getLatestEpisodes() {
    return getHttpClient().getLatestEpisodes();
  }

  private HttpClient getHttpClient() {
    String endPoint = "http://10.0.2.2:1111";
    return restAdapterBuilder
        .setEndpoint(endPoint)
        .build()
        .create(HttpClient.class);
  }
}

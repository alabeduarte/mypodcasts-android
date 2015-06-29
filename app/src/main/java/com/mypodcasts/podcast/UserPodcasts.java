package com.mypodcasts.podcast;

import com.mypodcasts.podcast.models.Episode;

import java.util.List;

import retrofit.RestAdapter;

public class UserPodcasts {

  public List<Episode> getLatestEpisodes() {
    return getHttpClient().getLatestEpisodes();
  }

  private HttpClient getHttpClient() {
    String endPoint = "http://localhost:1111";
    return new RestAdapter
        .Builder()
        .setEndpoint(endPoint)
        .build()
        .create(HttpClient.class);
  }
}

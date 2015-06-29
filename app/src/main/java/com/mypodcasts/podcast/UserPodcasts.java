package com.mypodcasts.podcast;

import android.content.res.Resources;

import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Episode;

import java.util.List;

import javax.inject.Inject;

import retrofit.RestAdapter;

public class UserPodcasts {

  private final Resources resources;
  private final RestAdapter.Builder restAdapterBuilder;

  @Inject
  public UserPodcasts(Resources resources, RestAdapter.Builder restAdapterBuilder) {
    this.resources = resources;
    this.restAdapterBuilder = restAdapterBuilder;
  }

  public List<Episode> getLatestEpisodes() {
    return getHttpClient().getLatestEpisodes();
  }

  private HttpClient getHttpClient() {
    String endPoint = resources.getString(R.string.base_url);
    return restAdapterBuilder
        .setEndpoint(endPoint)
        .build()
        .create(HttpClient.class);
  }
}

package com.mypodcasts.repositories;

import android.content.res.Resources;

import com.mypodcasts.R;

import javax.inject.Inject;

import retrofit.RestAdapter;

public class HttpClient {

  private final Resources resources;
  private final RestAdapter.Builder restAdapterBuilder;

  @Inject
  public HttpClient(Resources resources, RestAdapter.Builder restAdapterBuilder) {
    this.resources = resources;
    this.restAdapterBuilder = restAdapterBuilder;
  }

  public Api getApi() {
    String endPoint = resources.getString(R.string.base_url);
    return restAdapterBuilder
        .setEndpoint(endPoint)
        .build()
        .create(Api.class);
  }
}

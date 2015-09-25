package com.mypodcasts.support.injection;

import javax.inject.Provider;

import retrofit.RestAdapter;

public class RestAdapterBuilderProvider implements Provider<RestAdapter.Builder> {
  @Override
  public RestAdapter.Builder get() {
    return new RestAdapter.Builder();
  }
}

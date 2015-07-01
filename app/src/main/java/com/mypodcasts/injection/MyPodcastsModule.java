package com.mypodcasts.injection;

import android.app.ProgressDialog;

import com.google.inject.Binder;
import com.google.inject.Module;

import retrofit.RestAdapter;

public class MyPodcastsModule implements Module {

  @Override
  public void configure(Binder binder) {
    binder.bind(RestAdapter.Builder.class)
        .toProvider(RestAdapterBuilderProvider.class);
    binder.bind(ProgressDialog.class)
        .toProvider(ProgressDialogProvider.class);
  }
}

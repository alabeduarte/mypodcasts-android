package com.mypodcasts.injection;

import android.app.Notification;
import android.app.ProgressDialog;
import android.widget.MediaController;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

public class MyPodcastsModule implements Module {

  @Override
  public void configure(Binder binder) {
    binder.bind(RestAdapter.Builder.class)
        .toProvider(RestAdapterBuilderProvider.class);
    binder.bind(ProgressDialog.class)
        .toProvider(ProgressDialogProvider.class);
    binder.bind(Notification.Builder.class)
        .toProvider(NotificationBuilderProvider.class);
    binder.bind(EventBus.class)
        .toProvider(EventBusProvider.class);
  }
}

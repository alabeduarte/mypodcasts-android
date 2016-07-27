package com.mypodcasts.support.injection;

import android.app.Notification;
import android.app.ProgressDialog;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.mypodcasts.support.MyPodcastsImageCache;

import org.greenrobot.eventbus.EventBus;
import retrofit.RestAdapter;

public class MyPodcastsModule implements Module {

  @Override
  public void configure(Binder binder) {
    binder.bind(RestAdapter.Builder.class).toProvider(RestAdapterBuilderProvider.class);

    binder.bind(ProgressDialog.class).toProvider(ProgressDialogProvider.class);

    binder.bind(Notification.Builder.class).toProvider(NotificationBuilderProvider.class);

    binder.bind(EventBus.class).toProvider(EventBusProvider.class);

    binder.bind(ImageLoader.ImageCache.class).to(MyPodcastsImageCache.class).in(Scopes.SINGLETON);

    binder.bind(RequestQueue.class).toProvider(RequestQueueProvider.class);

    binder.bind(ImageLoader.class).toProvider(ImageLoaderProvider.class);
  }
}

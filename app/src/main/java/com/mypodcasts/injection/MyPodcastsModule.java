package com.mypodcasts.injection;

import android.app.Notification;
import android.app.ProgressDialog;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.mypodcasts.player.AudioPlayer;
import com.mypodcasts.player.AudioPlayerStreaming;
import com.mypodcasts.podcast.models.Audio;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

public class MyPodcastsModule implements Module {

  @Override
  public void configure(Binder binder) {
    binder.bind(AudioPlayer.class).to(AudioPlayerStreaming.class);

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

package com.mypodcasts.support.injection;

import android.app.Notification;
import android.content.Context;

import javax.inject.Inject;
import javax.inject.Provider;

public class NotificationBuilderProvider implements Provider<Notification.Builder> {

  @Inject
  private Context context;

  @Override
  public Notification.Builder get() {
    return new Notification.Builder(context);
  }
}

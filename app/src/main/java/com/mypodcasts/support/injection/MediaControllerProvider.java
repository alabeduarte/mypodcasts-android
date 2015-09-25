package com.mypodcasts.support.injection;

import android.content.Context;
import android.widget.MediaController;

import javax.inject.Inject;
import javax.inject.Provider;

public class MediaControllerProvider implements Provider<MediaController> {

  @Inject
  private Context context;

  @Override
  public MediaController get() {
    return new MediaController(context);
  }
}

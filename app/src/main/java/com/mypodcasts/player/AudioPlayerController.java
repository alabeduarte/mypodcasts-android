package com.mypodcasts.player;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.MediaController;

import javax.inject.Inject;

public class AudioPlayerController extends MediaController {

  @Inject
  public AudioPlayerController(Context context) {
    super(context);
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
      ((Activity) getContext()).finish();
    }

    return super.dispatchKeyEvent(event);
  }
}
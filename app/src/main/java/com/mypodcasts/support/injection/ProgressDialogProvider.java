package com.mypodcasts.support.injection;

import android.app.ProgressDialog;
import android.content.Context;

import javax.inject.Inject;
import javax.inject.Provider;

public class ProgressDialogProvider implements Provider<ProgressDialog> {

  @Inject
  private Context context;

  @Override
  public ProgressDialog get() {
    return new ProgressDialog(context);
  }
}

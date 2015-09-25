package com.mypodcasts.support.injection;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.inject.Provider;

import javax.inject.Inject;

public class RequestQueueProvider implements Provider<RequestQueue> {
  @Inject
  private Context context;

  @Override
  public RequestQueue get() {
    RequestQueue requestQueue = Volley.newRequestQueue(context);
    return requestQueue;
  }
}
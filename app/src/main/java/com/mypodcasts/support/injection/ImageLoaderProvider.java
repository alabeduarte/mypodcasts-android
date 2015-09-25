package com.mypodcasts.support.injection;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import javax.inject.Inject;
import javax.inject.Provider;

public class ImageLoaderProvider implements Provider<ImageLoader> {
  @Inject
  private ImageLoader.ImageCache imageCache;

  @Inject
  private RequestQueue requestQueue;

  @Override
  public ImageLoader get() {
    ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
    return imageLoader;
  }
}

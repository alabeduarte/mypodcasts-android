package com.mypodcasts.support;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class MyPodcastsImageCache implements ImageLoader.ImageCache {

  private LruCache<String, Bitmap> imageCache;
  private static final int ONE_KB = 1024;

  public MyPodcastsImageCache() {
    final int cacheSize = onEigthOf(maxMemoryInKB());

    imageCache = new LruCache<String, Bitmap>(cacheSize) {
      @Override
      protected int sizeOf(String imageUrl, Bitmap image) {
        return imageSizeInKB(image);
      }

      private int imageSizeInKB(Bitmap image) {
        return image.getByteCount() / ONE_KB;
      }
    };
  }

  @Override
  public Bitmap getBitmap(String imageUrl) {
    return imageCache.get(imageUrl);
  }

  @Override
  public void putBitmap(String imageUrl, Bitmap image) {
    if(imageCache.get(imageUrl) == null) {
      imageCache.put(imageUrl, image);
    }
  }

  private int onEigthOf(int value) {
    return (value / 8);
  }

  private int maxMemoryInKB() {
    return (int) (Runtime.getRuntime().maxMemory() / ONE_KB);
  }

}

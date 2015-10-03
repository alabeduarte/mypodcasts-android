package com.mypodcasts.support;

import android.app.DownloadManager;
import android.net.Uri;

import javax.inject.Inject;

public class FileDownloadManager {
  private final DownloadManager downloadManager;

  @Inject
  public FileDownloadManager(DownloadManager downloadManager) {
    this.downloadManager = downloadManager;
  }

  public long enqueue(Uri uri, String directory, String filePath) {
    DownloadManager.Request request = new DownloadManager.Request(uri);
    request.setDestinationInExternalPublicDir(directory, filePath);

    return downloadManager.enqueue(request);
  }
}

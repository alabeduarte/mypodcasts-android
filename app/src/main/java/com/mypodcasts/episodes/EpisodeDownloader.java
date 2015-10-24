package com.mypodcasts.episodes;

import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.support.FileDownloadManager;

import javax.inject.Inject;

import static android.net.Uri.parse;

public class EpisodeDownloader {
  private final FileDownloadManager fileDownloadManager;
  private final EpisodeFile episodeFile;

  @Inject
  public EpisodeDownloader(FileDownloadManager fileDownloadManager, EpisodeFile episodeFile) {
    this.fileDownloadManager = fileDownloadManager;
    this.episodeFile = episodeFile;
  }

  public void download(Episode episode) {
    fileDownloadManager.enqueue(
        parse(episode.getAudioUrl()),
        episodeFile.getPodcastsDirectory(),
        episode.getAudioFilePath()
    );
  }

  public boolean isAlreadyDownloaded(Episode episode) {
    return episodeFile.exists(episode);
  }
}

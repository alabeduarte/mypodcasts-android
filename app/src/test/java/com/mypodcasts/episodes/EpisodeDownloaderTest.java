package com.mypodcasts.episodes;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.support.FileDownloadManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static android.net.Uri.parse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeDownloaderTest {

  EpisodeDownloader episodeDownloader;

  Episode episode;

  FileDownloadManager fileDownloadManagerMock= mock(FileDownloadManager.class);
  EpisodeFile episodeFileMock = mock(EpisodeFile.class);

  @Before
  public void setUp() {
    episodeDownloader = new EpisodeDownloader(fileDownloadManagerMock, episodeFileMock);

    episode = new Episode();
  }

  @Test
  public void itDelegatesToFileDownloadManagaerWhenAskForDownload() {
    episodeDownloader.download(episode);

    verify(fileDownloadManagerMock).enqueue(
        parse(episode.getAudioUrl()),
        episodeFileMock.getPodcastsDirectory(),
        episode.getAudioFilePath()
    );
  }

  @Test
  public void itDelegatesToEpisodeFileWhenAskedAboutIfDownloadWasAlreadyMade() {
    episodeDownloader.isAlreadyDownloaded(episode);

    verify(episodeFileMock).exists(episode);
  }
}
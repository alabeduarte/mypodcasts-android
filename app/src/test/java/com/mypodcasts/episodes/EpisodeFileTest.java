package com.mypodcasts.episodes;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.support.ExternalPublicFileLookup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;

import static android.os.Environment.DIRECTORY_PODCASTS;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeFileTest {

  EpisodeFile episodeFile;

  ExternalPublicFileLookup externalPublicFileLookupMock = mock(ExternalPublicFileLookup.class);
  Episode episode;
  File externalStoragePublicDirectory;

  @Before
  public void setUp() {
    episodeFile = new EpisodeFile(externalPublicFileLookupMock);

    episode = new Episode();
    externalStoragePublicDirectory = getExternalStoragePublicDirectory(DIRECTORY_PODCASTS);
  }

  @Test
  public void itDelegatesToExternalPublicFileLookupWhenAskAboutExistingFile() {
    episodeFile.exists(episode);

    verify(externalPublicFileLookupMock).exists(
        externalStoragePublicDirectory,
        episode.getAudioFilePath()
    );
  }

  @Test
  public void itReturnsAudioFileUrlWhenFileDoesNotExists() {
    episode = new Episode() {
      @Override
      public String getAudioUrl() {
        return "audio.mp3";
      }
    };

    when(
        externalPublicFileLookupMock.exists(
            externalStoragePublicDirectory,
            episode.getAudioFilePath()
        )
    ).thenReturn(false);

    assertThat(episodeFile.getAudioFilePath(episode), is(episode.getAudioUrl()));
  }

  @Test
  public void itReturnsAudioFilePathWhenFileAlreadyExists() {
    episode = new Episode() {
      @Override
      public String getAudioFilePath() {
        return "audio.mp3";
      }
    };

    when(
        externalPublicFileLookupMock.exists(
            externalStoragePublicDirectory,
            episode.getAudioFilePath()
        )
    ).thenReturn(true);


    String filePath = getExternalStoragePublicDirectory(DIRECTORY_PODCASTS) + "/" + episode.getAudioFilePath();

    assertThat(episodeFile.getAudioFilePath(episode), is(filePath));
  }
}
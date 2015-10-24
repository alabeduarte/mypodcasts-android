package com.mypodcasts.episodes;

import android.os.Environment;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.repositories.models.Audio;
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.support.ExternalPublicFileLookup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;

import static android.os.Environment.DIRECTORY_PODCASTS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
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
    externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(DIRECTORY_PODCASTS);
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
      public Audio getAudio() {
        return new Audio() {
          @Override
          public String getUrl() {
            return "audio.mp3";
          }
        };
      }
    };

    when(
        externalPublicFileLookupMock.exists(
            externalStoragePublicDirectory,
            episode.getAudioFilePath()
        )
    ).thenReturn(false);

    assertThat(episodeFile.getAudioFilePath(episode), is(episode.getAudio().getUrl()));
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


    assertThat(episodeFile.getAudioFilePath(episode), is(episode.getAudioFilePath()));
  }
}
package com.mypodcasts.repositories.models;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class EpisodeTest {

  @Test
  public void itReturnsAudioFilePath() {
    Episode episode = new Episode() {
      @Override
      public String getTitle() {
        return "Newest episode";
      }

      @Override
      public Podcast getPodcast() {
        return new Podcast() {
          @Override
          public String getId() {
            return "123";
          }

          @Override
          public String getTitle() {
            return "Awesome podcast";
          }
        };
      }

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

    assertThat(episode.getAudioFilePath(), is(episode.getPodcast().getId() + "/" + episode.getTitle() + ".mp3"));
  }

}
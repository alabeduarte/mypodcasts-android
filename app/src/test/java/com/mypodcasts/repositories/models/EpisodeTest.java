package com.mypodcasts.repositories.models;

import org.junit.Test;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class EpisodeTest {

  @Test
  public void itReturnsEpisodeRepresentation() {
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

    String episodeRepresentation = format(
        "{ \"title\": %s, \"audioFilePath\": %s, \"podcast\": { \"id\": %s, \"title\": %s } }",
        episode.getTitle(),
        episode.getAudioFilePath(),
        episode.getPodcast().getId(),
        episode.getPodcast().getTitle()
    );

    assertThat(episode.toString(), is(episodeRepresentation));
  }

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

  @Test
  public void itReturnsAudioUrl() {
    Episode episode = new Episode() {
      @Override
      protected Audio getAudio() {
        return new Audio() {
          @Override
          public String getUrl() {
            return "audio.mp3";
          }
        };
      }
    };

    assertThat(episode.getAudioUrl(), is(episode.getAudio().getUrl()));
  }

  @Test
  public void itReturnsEmptyUrlWhenAudioIsNull() {
    Episode episode = new Episode();

    assertThat(episode.getAudioUrl(), is(""));
  }

  @Test
  public void itReturnsEmptyDescriptionWhenItIsNull() {
    Episode episode = new Episode();

    assertThat(episode.getDescription(), is(""));
  }

  @Test
  public void itReturnsNullDateByDefault() {
    assertNull(new Episode().getPublishedDate());
  }
}
package com.mypodcasts.rss;

import com.rometools.fetcher.FetcherException;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.io.FeedException;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegularEpisodeTest {
  RegularEpisode regularEpisode;

  @Before
  public void setup() throws IOException, FetcherException, FeedException {
    SyndEntry syndEntry = new SyndEntryImpl() {
      @Override
      public String getTitle() {
        return "123 – My Podcasts";
      }

      @Override
      public SyndContent getDescription() {
        SyndContent syndContent = mock(SyndContent.class);
        when(syndContent.getValue()).thenReturn("Some description");

        return syndContent;
      }

      @Override
      public Date getPublishedDate() {
        return new Date();
      }

      @Override
      public List<SyndEnclosure> getEnclosures() {
        SyndEnclosure enclosure = mock(SyndEnclosure.class);
        when(enclosure.getUrl()).thenReturn("http://example.com/my_first_podcast.mp3");
        when(enclosure.getLength()).thenReturn(52417026L);

        return asList(enclosure);
      }
    };

    regularEpisode = new RegularEpisode(syndEntry);
  }

  @Test
  public void itReturnsTitle() {
    assertThat(regularEpisode.getTitle(), is("123 – My Podcasts"));
  }

  @Test
  public void itReturnsDescription() {
    assertThat(regularEpisode.getDescription(), is("Some description"));
  }

  @Test
  public void itReturnsPublishedDate() {
    assertThat(regularEpisode.getPublishedDate(), is(new Date()));
  }

  @Test
  public void itReturnsAudioUrl() {
    assertThat(regularEpisode.getAudioUrl(), is("http://example.com/my_first_podcast.mp3"));
  }

  @Test
  public void itReturnsAudioLength() {
    assertThat(regularEpisode.getAudioLength(), is(52417026L));
  }
}
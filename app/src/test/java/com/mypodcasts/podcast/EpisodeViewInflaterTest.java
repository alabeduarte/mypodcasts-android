package com.mypodcasts.podcast;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerActivity;
import com.mypodcasts.podcast.models.Audio;
import com.mypodcasts.podcast.models.Episode;
import com.mypodcasts.podcast.models.Image;
import com.mypodcasts.podcast.models.Podcast;
import com.mypodcasts.support.FileDownloadManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static java.lang.String.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeViewInflaterTest {
  EpisodeViewInflater episodeViewInflater;

  LayoutInflater layoutInflater;
  ViewGroup parent;

  ImageLoader imageLoaderMock = mock(ImageLoader.class);
  FileDownloadManager downloadManagerMock = mock(FileDownloadManager.class);

  @Before
  public void setup() {
    Activity context = buildActivity(Activity.class).create().get();

    layoutInflater = context.getLayoutInflater();
    parent = new ViewGroup(context) {
      @Override
      protected void onLayout(boolean changed, int l, int t, int r, int b) {
      }
    };

    episodeViewInflater = new EpisodeViewInflater(
        layoutInflater, parent, imageLoaderMock, downloadManagerMock
    );
  }

  @Test
  public void itIsVisible() {
    assertThat(episodeViewInflater.inflate(new Episode()).getVisibility(), is(View.VISIBLE));
  }

  @Test
  public void itSetsEpisodeTitle() {
    Episode episode = new Episode() {
      @Override
      public String getTitle() {
        return "123 - Podcast Episode";
      }
    };

    View inflatedView = episodeViewInflater.inflate(episode);

    TextView textView = (TextView) inflatedView.findViewById(R.id.episode_title);
    String title = valueOf(textView.getText());

    assertThat(title, is("123 - Podcast Episode"));
  }

  @Test
  public void itSetsEpisodeImageUrl() {
    Episode episode = new Episode() {
      @Override
      public Image getImage() {
        return new Image() {
          @Override
          public String getUrl() {
            return "http://images.com/photo.jpeg";
          }
        };
      }
    };

    View inflatedView = episodeViewInflater.inflate(episode);

    NetworkImageView networkImageView = (NetworkImageView) inflatedView.findViewById(
        R.id.episode_thumbnail
    );

    assertThat(networkImageView.getImageURL(), is("http://images.com/photo.jpeg"));
  }

  @Test
  public void itSetsDefaultEpisodeImageUrlWhenImageIsNull() {
    Episode episode = new Episode();

    View inflatedView = episodeViewInflater.inflate(episode);

    NetworkImageView networkImageView = (NetworkImageView) inflatedView.findViewById(R.id.episode_thumbnail);

    assertThat(
        networkImageView.getImageURL(),
        is(application.getResources().getString(R.string.episode_default_image))
    );
  }

  @Test
  public void itDisablesFocusFromMediaPlayButton() {
    Episode episode = new Episode();

    View inflatedView = episodeViewInflater.inflate(episode);

    ImageButton mediaPlayButton = (ImageButton) inflatedView.findViewById(R.id.media_play_button);

    assertThat(mediaPlayButton.isFocusable(), is(false));
  }

  @Test
  public void itOpensAPlayerOnMediaPlayButtonClick() {
    Episode episode = new Episode();

    View inflatedView = episodeViewInflater.inflate(episode);
    ImageButton mediaPlayButton = (ImageButton) inflatedView.findViewById(R.id.media_play_button);

    mediaPlayButton.performClick();

    Activity context = (Activity) mediaPlayButton.getContext();
    Intent intent = shadowOf(context).peekNextStartedActivity();
    assertThat(
        intent.getComponent().getClassName(),
        is(AudioPlayerActivity.class.getCanonicalName())
    );
  }

  @Test
  public void itDoesNotOpenAPlayerWhenMediaPlayButtonIsNotClicked() {
    Episode episode = new Episode();

    View inflatedView = episodeViewInflater.inflate(episode);
    ImageButton mediaPlayButton = (ImageButton) inflatedView.findViewById(R.id.media_play_button);

    Activity context = (Activity) mediaPlayButton.getContext();
    Intent intent = shadowOf(context).peekNextStartedActivity();

    assertThat(intent, is(nullValue()));
  }

  @Test
  public void itDisablesFocusFromEpisodeDownloadButton() {
    Episode episode = new Episode();

    View inflatedView = episodeViewInflater.inflate(episode);
    ImageButton downloadButton = (ImageButton) inflatedView.findViewById(R.id.episode_download_button);

    assertThat(downloadButton.isFocusable(), is(false));
  }

  @Test
  public void itDownloadsEpisodeAudioOnDownloadButtonClick() {
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

    View inflatedView = episodeViewInflater.inflate(episode);
    ImageButton downloadButton = (ImageButton) inflatedView.findViewById(R.id.episode_download_button);

    downloadButton.performClick();

    String expectedFilePath = episode.getPodcast().getId() + "/" + episode.getTitle() + ".mp3";

    verify(downloadManagerMock).enqueue(
        Uri.parse(episode.getAudio().getUrl()),
        Environment.DIRECTORY_PODCASTS,
        expectedFilePath
    );
  }

  @Test
  public void itDoesNotDownloadEpisodeAudioWhenDownloadButtonIsNotClicked() {
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

    episodeViewInflater.inflate(episode);

    verify(downloadManagerMock, never()).enqueue((Uri) anyObject(), anyString(), anyString());
  }
}
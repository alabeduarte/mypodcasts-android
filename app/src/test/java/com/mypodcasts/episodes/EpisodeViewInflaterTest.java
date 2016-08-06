package com.mypodcasts.episodes;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
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
import com.mypodcasts.repositories.models.Audio;
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Image;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static java.lang.String.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.joda.time.DateTime.parse;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeViewInflaterTest {
  EpisodeViewInflater episodeViewInflater;

  Activity activity;
  ViewGroup parent;
  LayoutInflater spiedLayoutInflater;

  EpisodeDownloader episodeDownloaderMock = mock(EpisodeDownloader.class);

  @Before
  public void setup() {
    spiedLayoutInflater = spy(buildActivity(Activity.class).create().get().getLayoutInflater());
    activity = spy(buildActivity(Activity.class).create().get());
    when(activity.getLayoutInflater()).thenReturn(spiedLayoutInflater);

    parent = new ViewGroup(activity) {
      @Override
      protected void onLayout(boolean changed, int l, int t, int r, int b) {
      }
    };

    episodeViewInflater = new EpisodeViewInflater(activity, mock(ImageLoader.class), episodeDownloaderMock);
  }

  private View inflateView(Episode episode) {
    return episodeViewInflater.inflate(null).with(episode).from(parent);
  }

  @Test
  public void itIsVisible() {
    View inflatedView = inflateView(new Episode());

    assertThat(inflatedView.getVisibility(), is(VISIBLE));
  }

  @Test
  public void itInflatesView() {
    View view = inflateView(new Episode());

    assertNotNull(view);
  }

  @Test
  public void itDoesNotInflateViewWhenViewIsAlreadySet() {
    View view = spiedLayoutInflater.inflate(R.layout.episode_list_item, parent, false);

    episodeViewInflater.inflate(view).with(new Episode()).from(parent);
    episodeViewInflater.inflate(view).with(new Episode()).from(parent);
    episodeViewInflater.inflate(view).with(new Episode()).from(parent);

    verify(spiedLayoutInflater, times(1)).inflate(R.layout.episode_list_item, parent, false);
  }

  @Test
  public void itSetsEpisodeTitle() {
    Episode episode = new Episode() {
      @Override
      public String getTitle() {
        return "123 - Podcast Episode";
      }
    };

    View inflatedView = inflateView(episode);

    TextView textView = (TextView) inflatedView.findViewById(R.id.episode_title);
    String title = valueOf(textView.getText());

    assertThat(title, is("123 - Podcast Episode"));
  }

  @Test
  public void itSetsEpisodeDescription() {
    Episode episode = new Episode() {
      @Override
      public String getDescription() {
        return "<span>Awesome Episode</span>";
      }
    };

    View inflatedView = inflateView(episode);

    TextView textView = (TextView) inflatedView.findViewById(R.id.episode_description);
    String description = valueOf(textView.getText());

    assertThat(description, is("Awesome Episode"));
  }

  @Test
  public void itEllipsizeDescriptionWithMaxLines() {
    Episode episode = new Episode() {
      @Override
      public String getDescription() {
        return "<span>Awesome Episode</span>";
      }
    };

    View inflatedView = inflateView(episode);

    TextView textView = (TextView) inflatedView.findViewById(R.id.episode_description);

    assertThat(textView.getEllipsize(), is(TextUtils.TruncateAt.END));
    assertThat(textView.getMaxLines(), is(3));
  }

  @Test
  public void itSetsFileSizeIntoHumanReadableWay() {
    Episode episode = new Episode() {
      @Override
      protected Audio getAudio() {
        return new Audio() {
          @Override
          public String getLength() {
            return "67139026";
          }
        };
      }
    };

    View inflatedView = inflateView(episode);

    TextView textView = (TextView) inflatedView.findViewById(R.id.episode_length);
    String audioLength = valueOf(textView.getText());

    assertThat(audioLength, is("64MB"));
  }

  @Test
  public void itSetsFileSizeToEmptyWhenItsInvalid() {
    Episode episode = new Episode() {
      @Override
      protected Audio getAudio() {
        return new Audio() {
          @Override
          public String getLength() {
            return "85:12";
          }
        };
      }
    };

    View inflatedView = inflateView(episode);

    TextView textView = (TextView) inflatedView.findViewById(R.id.episode_length);
    String audioLength = valueOf(textView.getText());

    assertThat(audioLength, is(""));
  }

  @Test
  public void itSetsPublishedDate() {
    Episode episode = new Episode() {
      @Override
      public Date getPublishedDate() {
        return parse("2015-10-20", forPattern("yyyy-MM-dd")).toDate();
      }
    };

    View inflatedView = inflateView(episode);

    TextView textView = (TextView) inflatedView.findViewById(R.id.episode_published_date);
    String publishedDate = valueOf(textView.getText());

    assertThat(publishedDate, is("OCT 20, 2015"));
  }

  @Test
  public void itSetsDuration() {
    Episode episode = new Episode() {
      @Override
      public String getDuration() {
        return "01:30:00";
      }
    };

    View inflatedView = inflateView(episode);

    TextView textView = (TextView) inflatedView.findViewById(R.id.episode_duration);
    String duration = valueOf(textView.getText());

    assertThat(duration, is(episode.getDuration()));
  }

  @Test
  public void itSetsToEmptyPublishedDateWhenItIsNull() {
    Episode episode = new Episode();

    View inflatedView = inflateView(episode);

    TextView textView = (TextView) inflatedView.findViewById(R.id.episode_published_date);
    String publishedDate = valueOf(textView.getText());

    assertThat(textView.getVisibility(), is(INVISIBLE));
    assertThat(publishedDate, is(""));
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

    View inflatedView = inflateView(episode);

    NetworkImageView networkImageView = (NetworkImageView) inflatedView.findViewById(
        R.id.episode_thumbnail
    );

    assertThat(networkImageView.getImageURL(), is("http://images.com/photo.jpeg"));
  }

  @Test
  public void itSetsDefaultEpisodeImageUrlWhenImageIsNull() {
    Episode episode = new Episode();

    View inflatedView = inflateView(episode);

    NetworkImageView networkImageView = (NetworkImageView) inflatedView.findViewById(R.id.episode_thumbnail);

    assertThat(
        networkImageView.getImageURL(),
        is(application.getResources().getString(R.string.episode_default_image))
    );
  }

  @Test
  public void itDisablesFocusFromMediaPlayButton() {
    Episode episode = new Episode();

    View inflatedView = inflateView(episode);

    ImageButton mediaPlayButton = (ImageButton) inflatedView.findViewById(R.id.media_play_button);

    assertThat(mediaPlayButton.isFocusable(), is(false));
  }

  @Test
  public void itOpensAPlayerOnMediaPlayButtonClick() {
    Episode episode = new Episode();

    View inflatedView = inflateView(episode);
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

    View inflatedView = inflateView(episode);
    ImageButton mediaPlayButton = (ImageButton) inflatedView.findViewById(R.id.media_play_button);

    Activity context = (Activity) mediaPlayButton.getContext();
    Intent intent = shadowOf(context).peekNextStartedActivity();

    assertThat(intent, is(nullValue()));
  }

  @Test
  public void itDisablesFocusFromEpisodeDownloadButton() {
    Episode episode = new Episode();

    View inflatedView = inflateView(episode);
    ImageButton downloadButton = (ImageButton) inflatedView.findViewById(R.id.episode_download_button);

    assertThat(downloadButton.isFocusable(), is(false));
  }

  @Test
  public void itDownloadsEpisodeAudioOnDownloadButtonClick() {
    Episode episode = new Episode();

    View inflatedView = inflateView(episode);
    ImageButton downloadButton = (ImageButton) inflatedView
        .findViewById(R.id.episode_download_button);

    downloadButton.performClick();

    verify(episodeDownloaderMock).download(episode);
  }

  @Test
  public void itDoesNotDownloadEpisodeAudioWhenDownloadButtonIsNotClicked() {
    Episode episode = new Episode();

    inflateView(episode);

    verify(episodeDownloaderMock, never()).download(episode);
  }

  @Test
  public void itShowsDownloadButtonButtonWhenAudioFileHasNotBeenDownloaded() {
    Episode episode = new Episode();

    when(episodeDownloaderMock.isAlreadyDownloaded(episode)).thenReturn(false);

    View inflatedView = inflateView(episode);
    ViewGroup downloadButtonLayout = (ViewGroup) inflatedView
        .findViewById(R.id.episode_download_layout);

    assertThat(downloadButtonLayout.getVisibility(), is(VISIBLE));
  }

  @Test
  public void itDoesNotShowDownloadButtonButtonWhenAudioFileItsAlreadyDownloaded() {
    Episode episode = new Episode();

    when(episodeDownloaderMock.isAlreadyDownloaded(episode)).thenReturn(true);

    View inflatedView = inflateView(episode);
    ViewGroup downloadButtonLayout = (ViewGroup) inflatedView
        .findViewById(R.id.episode_download_layout);

    assertThat(downloadButtonLayout.getVisibility(), is(INVISIBLE));
  }

  @Test
  public void itShowsDownloadButtonButtonWhenAudioFileHasNotBeenDownloadedAndLayoutIsNotVisible() {
    Episode episode = new Episode();

    when(episodeDownloaderMock.isAlreadyDownloaded(episode)).thenReturn(true);

    View inflatedView = inflateView(episode);
    ViewGroup invisibleDownloadButtonLayout = (ViewGroup) inflatedView
        .findViewById(R.id.episode_download_layout);

    assertThat(invisibleDownloadButtonLayout.getVisibility(), is(INVISIBLE));

    when(episodeDownloaderMock.isAlreadyDownloaded(episode)).thenReturn(false);

    View recycledView = episodeViewInflater.inflate(inflatedView).with(episode).from(parent);
    ViewGroup visibleDownloadButtonLayout = (ViewGroup) recycledView
        .findViewById(R.id.episode_download_layout);

    assertThat(visibleDownloadButtonLayout.getVisibility(), is(VISIBLE));
  }
}
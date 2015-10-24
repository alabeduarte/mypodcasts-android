package com.mypodcasts.episodes;

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
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Image;
import com.mypodcasts.support.ExternalPublicFileLookup;
import com.mypodcasts.support.FileDownloadManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static android.os.Environment.DIRECTORY_PODCASTS;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static java.lang.String.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeViewInflaterTest {
  EpisodeViewInflater episodeViewInflater;

  Activity activity;
  ViewGroup parent;
  LayoutInflater spiedLayoutInflater;

  ImageLoader imageLoaderMock = mock(ImageLoader.class);
  FileDownloadManager downloadManagerMock = mock(FileDownloadManager.class);
  ExternalPublicFileLookup externalPublicFileLookupMock = mock(ExternalPublicFileLookup.class);

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

    episodeViewInflater = new EpisodeViewInflater(activity, imageLoaderMock, downloadManagerMock, externalPublicFileLookupMock);
  }

  private View inflateView(View view, Episode episode) {
    return episodeViewInflater.inflate(view).with(episode).from(parent);
  }

  private View inflateView(Episode episode) {
    return inflateView(null, episode);
  }

  @Test
  public void itIsVisible() {
    assertThat(inflateView(new Episode()).getVisibility(), is(VISIBLE));
  }

  @Test
  public void itInflatesView() {
    inflateView(new Episode());

    verify(spiedLayoutInflater).inflate(R.layout.episode_list_item, parent, false);
  }

  @Test
  public void itDoesNotInflateViewWhenViewIsAlreadySet() {
    View view = spiedLayoutInflater.inflate(R.layout.episode_list_item, parent, false);

    inflateView(view, new Episode());
    inflateView(view, new Episode());
    inflateView(view, new Episode());

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
    Episode episode = new Episode() {
      @Override
      public String getAudioFilePath() {
        return "audio.mp3";
      }
    };

    View inflatedView = inflateView(episode);
    ImageButton downloadButton = (ImageButton) inflatedView.findViewById(R.id.episode_download_button);

    downloadButton.performClick();

    verify(downloadManagerMock).enqueue(
        Uri.parse(episode.getAudio().getUrl()),
        Environment.DIRECTORY_PODCASTS,
        episode.getAudioFilePath()
    );
  }

  @Test
  public void itDoesNotDownloadEpisodeAudioWhenDownloadButtonIsNotClicked() {
    Episode episode = new Episode();

    inflateView(episode);

    verify(downloadManagerMock, never()).enqueue((Uri) anyObject(), anyString(), anyString());
  }

  @Test
  public void itShowsDownloadButtonButtonWhenAudioFileHasNotBeenDownloaded() {
    Episode episode = new Episode();

    when(
        externalPublicFileLookupMock.exists(
            Environment.getExternalStoragePublicDirectory(DIRECTORY_PODCASTS),
            episode.getAudioFilePath()
        )
    ).thenReturn(false);

    ViewGroup downloadButtonLayout = (ViewGroup) inflateView(episode)
        .findViewById(R.id.episode_download_layout);

    assertThat(downloadButtonLayout.getVisibility(), is(VISIBLE));
  }

  @Test
  public void itDoesNotShowDownloadButtonButtonWhenAudioFileItsAlreadyDownloaded() {
    Episode episode = new Episode();

    when(
        externalPublicFileLookupMock.exists(
            Environment.getExternalStoragePublicDirectory(DIRECTORY_PODCASTS),
            episode.getAudioFilePath()
        )
    ).thenReturn(true);

    ViewGroup downloadButtonLayout = (ViewGroup) inflateView(episode)
        .findViewById(R.id.episode_download_layout);

    assertThat(downloadButtonLayout.getVisibility(), is(INVISIBLE));
  }

  @Test
  public void itShowsDownloadButtonButtonWhenAudioFileHasNotBeenDownloadedAndLayoutIsNotVisible() {
    Episode episode = new Episode();

    when(
        externalPublicFileLookupMock.exists(
            Environment.getExternalStoragePublicDirectory(DIRECTORY_PODCASTS),
            episode.getAudioFilePath()
        )
    ).thenReturn(true);

    View inflatedView = inflateView(episode);
    ViewGroup invisibleDownloadButtonLayout = (ViewGroup) inflatedView
        .findViewById(R.id.episode_download_layout);

    assertThat(invisibleDownloadButtonLayout.getVisibility(), is(INVISIBLE));

    when(
        externalPublicFileLookupMock.exists(
            Environment.getExternalStoragePublicDirectory(DIRECTORY_PODCASTS),
            episode.getAudioFilePath()
        )
    ).thenReturn(false);

    View recycledView = episodeViewInflater.inflate(inflatedView).with(episode).from(parent);
    ViewGroup visibleDownloadButtonLayout = (ViewGroup) recycledView
        .findViewById(R.id.episode_download_layout);

    assertThat(visibleDownloadButtonLayout.getVisibility(), is(VISIBLE));
  }
}
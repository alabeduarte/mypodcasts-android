package com.mypodcasts.podcast;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerActivity;
import com.mypodcasts.podcast.models.Episode;
import com.mypodcasts.podcast.models.Image;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeListAdapterTest {

  Activity activity;
  View convertView;
  ViewGroup parent;
  ImageLoader imageLoaderMock = mock(ImageLoader.class);

  int firstPosition = 0;

  @Before
  public void setup() {
    activity = buildActivity(Activity.class).create().get();

    convertView = new View(activity);
    parent = new ViewGroup(activity) {
      @Override
      protected void onLayout(boolean changed, int l, int t, int r, int b) {
      }
    };
  }

  private EpisodeListAdapter givenAdapaterWith(List<Episode> episodes) {
    return new EpisodeListAdapter(episodes, activity.getLayoutInflater(), imageLoaderMock);
  }

  private EpisodeListAdapter givenAdapaterWithSomeEpisodes() {
    return givenAdapaterWith(givenEpisodes(new Episode(), new Episode()));
  }

  private List<Episode> givenEpisodes(Episode... episodes) {
    return asList(episodes);
  }

  private View someRowOf(EpisodeListAdapter episodeListAdapter) {
    return episodeListAdapter.getView(firstPosition, convertView, parent);
  }

  @Test
  public void itReturnsEpisodesCount() {
    List<Episode> episodes = givenEpisodes(new Episode());
    EpisodeListAdapter episodeListAdapter = givenAdapaterWith(episodes);

    assertThat(episodeListAdapter.getCount(), is(episodes.size()));
  }

  @Test
  public void itInflatesEachRow() {
    EpisodeListAdapter episodeListAdapter = givenAdapaterWithSomeEpisodes();

    View row = someRowOf(episodeListAdapter);

    assertThat(row.getVisibility(), is(View.VISIBLE));
  }

  @Test
  public void itSetsEpisodeTitle() {
    List<Episode> episodes = givenEpisodes(new Episode() {
      @Override
      public String getTitle() {
        return "123 - Podcast Episode";
      }
    });
    EpisodeListAdapter episodeListAdapter = givenAdapaterWith(episodes);

    View row = someRowOf(episodeListAdapter);
    TextView textView = (TextView) row.findViewById(R.id.episode_title);
    String title = valueOf(textView.getText());

    assertThat(title, is("123 - Podcast Episode"));
  }

  @Test
  public void itSetsEpisodeImageUrl() {
    List<Episode> episodes = givenEpisodes(new Episode() {
      @Override
      public Image getImage() {
        return new Image() {
          @Override
          public String getUrl() {
            return "http://images.com/photo.jpeg";
          }
        };
      }
    });
    EpisodeListAdapter episodeListAdapter = givenAdapaterWith(episodes);

    View row = someRowOf(episodeListAdapter);
    NetworkImageView networkImageView = (NetworkImageView) row.findViewById(R.id.episode_thumbnail);

    assertThat(networkImageView.getImageURL(), is("http://images.com/photo.jpeg"));
  }

  @Test
  public void itSetsDefaultEpisodeImageUrlWhenImageIsNull() {
    List<Episode> episodes = givenEpisodes(new Episode());
    EpisodeListAdapter episodeListAdapter = givenAdapaterWith(episodes);

    View row = someRowOf(episodeListAdapter);
    NetworkImageView networkImageView = (NetworkImageView) row.findViewById(R.id.episode_thumbnail);

    assertThat(networkImageView.getImageURL(), is(application.getResources().getString(R.string.episode_default_image)));
  }

  @Test
  public void itDisablesFocusFromMediaPlayButton() {
    EpisodeListAdapter episodeListAdapter = givenAdapaterWithSomeEpisodes();

    View row = someRowOf(episodeListAdapter);
    ImageButton mediaPlayButton = (ImageButton) row.findViewById(R.id.media_play_button);

    assertThat(mediaPlayButton.isFocusable(), is(false));
  }

  @Test
  public void itOpensAPlayerOnMediaPlayButtonClick() {
    EpisodeListAdapter episodeListAdapter = givenAdapaterWithSomeEpisodes();

    View row = someRowOf(episodeListAdapter);
    ImageButton mediaPlayButton = (ImageButton) row.findViewById(R.id.media_play_button);

    mediaPlayButton.performClick();

    Activity activity = (Activity) mediaPlayButton.getContext();
    Intent intent = shadowOf(activity).peekNextStartedActivity();
    assertThat(
        AudioPlayerActivity.class.getCanonicalName(),
        is(intent.getComponent().getClassName())
    );
  }

  @Test
  public void itDisablesFocusFromEpisodeDownloadButton() {
    EpisodeListAdapter episodeListAdapter = givenAdapaterWithSomeEpisodes();

    View row = someRowOf(episodeListAdapter);
    ImageButton downloadButton = (ImageButton) row.findViewById(R.id.episode_download_button);

    assertThat(downloadButton.isFocusable(), is(false));
  }
}
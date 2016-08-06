package com.mypodcasts.episodes;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.repositories.models.Episode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeListAdapterTest {

  Activity activity;
  ViewGroup parent;

  EpisodeViewInflater episodeViewInflater;

  int firstPosition = 0;

  @Before
  public void setup() {
    activity = buildActivity(Activity.class).create().get();

    episodeViewInflater = new EpisodeViewInflater(
        activity, mock(ImageLoader.class), mock(EpisodeDownloader.class)
    );

    parent = new ViewGroup(activity) {
      @Override
      protected void onLayout(boolean changed, int l, int t, int r, int b) {
      }
    };
  }

  @Test
  public void itReturnsEpisodesCount() {
    List<Episode> episodes = asList(new Episode());
    EpisodeListAdapter episodeListAdapter = new EpisodeListAdapter(episodes, episodeViewInflater);

    assertThat(episodeListAdapter.getCount(), is(episodes.size()));
  }

  @Test
  public void itInflatesEpisode() {
    Episode episode = new Episode() {
      @Override
      public String getTitle() {
        return "Awesome episode";
      }
    };

    List<Episode> episodes = asList(episode);

    EpisodeListAdapter episodeListAdapter = new EpisodeListAdapter(episodes, episodeViewInflater);

    View view = episodeListAdapter.getView(firstPosition, null, parent);

    assertNotNull(view);

    TextView episodeTitle = (TextView) view.findViewById(R.id.episode_title);

    assertThat(valueOf(episodeTitle.getText()), is(episode.getTitle()));
  }
}
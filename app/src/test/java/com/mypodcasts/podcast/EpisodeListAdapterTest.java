package com.mypodcasts.podcast;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.podcast.models.Episode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeListAdapterTest {

  Activity activity;
  View convertView;
  ViewGroup parent;
  EpisodeViewInflater episodeViewInflaterMock = mock(EpisodeViewInflater.class);

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

  @Test
  public void itReturnsEpisodesCount() {
    List<Episode> episodes = asList(new Episode());
    EpisodeListAdapter episodeListAdapter = new EpisodeListAdapter(episodes, episodeViewInflaterMock);

    assertThat(episodeListAdapter.getCount(), is(episodes.size()));
  }

  @Test
  public void itInflatesEpisode() {
    List<Episode> episodes = asList(new Episode());
    EpisodeListAdapter episodeListAdapter = new EpisodeListAdapter(episodes, episodeViewInflaterMock);

    episodeListAdapter.getView(firstPosition, convertView, parent);

    verify(episodeViewInflaterMock).inflate(episodes.get(firstPosition));
  }
}
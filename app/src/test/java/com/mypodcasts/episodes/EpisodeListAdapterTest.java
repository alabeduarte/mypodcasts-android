package com.mypodcasts.episodes;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.repositories.models.Episode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeListAdapterTest {

  Activity activity;
  View view;
  ViewGroup parent;
  EpisodeViewInflater episodeViewInflaterMock = mock(EpisodeViewInflater.class);
  EpisodeViewInflater.InflaterWith inflaterWithMock = mock(EpisodeViewInflater.InflaterWith.class);
  EpisodeViewInflater.InflaterFrom inflaterFromMock = mock(EpisodeViewInflater.InflaterFrom.class);

  int firstPosition = 0;

  @Before
  public void setup() {
    activity = buildActivity(Activity.class).create().get();

    view = new View(activity);

    when(episodeViewInflaterMock.inflate((View) anyObject())).thenReturn(inflaterWithMock);
    when(inflaterWithMock.with((Episode) anyObject())).thenReturn(inflaterFromMock);
    when(inflaterFromMock.from((ViewGroup) anyObject())).thenReturn(view);

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

    episodeListAdapter.getView(firstPosition, view, parent);

    verify(episodeViewInflaterMock).inflate(view);
    verify(inflaterWithMock).with(episodes.get(firstPosition));
    verify(inflaterFromMock).from(parent);
  }
}
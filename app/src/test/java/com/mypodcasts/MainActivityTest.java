package com.mypodcasts;

import android.widget.ListView;

import com.mypodcasts.podcasts.EpisodesAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

  MainActivity activity;
  ListView listView;

  @Before
  public void setup() {
    activity = buildActivity(MainActivity.class).create().get();

    listView = (ListView) activity.findViewById(R.id.episodesListView);
  }

  @Test
  public void itSetsAdapterProperlyWithEpisodes() {
    assertThat(listView.getAdapter() instanceof EpisodesAdapter, is(true));
  }
}
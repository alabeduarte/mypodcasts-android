package com.mypodcasts;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
  public void itReturnsEpisodesCount() {
    assertThat(listView.getAdapter().getCount(), is(1));
  }

  @Test
  public void itInflatesEachRow() {
    int position = 0;
    View convertView = new View(activity);
    ViewGroup parent = new ViewGroup(activity) {
      @Override
      protected void onLayout(boolean changed, int l, int t, int r, int b) {
      }
    };

    View row = listView.getAdapter().getView(position, convertView, parent);

    assertThat(row.getVisibility(), is(View.VISIBLE));
  }
}
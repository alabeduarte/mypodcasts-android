package com.mypodcasts.podcasts;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static java.lang.String.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodesAdapterTest {

  EpisodesAdapter episodesAdapter;

  Activity activity;
  View convertView;
  ViewGroup parent;

  @Before
  public void setup() {
    activity = buildActivity(Activity.class).create().get();

    convertView = new View(activity);
    parent = new ViewGroup(activity) {
      @Override
      protected void onLayout(boolean changed, int l, int t, int r, int b) {
      }
    };

    episodesAdapter = new EpisodesAdapter(activity.getLayoutInflater());
  }

  @Test
  public void itReturnsEpisodesCount() {
    assertThat(episodesAdapter.getCount(), is(1));
  }

  @Test
  public void itInflatesEachRow() {
    int position = 0;

    View row = episodesAdapter.getView(position, convertView, parent);

    assertThat(row.getVisibility(), is(View.VISIBLE));
  }

  @Test
  public void itShowsEpisodeTitle() {
    int position = 0;

    View row = episodesAdapter.getView(position, convertView, parent);
    TextView textView = (TextView) row.findViewById(R.id.episode_title);
    String title = valueOf(textView.getText());

    assertThat(title, is("123 - Podcast Episode"));
  }
}
package com.mypodcasts.podcast;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Episode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeListAdapterTest {

  EpisodeListAdapter episodeListAdapter;

  Activity activity;
  View convertView;
  ViewGroup parent;

  List<Episode> episodes;

  @Before
  public void setup() {
    activity = buildActivity(Activity.class).create().get();

    convertView = new View(activity);
    parent = new ViewGroup(activity) {
      @Override
      protected void onLayout(boolean changed, int l, int t, int r, int b) {
      }
    };

    episodes = new ArrayList<Episode>() {{
      add(new Episode() {
        @Override
        public String getTitle() {
          return "123 - Podcast Episode";
        }
      });

      add(new Episode() {
        @Override
        public String getTitle() {
          return "456 - Another Podcast Episode";
        }
      });
    }};

    episodeListAdapter = new EpisodeListAdapter(episodes, activity.getLayoutInflater());
  }

  @Test
  public void itReturnsEpisodesCount() {
    assertThat(episodeListAdapter.getCount(), is(episodes.size()));
  }

  @Test
  public void itInflatesEachRow() {
    int position = 0;

    View row = episodeListAdapter.getView(position, convertView, parent);

    assertThat(row.getVisibility(), is(View.VISIBLE));
  }

  @Test
  public void itShowsFirstEpisodeTitle() {
    int position = 0;

    View row = episodeListAdapter.getView(position, convertView, parent);
    TextView textView = (TextView) row.findViewById(R.id.episode_title);
    String title = valueOf(textView.getText());

    assertThat(title, is("123 - Podcast Episode"));
  }

  @Test
  public void itShowsSecondEpisodeTitle() {
    int position = 1;

    View row = episodeListAdapter.getView(position, convertView, parent);
    TextView textView = (TextView) row.findViewById(R.id.episode_title);
    String title = valueOf(textView.getText());

    assertThat(title, is("456 - Another Podcast Episode"));
  }
}
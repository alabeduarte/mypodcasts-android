package com.mypodcasts.feeds;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Feed;

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
public class FeedsAdapterTest {

  FeedsAdapter feedsAdapter;

  Activity activity;
  View convertView;
  ViewGroup parent;

  List<Feed> feeds;

  @Before
  public void setup() {
    activity = buildActivity(Activity.class).create().get();

    convertView = new View(activity);
    parent = new ViewGroup(activity) {
      @Override
      protected void onLayout(boolean changed, int l, int t, int r, int b) {
      }
    };

    feeds = new ArrayList<Feed>() {{
      add(aFeed("Some Feed"));
      add(aFeed("Another Feed"));
    }};

    feedsAdapter = new FeedsAdapter(feeds, activity.getLayoutInflater());
  }

  @Test
  public void itReturnsEpisodesCount() {
    assertThat(feedsAdapter.getCount(), is(feeds.size()));
  }

  @Test
  public void itInflatesEachRow() {
    int position = 0;

    View row = feedsAdapter.getView(position, convertView, parent);

    assertThat(row.getVisibility(), is(View.VISIBLE));
  }

  @Test
  public void itShowsFirstEpisodeTitle() {
    int position = 0;

    View row = feedsAdapter.getView(position, convertView, parent);
    TextView textView = (TextView) row.findViewById(R.id.feed_title);
    String title = valueOf(textView.getText());

    assertThat(title, is("Some Feed"));
  }

  @Test
  public void itShowsSecondEpisodeTitle() {
    int position = 1;

    View row = feedsAdapter.getView(position, convertView, parent);
    TextView textView = (TextView) row.findViewById(R.id.feed_title);
    String title = valueOf(textView.getText());

    assertThat(title, is("Another Feed"));
  }

  private Feed aFeed(final String title) {
    return new Feed() {
      @Override
      public String getTitle() {
        return title;
      }
    };
  }
}
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

import java.util.List;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class FeedsAdapterTest {

  Activity activity;
  View convertView;
  ViewGroup parent;

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

  private FeedsAdapter givenAdapaterWith(List<Feed> feeds) {
    return new FeedsAdapter(feeds, activity.getLayoutInflater());
  }

  private List<Feed> givenFeeds(Feed... feeds) {
    return asList(feeds);
  }

  private View someRowOf(FeedsAdapter feedsAdapter) {
    return feedsAdapter.getView(firstPosition, convertView, parent);
  }

  @Test
  public void itReturnsEpisodesCount() {
    List<Feed> feeds = givenFeeds(new Feed());
    FeedsAdapter feedsAdapter = givenAdapaterWith(feeds);

    assertThat(feedsAdapter.getCount(), is(feeds.size()));
  }

  @Test
  public void itInflatesEachRow() {
    List<Feed> feeds = givenFeeds(new Feed());
    FeedsAdapter feedsAdapter = givenAdapaterWith(feeds);

    View row = someRowOf(feedsAdapter);

    assertThat(row.getVisibility(), is(View.VISIBLE));
  }

  @Test
  public void itSetsFeedTitle() {
    List<Feed> feeds = givenFeeds(new Feed() {
      @Override
      public String getTitle() {
        return "Some Feed";
      }
    });
    FeedsAdapter feedsAdapter = givenAdapaterWith(feeds);

    View row = someRowOf(feedsAdapter);
    TextView textView = (TextView) row.findViewById(R.id.feed_title);
    String title = valueOf(textView.getText());

    assertThat(title, is("Some Feed"));
  }
}
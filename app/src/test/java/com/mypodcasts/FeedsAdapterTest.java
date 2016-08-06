package com.mypodcasts;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mypodcasts.repositories.models.Feed;

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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class FeedsAdapterTest {

  LayoutInflater spiedLayoutInflater;
  View recycledView;

  ViewGroup parent;
  int firstPosition = 0;

  @Before
  public void setup() {
    Activity context = buildActivity(Activity.class).create().get();
    spiedLayoutInflater = spy(context.getLayoutInflater());

    recycledView = null;
    parent = new ViewGroup(context) {
      @Override
      protected void onLayout(boolean changed, int l, int t, int r, int b) {
      }
    };
  }

  private FeedsAdapter givenAdapaterWith(List<Feed> feeds) {
    return new FeedsAdapter(feeds, spiedLayoutInflater);
  }

  private List<Feed> givenFeeds(Feed... feeds) {
    return asList(feeds);
  }

  private View someRowOf(FeedsAdapter feedsAdapter) {
    return feedsAdapter.getView(firstPosition, recycledView, parent);
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
  public void itInflatesView() {
    List<Feed> feeds = givenFeeds(new Feed());
    FeedsAdapter feedsAdapter = givenAdapaterWith(feeds);

    View view = feedsAdapter.getView(firstPosition, recycledView, parent);

    assertNotNull(view);
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

  @Test
  public void itDoesNotInflateViewWhenViewIsAlreadySet() {
    List<Feed> feeds = givenFeeds(new Feed());
    FeedsAdapter feedsAdapter = givenAdapaterWith(feeds);

    recycledView = spiedLayoutInflater.inflate(R.layout.feed_list_item, parent, false);

    feedsAdapter.getView(firstPosition, recycledView, parent);
    feedsAdapter.getView(firstPosition, recycledView, parent);
    feedsAdapter.getView(firstPosition, recycledView, parent);

    verify(spiedLayoutInflater, times(1)).inflate(R.layout.feed_list_item, parent, false);
  }
}
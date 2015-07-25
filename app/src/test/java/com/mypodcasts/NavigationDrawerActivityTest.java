package com.mypodcasts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.ListView;

import com.google.inject.AbstractModule;
import com.mypodcasts.feeds.episodes.FeedEpisodesActivity;
import com.mypodcasts.latestepisodes.LatestEpisodesActivity;
import com.mypodcasts.podcast.UserPodcasts;
import com.mypodcasts.podcast.models.Feed;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mypodcasts.util.ListViewHelper.performItemClickAtPosition;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;
import static roboguice.RoboGuice.Util.reset;
import static roboguice.RoboGuice.overrideApplicationInjector;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class NavigationDrawerActivityTest {

  NavigationDrawerActivity activity;
  ListView leftDrawer;

  UserPodcasts userPodcastsMock = mock(UserPodcasts.class);
  ProgressDialog progressDialogMock = mock(ProgressDialog.class);

  @Before
  public void setup() {
    overrideApplicationInjector(application, new MyTestModule());
  }

  @After
  public void teardown() {
    reset();
  }

  @Test
  public void itReturnsEmptyListWhenThereAreNoFeedsAvailable() {
    createActivityWith(Collections.<Feed>emptyList());

    assertThat(leftDrawer.getCount(), is(0));
  }

  @Test
  public void itLoadsUserFeedsOnCreate() {
    List<Feed> feeds = new ArrayList<Feed>() {{
      add(aFeed("Feed 1"));
      add(aFeed("Feed 2"));
    }};

    createActivityWith(feeds);

    Feed menuItem1 = (Feed) leftDrawer.getAdapter().getItem(0);
    Feed menuItem2 = (Feed) leftDrawer.getAdapter().getItem(1);

    assertThat(menuItem1, is(feeds.get(0)));
    assertThat(menuItem2, is(feeds.get(1)));
  }

  @Test
  public void itOpensFeedPodcastsOnItemClick() {
    createActivityWith(asList(aFeed("Some feed")));

    performItemClickAtPosition(leftDrawer, 0);

    Intent intent = shadowOf(activity).peekNextStartedActivity();
    assertThat(
        FeedEpisodesActivity.class.getCanonicalName(),
        is(intent.getComponent().getClassName())
    );
  }

  @Test
  public void itOpensFeedPodcastsOnItemClickPassingAnFeedToBeOpenned() {
    Feed someFeed = aFeed("Some feed");
    createActivityWith(asList(someFeed));

    Matcher<Serializable> serializedFeed = CoreMatchers.<Serializable>is(someFeed);

    performItemClickAtPosition(leftDrawer, 0);

    Intent intent = shadowOf(activity).peekNextStartedActivity();

    assertThat(
        intent.getSerializableExtra(Feed.class.toString()),
        serializedFeed
    );
  }


  private Feed aFeed(final String title) {
    return new Feed() {
      @Override
      public String getTitle() {
        return title;
      }
    };
  }

  private void createActivityWith(List<Feed> feeds) {
    when(userPodcastsMock.getFeeds()).thenReturn(feeds);

    activity = buildActivity(LatestEpisodesActivity.class).create().get();
    leftDrawer = (ListView) activity.findViewById(R.id.left_drawer);
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(ProgressDialog.class).toInstance(progressDialogMock);
      bind(UserPodcasts.class).toInstance(userPodcastsMock);
    }
  }
}

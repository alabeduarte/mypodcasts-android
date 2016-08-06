package com.mypodcasts;

import android.app.Activity;
import android.content.Intent;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.inject.AbstractModule;
import com.mypodcasts.episodes.EpisodeFeedsActivity;
import com.mypodcasts.repositories.UserFeedsRepository;
import com.mypodcasts.repositories.models.Feed;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mypodcasts.util.FeedHelper.aFeed;
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

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MyPodcastsActivityTest {

  UserFeedsRepository userFeedsRepositoryMock = mock(UserFeedsRepository.class);

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
    Activity activity = createActivityWith(Collections.<Feed>emptyList());

    ListView listView = (ListView) activity.findViewById(R.id.left_drawer);

    assertThat(listView.getCount(), is(0));
  }

  @Test
  public void itLoadsUserFeedsOnCreate() {
    List<Feed> feeds = new ArrayList<Feed>() {{
      add(aFeed("Feed 1"));
      add(aFeed("Feed 2"));
    }};

    MyPodcastsActivity activity = createActivityWith(feeds);
    ListView listView = (ListView) activity.findViewById(R.id.left_drawer);

    ListAdapter listAdapter = listView.getAdapter();

    Feed menuItem1 = (Feed) listAdapter.getItem(0);
    Feed menuItem2 = (Feed) listAdapter.getItem(1);

    assertThat(menuItem1, is(feeds.get(0)));
    assertThat(menuItem2, is(feeds.get(1)));
  }

  @Test
  public void itOpensFeedPodcastsOnItemClick() {
    MyPodcastsActivity activity = createActivityWith(asList(aFeed("Some feed")));
    ListView listView = (ListView) activity.findViewById(R.id.left_drawer);

    performItemClickAtPosition(listView, 0);

    Intent intent = shadowOf(activity).peekNextStartedActivity();

    assertThat(
        EpisodeFeedsActivity.class.getCanonicalName(),
        is(intent.getComponent().getClassName())
    );
  }

  @Test
  public void itOpensFeedPodcastsOnItemClickPassingAnFeedToBeOpenned() {
    Feed someFeed = aFeed("Some feed");
    MyPodcastsActivity activity = createActivityWith(asList(someFeed));
    ListView listView = (ListView) activity.findViewById(R.id.left_drawer);

    performItemClickAtPosition(listView, 0);

    Intent intent = shadowOf(activity).peekNextStartedActivity();

    assertThat(
        intent.getSerializableExtra(Feed.class.toString()),
        CoreMatchers.<Serializable>is(someFeed)
    );
  }

  private MyPodcastsActivity createActivityWith(List<Feed> feeds) {
    when(userFeedsRepositoryMock.getFeeds()).thenReturn(feeds);

    return buildActivity(MyPodcastsActivity.class).create().get();
  }
  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(UserFeedsRepository.class).toInstance(userFeedsRepositoryMock);
    }
  }
}

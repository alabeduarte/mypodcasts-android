package com.mypodcasts.latestepisodes;

import android.app.ProgressDialog;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ListView;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.podcast.UserPodcasts;
import com.mypodcasts.podcast.models.Episode;
import com.mypodcasts.podcast.models.Feed;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.RuntimeEnvironment.application;
import static roboguice.RoboGuice.Util.reset;
import static roboguice.RoboGuice.overrideApplicationInjector;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class NavigationDrawerTest {

  LatestEpisodesActivity activity;
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
      add(new Feed() {
        @Override
        public String getTitle() {
          return "Feed 1";
        }
      });
      add(new Feed() {
        @Override
        public String getTitle() {
          return "Feed 2";
        }
      });
    }};

    createActivityWith(feeds);

    String menuItem1 = (String) leftDrawer.getAdapter().getItem(0);
    String menuItem2 = (String) leftDrawer.getAdapter().getItem(1);

    assertThat(menuItem1, is(feeds.get(0).getTitle()));
    assertThat(menuItem2, is(feeds.get(1).getTitle()));
  }

  void createActivityWith(List<Feed> feeds) {
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

package com.mypodcasts;

import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.AbstractModule;
import com.mypodcasts.podcast.models.Episode;
import com.mypodcasts.podcast.UserPodcasts;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.valueOf;
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
public class MainActivityTest {

  MainActivity activity;
  ListView listView;

  UserPodcasts userPodcastsMock = mock(UserPodcasts.class);
  List<Episode> emptyList = Collections.<Episode>emptyList();

  @Before
  public void setup() {
    overrideApplicationInjector(application, new MyTestModule());
  }

  @After
  public void teardown() {
    reset();
  }

  @Test
  public void itShowsListTitle() {
    createActivityWith(emptyList);

    TextView textView = (TextView) activity.findViewById(R.id.latest_episodes_title);
    String listTitle = valueOf(textView.getText());

    assertThat(listTitle, is("Latest Episodes"));
  }

  @Test
  public void itLoadsLatestEpisodesOnCreate() {
    createActivityWith(emptyList);

    assertThat(listView.getCount(), is(0));
  }

  @Test
  public void itLoadsLatestEpisodesWhenThereIsEpisodesOnCreate() {
   List<Episode> episodes = new ArrayList<Episode>() {{
      add(new Episode());
      add(new Episode());
    }};

    createActivityWith(episodes);

    assertThat(listView.getCount(), is(2));
  }

  void createActivityWith(List<Episode> episodes) {
    when(userPodcastsMock.getLatestEpisodes()).thenReturn(episodes);

    activity = buildActivity(MainActivity.class).create().get();
    listView = (ListView) activity.findViewById(R.id.episodesListView);
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(UserPodcasts.class).toInstance(userPodcastsMock);
    }
  }
}
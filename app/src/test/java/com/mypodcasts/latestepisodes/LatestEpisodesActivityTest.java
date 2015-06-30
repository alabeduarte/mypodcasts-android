package com.mypodcasts.latestepisodes;

import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerActivity;
import com.mypodcasts.podcast.UserPodcasts;
import com.mypodcasts.podcast.models.Episode;

import org.hamcrest.CoreMatchers;
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

import static java.lang.String.valueOf;
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
public class LatestEpisodesActivityTest {

  LatestEpisodesActivity activity;
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

  @Test
  public void itOpensAPlayerOnItemClick() {
    createActivityWith(asList(new Episode()));

    performItemClickAtPosition(0);

    Intent intent = shadowOf(activity).peekNextStartedActivity();
    assertThat(AudioPlayerActivity.class.getCanonicalName(), is(intent.getComponent().getClassName()));
  }

  @Test
  public void itOpensAPlayerOnItemClickPassingAnEpisodeToBeOpenned() {
    Episode episode = new Episode();
    createActivityWith(asList(episode));

    performItemClickAtPosition(0);

    Intent intent = shadowOf(activity).peekNextStartedActivity();
    assertThat(intent.getSerializableExtra(episode.toString()), CoreMatchers.<Serializable>is(episode));
  }

  void createActivityWith(List<Episode> episodes) {
    when(userPodcastsMock.getLatestEpisodes()).thenReturn(episodes);

    activity = buildActivity(LatestEpisodesActivity.class).create().get();
    listView = (ListView) activity.findViewById(R.id.episodesListView);
  }

  private void performItemClickAtPosition(int position) {
    listView.performItemClick(
        listView.getAdapter().getView(position, null, null),
        position,
        position
    );
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(UserPodcasts.class).toInstance(userPodcastsMock);
    }
  }
}
package com.mypodcasts.latestepisodes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerActivity;
import com.mypodcasts.podcast.EpisodeList;
import com.mypodcasts.podcast.EpisodeListFragment;
import com.mypodcasts.podcast.models.Episode;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mypodcasts.util.ListViewHelper.performItemClickAtPosition;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.util.FragmentTestUtil.startFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeListFragmentTest {

  EpisodeListFragment fragment;
  ListView listView;

  List<Episode> emptyList = Collections.<Episode>emptyList();

  @Test
  public void itShowsListTitle() {
    createFragmentWith(emptyList);

    TextView textView = (TextView) getView().findViewById(R.id.latest_episodes_title);
    String listTitle = valueOf(textView.getText());

    assertThat(listTitle, is("Latest Episodes"));
  }

  @Test
  public void itLoadsLatestEpisodesOnCreate() {
    createFragmentWith(emptyList);

    assertThat(listView.getCount(), is(0));
  }

  @Test
  public void itLoadsLatestEpisodesWhenThereAreEpisodesOnCreate() {
   List<Episode> episodes = new ArrayList<Episode>() {{
      add(anEpisode());
      add(anEpisode());
    }};

    createFragmentWith(episodes);

    assertThat(listView.getCount(), is(2));
  }

  @Test
  public void itOpensAPlayerOnItemClick() {
    createFragmentWith(asList(anEpisode()));

    performItemClickAtPosition(listView, 0);

    Intent intent = peekNextStartedActivity();
    assertThat(AudioPlayerActivity.class.getCanonicalName(), is(intent.getComponent().getClassName()));
  }

  @Test
  public void itOpensAPlayerOnItemClickPassingAnEpisodeToBeOpenned() {
    Episode episode1 = anEpisode();
    Episode episode2 = anEpisode();
    createFragmentWith(asList(episode1, episode2));

    int secondPosition = 1;
    Matcher<Serializable> serializedEpisode2 = CoreMatchers.<Serializable>is(episode2);

    performItemClickAtPosition(listView, secondPosition);

    Intent intent = peekNextStartedActivity();

    assertThat(
        intent.getSerializableExtra(Episode.class.toString()),
        serializedEpisode2
    );
  }

  void createFragmentWith(List<Episode> episodes) {
    Bundle arguments = new Bundle();
    arguments.putSerializable(
        EpisodeList.class.toString(),
        new EpisodeList(episodes)
    );

    fragment = new EpisodeListFragment();
    fragment.setArguments(arguments);

    startFragment(fragment);

    listView = (ListView) getView().findViewById(R.id.episodesListView);
  }

  private Intent peekNextStartedActivity() {
    Activity activity = fragment.getActivity();
    return shadowOf(activity).peekNextStartedActivity();
  }

  private Episode anEpisode() {
    return new Episode();
  }

  private View getView() {
    return fragment.getView();
  }
}
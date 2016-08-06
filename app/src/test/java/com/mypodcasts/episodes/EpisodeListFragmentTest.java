package com.mypodcasts.episodes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerActivity;
import com.mypodcasts.repositories.models.Episode;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.Serializable;
import java.util.List;

import roboguice.activity.RoboFragmentActivity;

import static com.mypodcasts.util.EpisodeHelper.anEpisode;
import static com.mypodcasts.util.ListViewHelper.performItemClickAtPosition;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.robolectric.Robolectric.setupActivity;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;
import static roboguice.RoboGuice.Util.reset;
import static roboguice.RoboGuice.overrideApplicationInjector;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeListFragmentTest {

  EpisodeListFragment fragment;
  ListView listView;

  List<Episode> emptyList = emptyList();

  ImageLoader imageLoaderMock = mock(ImageLoader.class);
  Activity activity;

  @Before
  public void setup() {
    overrideApplicationInjector(application, new MyTestModule());

    activity = setupActivity(RoboFragmentActivity.class);
  }

  @After
  public void teardown() {
    reset();
  }

  @Test
  public void itShowsListImage() {
    String expectedImageUrl = "http://example.com/feed.png";
    createFragmentWith(new EpisodeListHeaderInfo("Episode list title", expectedImageUrl));

    NetworkImageView networkImageView = (NetworkImageView) getView().findViewById(
        R.id.episode_list_thumbnail
    );

    assertThat(networkImageView.getImageURL(), is(expectedImageUrl));
  }

  @Test
  public void itDoesNotShowListImageWhenItIsNull() {
    String noImageUrl = null;
    createFragmentWith(new EpisodeListHeaderInfo("Episode list title", noImageUrl));

    NetworkImageView networkImageView = (NetworkImageView) getView().findViewById(
        R.id.episode_list_thumbnail
    );

    assertThat(networkImageView.getImageURL(), is(noImageUrl));
  }

  @Test
  public void itShowsListTitle() {
    String expectedTitle = "Episode list title";
    String noImageUrl = null;

    createFragmentWith(new EpisodeListHeaderInfo(expectedTitle, noImageUrl));

    TextView textView = (TextView) getView().findViewById(R.id.episodes_list_title);
    String listTitle = valueOf(textView.getText());

    assertThat(listTitle, is(expectedTitle));
  }

  @Test
  public void itLoadsEpisodesOnCreate() {
    createFragment();

    assertThat(listView.getCount(), is(0));
  }

  @Test
  public void itLoadsEpisodesWhenThereAreEpisodesOnCreate() {
    createFragmentWith(asList(anEpisode(), anEpisode()));

    assertThat(listView.getCount(), is(2));
  }

  @Test
  public void itDoesNotShowEpisodesWhenEpisodesAreNotGiven() {
    List<Episode> nullEpisodeList = null;
    createFragmentWith(nullEpisodeList);

    assertThat(listView.getCount(), is(0));
  }

  @Test
  public void itOpensAPlayerOnItemClick() {
    createFragmentWith(asList(anEpisode()));

    performItemClickAtPosition(listView, 0);

    Intent intent = peekNextStartedActivity();
    assertThat(
        AudioPlayerActivity.class.getCanonicalName(),
        is(intent.getComponent().getClassName())
    );
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

  private void createFragment() {
    createFragmentWith(emptyList);
  }

  void createFragmentWith(List<Episode> episodes) {
    createFragmentWith(new EpisodeListHeaderInfo(null), episodes);
  }

  void createFragmentWith(EpisodeListHeaderInfo headerInfo) {
    createFragmentWith(headerInfo, emptyList);
  }

  void createFragmentWith(EpisodeListHeaderInfo headerInfo, List<Episode> episodes) {
    Bundle arguments = new Bundle();
    arguments.putSerializable(EpisodeList.HEADER, headerInfo);
    arguments.putSerializable(
        EpisodeList.LIST,
        new EpisodeList(episodes)
    );

    fragment = new EpisodeListFragment();
    fragment.setArguments(arguments);

    activity.getFragmentManager().beginTransaction().add(fragment, null).commit();

    listView = (ListView) getView().findViewById(R.id.episodes_list_view);
  }

  private Intent peekNextStartedActivity() {
    Activity activity = fragment.getActivity();
    return shadowOf(activity).peekNextStartedActivity();
  }

  private View getView() {
    return fragment.getView();
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(ImageLoader.class).toInstance(imageLoaderMock);
    }
  }
}
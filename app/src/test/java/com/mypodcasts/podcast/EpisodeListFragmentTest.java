package com.mypodcasts.podcast;

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
import com.mypodcasts.podcast.EpisodeList;
import com.mypodcasts.podcast.EpisodeListFragment;
import com.mypodcasts.podcast.EpisodeListHeaderInfo;
import com.mypodcasts.podcast.models.Episode;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.Serializable;
import java.util.List;

import static com.mypodcasts.util.ListViewHelper.performItemClickAtPosition;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.util.FragmentTestUtil.startFragment;
import static roboguice.RoboGuice.Util.reset;
import static roboguice.RoboGuice.overrideApplicationInjector;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeListFragmentTest {

  EpisodeListFragment fragment;
  ListView listView;

  List<Episode> emptyList = emptyList();

  ImageLoader imageLoaderMock = mock(ImageLoader.class);

  @Before
  public void setup() {
    overrideApplicationInjector(application, new MyTestModule());
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
  public void itLoadsLatestEpisodesOnCreate() {
    createFragment();

    assertThat(listView.getCount(), is(0));
  }

  @Test
  public void itLoadsLatestEpisodesWhenThereAreEpisodesOnCreate() {
    createFragmentWith(asList(anEpisode(), anEpisode()));

    assertThat(listView.getCount(), is(2));
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

    startFragment(fragment);

    listView = (ListView) getView().findViewById(R.id.episodes_list_view);
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

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(ImageLoader.class).toInstance(imageLoaderMock);
    }
  }
}
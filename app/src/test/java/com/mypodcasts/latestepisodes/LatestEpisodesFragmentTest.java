package com.mypodcasts.latestepisodes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerActivity;
import com.mypodcasts.podcast.UserPodcasts;
import com.mypodcasts.podcast.models.Episode;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
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
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.util.FragmentTestUtil.startFragment;
import static roboguice.RoboGuice.Util.reset;
import static roboguice.RoboGuice.overrideApplicationInjector;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LatestEpisodesFragmentTest {

  LatestEpisodesFragment fragment;
  ListView listView;

  ProgressDialog progressDialogMock = mock(ProgressDialog.class);
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
  public void itShowsAndHideProgressDialog() {
    when(progressDialogMock.isShowing()).thenReturn(true);
    String message = application.getString(R.string.loading_latest_episodes);

    createFragmentWith(emptyList);

    InOrder order = inOrder(progressDialogMock);

    order.verify(progressDialogMock).show();
    order.verify(progressDialogMock).setMessage(message);

    order.verify(progressDialogMock).cancel();
  }

  @Test
  public void itDoNotCancelProgressDialogIfItIsNotShowing() {
    when(progressDialogMock.isShowing()).thenReturn(false);

    createFragmentWith(emptyList);

    InOrder order = inOrder(progressDialogMock);

    order.verify(progressDialogMock).show();
    order.verify(progressDialogMock, never()).cancel();
  }

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
      add(new Episode());
      add(new Episode());
    }};

    createFragmentWith(episodes);

    assertThat(listView.getCount(), is(2));
  }

  @Test
  public void itOpensAPlayerOnItemClick() {
    createFragmentWith(asList(new Episode()));

    performItemClickAtPosition(0);

    Intent intent = peekNextStartedActivity();
    assertThat(AudioPlayerActivity.class.getCanonicalName(), is(intent.getComponent().getClassName()));
  }

  @Test
  public void itOpensAPlayerOnItemClickPassingAnEpisodeToBeOpenned() {
    Episode episode1 = new Episode();
    Episode episode2 = new Episode();
    createFragmentWith(asList(episode1, episode2));

    int secondPosition = 1;
    Matcher<Serializable> serializedEpisode2 = CoreMatchers.<Serializable>is(episode2);

    performItemClickAtPosition(secondPosition);
    Intent intent = peekNextStartedActivity();

    assertThat(
        intent.getSerializableExtra(Episode.class.toString()),
        serializedEpisode2
    );
  }

  void createFragmentWith(List<Episode> episodes) {
    when(userPodcastsMock.getLatestEpisodes()).thenReturn(episodes);

    fragment = new LatestEpisodesFragment();
    startFragment(fragment);

    listView = (ListView) getView().findViewById(R.id.episodesListView);
  }

  private Intent peekNextStartedActivity() {
    Activity activity = fragment.getActivity();
    return shadowOf(activity).peekNextStartedActivity();
  }

  private void performItemClickAtPosition(int position) {
    listView.performItemClick(
        listView.getAdapter().getView(position, null, null),
        position,
        position
    );
  }

  private View getView() {
    return fragment.getView();
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(ProgressDialog.class).toInstance(progressDialogMock);
      bind(UserPodcasts.class).toInstance(userPodcastsMock);
    }
  }
}
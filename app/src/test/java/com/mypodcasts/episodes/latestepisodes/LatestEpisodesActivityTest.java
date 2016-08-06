package com.mypodcasts.episodes.latestepisodes;

import android.app.ProgressDialog;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.repositories.UserFeedsRepository;
import com.mypodcasts.repositories.UserLatestEpisodesRepository;
import com.mypodcasts.repositories.models.Episode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

import static java.lang.String.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.RuntimeEnvironment.application;
import static roboguice.RoboGuice.Util.reset;
import static roboguice.RoboGuice.overrideApplicationInjector;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LatestEpisodesActivityTest {

  LatestEpisodesActivity activity;

  UserFeedsRepository userFeedsMock = mock(UserFeedsRepository.class);
  UserLatestEpisodesRepository userLatestEpisodesRepositoryMock = mock(UserLatestEpisodesRepository.class);
  ProgressDialog progressDialogMock = mock(ProgressDialog.class);

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
  public void itReplacesContentFrameByLatestEpisodesFragment() {
    activity = createActivity();

    assertNotNull(activity.findViewById(R.id.content_frame));
    assertNotNull(activity.findViewById(R.id.episodes_list_title));
    assertNotNull(activity.findViewById(R.id.episodes_list_view));
  }

  @Test
  public void itSetsHeader() {
    activity = createActivity();

    TextView episodesListTitle = (TextView) activity.findViewById(R.id.episodes_list_title);

    assertThat(valueOf(episodesListTitle.getText()), is(application.getString(R.string.latest_episodes)));
  }

  @Test
  public void itSetsEpisodeList() {
    activity = createActivityWith(emptyList);

    ListView episodesListView = (ListView) activity.findViewById(R.id.episodes_list_view);

    assertNotNull(episodesListView);

    assertThat(episodesListView.getAdapter().getCount(), is(emptyList.size()));
  }

  @Test
  public void itShowsAndHideProgressDialog() {
    when(progressDialogMock.isShowing()).thenReturn(true);
    String message = application.getString(R.string.loading_latest_episodes);

    activity = createActivityWith(emptyList);

    InOrder order = inOrder(progressDialogMock);

    order.verify(progressDialogMock).show();
    order.verify(progressDialogMock).setMessage(message);

    order.verify(progressDialogMock).dismiss();
  }

  @Test
  public void itHidesProgressDialogOnDestroyToAvoidNotAttachedWindowManager() {
    when(progressDialogMock.isShowing()).thenReturn(true);

    createActivityWith(emptyList).onDestroy();

    // first hide dialog after async task, then hides again on destroy
    verify(progressDialogMock, times(2)).dismiss();
  }

  @Test
  public void itDoesNotCancelProgressDialogIfItIsNotShowing() {
    when(progressDialogMock.isShowing()).thenReturn(false);

    activity = createActivityWith(emptyList);

    InOrder order = inOrder(progressDialogMock);

    order.verify(progressDialogMock).show();
    order.verify(progressDialogMock, never()).dismiss();
  }

  LatestEpisodesActivity createActivityWith(List<Episode> episodes) {
    when(userLatestEpisodesRepositoryMock.getLatestEpisodes()).thenReturn(episodes);

    return buildActivity(LatestEpisodesActivity.class).create().get();
  }

  private LatestEpisodesActivity createActivity() {
    return createActivityWith(emptyList);
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(ProgressDialog.class).toInstance(progressDialogMock);
      bind(UserFeedsRepository.class).toInstance(userFeedsMock);
      bind(UserLatestEpisodesRepository.class).toInstance(userLatestEpisodesRepositoryMock);
    }
  }
}
package com.mypodcasts.episodes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.repositories.UserFeedsRepository;
import com.mypodcasts.repositories.UserLatestEpisodesRepository;
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Feed;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static com.mypodcasts.util.EpisodeHelper.anEpisode;
import static com.mypodcasts.util.FeedHelper.aFeed;
import static com.mypodcasts.util.FeedHelper.aFeedWith;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
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
public class EpisodeFeedsActivityTest {

  EpisodeFeedsActivity activity;

  UserLatestEpisodesRepository userLatestEpisodesRepositoryMock = mock(UserLatestEpisodesRepository.class);
  UserFeedsRepository userFeedsRepositoryMock = mock(UserFeedsRepository.class);
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
  public void itReplacesContentFrameByLatestEpisodesFragment() {
    activity = createActivity();

    assertNotNull(activity.findViewById(R.id.content_frame));
    assertNotNull(activity.findViewById(R.id.episode_list_thumbnail));
    assertNotNull(activity.findViewById(R.id.episodes_list_title));
    assertNotNull(activity.findViewById(R.id.episodes_list_view));
  }

  @Test
  public void itSetsHeader() {
    String expectedTitle = "Some title";
    String expectedImageUrl = "http://example.com/feed.png";

    activity = createActivityWith(aFeed(expectedTitle));

    TextView episodesListTitle = (TextView) activity.findViewById(R.id.episodes_list_title);
    NetworkImageView episodeListImageView = (NetworkImageView) activity.findViewById(R.id.episode_list_thumbnail);

    assertThat(valueOf(episodesListTitle.getText()), is(expectedTitle));
    assertThat(episodeListImageView.getImageURL(), is(expectedImageUrl));
  }

  @Test
  public void itSetsEpisodeList() {
    List<Episode> episodes = asList(anEpisode());
    activity = createActivityWith(aFeedWith(episodes));

    ListView episodesListView = (ListView) activity.findViewById(R.id.episodes_list_view);

    assertNotNull(episodesListView);

    assertThat(episodesListView.getAdapter().getCount(), is(episodes.size()));
    assertThat((Episode) episodesListView.getAdapter().getItem(0), is(episodes.get(0)));
  }

  @Test
  public void itShowsAndHideProgressDialog() {
    when(progressDialogMock.isShowing()).thenReturn(true);

    Feed feed = aFeed("Awesome Feed");
    activity = createActivityWith(feed);
    String message = format(
        application.getString(R.string.loading_feed_episodes), feed.getTitle()
    );

    InOrder order = inOrder(progressDialogMock);

    order.verify(progressDialogMock).show();
    order.verify(progressDialogMock).setMessage(message);

    order.verify(progressDialogMock).dismiss();
  }

  @Test
  public void itHidesProgressDialogOnDestroyToAvoidNotAttachedWindowManager() {
    when(progressDialogMock.isShowing()).thenReturn(true);

    createActivityWith(aFeed("Awesome Feed")).onDestroy();

    // first hide dialog after async task, then hides again on destroy
    verify(progressDialogMock, times(2)).dismiss();
  }

  @Test
  public void itDoNotCancelProgressDialogIfItIsNotShowing() {
    when(progressDialogMock.isShowing()).thenReturn(false);

    activity = createActivityWith(aFeed());

    InOrder order = inOrder(progressDialogMock);

    order.verify(progressDialogMock).show();
    order.verify(progressDialogMock, never()).dismiss();
  }

  private EpisodeFeedsActivity createActivityWith(Feed feed) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.putExtra(Feed.class.toString(), feed);

    when(userFeedsRepositoryMock.getFeed(feed.getId())).thenReturn(feed);

    return buildActivity(EpisodeFeedsActivity.class, intent).create().get();
  }

  private EpisodeFeedsActivity createActivity() {
    return createActivityWith(aFeed());
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(ProgressDialog.class).toInstance(progressDialogMock);
      bind(UserLatestEpisodesRepository.class).toInstance(userLatestEpisodesRepositoryMock);
      bind(UserFeedsRepository.class).toInstance(userFeedsRepositoryMock);
    }
  }
}
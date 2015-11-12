package com.mypodcasts.episodes.feeds;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.mypodcasts.MyPodcastsActivity;
import com.mypodcasts.R;
import com.mypodcasts.episodes.EpisodeList;
import com.mypodcasts.episodes.EpisodeListFragment;
import com.mypodcasts.episodes.EpisodeListHeaderInfo;
import com.mypodcasts.repositories.UserFeedsRepository;
import com.mypodcasts.repositories.models.Feed;

import javax.inject.Inject;

import retryable.asyncTask.RetryableAsyncTask;

import static java.lang.String.format;

public class FeedEpisodesActivity extends MyPodcastsActivity {

  @Inject
  private FragmentManager fragmentManager;

  @Inject
  private Bundle arguments;

  @Inject
  private EpisodeListFragment episodeListFragment;

  @Inject
  private ProgressDialog progressDialog;

  @Inject
  private UserFeedsRepository userFeedsRepository;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Feed feed = (Feed) getIntent().getSerializableExtra(Feed.class.toString());

    new FeedEpisodesAsyncTask(feed).execute();
  }

  class FeedEpisodesAsyncTask extends RetryableAsyncTask<Void, Void, Feed> {
    private final Feed feed;

    public FeedEpisodesAsyncTask(Feed feed) {
      super(FeedEpisodesActivity.this);

      this.feed = feed;
    }

    @Override
    protected void onPreExecute() {
      progressDialog.show();
      progressDialog.setMessage(format(
          getResources().getString(R.string.loading_feed_episodes), feed.getTitle()
      ));
    }

    @Override
    protected Feed doInBackground(Void... params) {
      return userFeedsRepository.getFeed(feed.getId());
    }

    @Override
    protected void onPostExecute(Feed feed) {
      if (progressDialog != null && progressDialog.isShowing()) {
        progressDialog.dismiss();
      }

      arguments.putSerializable(
          EpisodeList.HEADER,
          new EpisodeListHeaderInfo(feed.getTitle(), feed.getImage())
      );

      arguments.putSerializable(
          EpisodeList.LIST,
          new EpisodeList(feed.getEpisodes())
      );

      episodeListFragment.setArguments(arguments);

      fragmentManager.beginTransaction()
          .replace(R.id.content_frame, episodeListFragment)
          .commitAllowingStateLoss();
    }
  }
}

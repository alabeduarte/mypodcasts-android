package com.mypodcasts.episodes;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.mypodcasts.MyPodcastsActivity;
import com.mypodcasts.R;
import com.mypodcasts.repositories.UserFeedsRepository;
import com.mypodcasts.repositories.models.Feed;

import javax.inject.Inject;

import retryable.asynctask.RetryableAsyncTask;

import static java.lang.String.format;

public class EpisodeFeedsActivity extends MyPodcastsActivity {

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
  private FeedEpisodesAsyncTask feedEpisodesAsyncTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Feed feed = (Feed) getIntent().getSerializableExtra(Feed.class.toString());

    feedEpisodesAsyncTask = new FeedEpisodesAsyncTask(this, feed);

    feedEpisodesAsyncTask.execute();
  }

  @Override
  protected void onPause() {
    super.onPause();

    feedEpisodesAsyncTask.cancel();
  }

  @Override
  protected void onDestroy() {
    dismissProgressDialog();

    super.onDestroy();
  }

  private void showProgressDialog(String feedTitle) {
    progressDialog.setIndeterminate(false);
    progressDialog.setCancelable(false);
    progressDialog.show();
    progressDialog.setMessage(format(
        getResources().getString(R.string.loading_feed_episodes), feedTitle
    ));
  }

  private void dismissProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  class FeedEpisodesAsyncTask extends RetryableAsyncTask<Void, Void, Feed> {
    private final Activity activity;
    private final Feed feed;

    public FeedEpisodesAsyncTask(Activity activity, Feed feed) {
      super(activity);

      this.activity = activity;
      this.feed = feed;
    }

    @Override
    protected void onPreExecute() {
      showProgressDialog(feed.getTitle());
    }

    @Override
    protected Feed doInBackground(Void... params) {
      return userFeedsRepository.getFeed(feed.getId());
    }

    @Override
    protected void onPostExecute(Feed feed) {
      if (this.activity.isDestroyed()) return;

      dismissProgressDialog();

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

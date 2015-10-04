package com.mypodcasts.episodes.feeds;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mypodcasts.MyPodcastsActivity;
import com.mypodcasts.R;
import com.mypodcasts.episodes.EpisodeList;
import com.mypodcasts.episodes.EpisodeListFragment;
import com.mypodcasts.episodes.EpisodeListHeaderInfo;
import com.mypodcasts.repositories.models.Feed;
import com.mypodcasts.repositories.FeedPodcasts;

import javax.inject.Inject;

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
  private FeedPodcasts feedPodcasts;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Feed feed = (Feed) getIntent().getSerializableExtra(Feed.class.toString());

    new FeedEpisodesAsyncTask().execute(feed.getId());
  }

  class FeedEpisodesAsyncTask extends AsyncTask<String, Void, Feed> {
    @Override
    protected void onPreExecute() {
      progressDialog.show();
      progressDialog.setMessage(getResources().getString(R.string.loading_latest_episodes));
    }

    @Override
    protected Feed doInBackground(String... params) {
      return feedPodcasts.getFeed(params[0]);
    }

    @Override
    protected void onPostExecute(Feed feed) {
      if (progressDialog != null && progressDialog.isShowing()) {
        progressDialog.cancel();
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

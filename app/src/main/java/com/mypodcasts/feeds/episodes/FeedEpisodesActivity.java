package com.mypodcasts.feeds.episodes;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mypodcasts.NavigationDrawerActivity;
import com.mypodcasts.R;
import com.mypodcasts.podcast.EpisodeList;
import com.mypodcasts.podcast.EpisodeListFragment;
import com.mypodcasts.podcast.FeedPodcasts;
import com.mypodcasts.podcast.models.Feed;

import javax.inject.Inject;

public class FeedEpisodesActivity extends NavigationDrawerActivity {

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

      arguments.putString(EpisodeList.TITLE, feed.getTitle());
      arguments.putSerializable(
          EpisodeList.LIST,
          new EpisodeList(feed.getEpisodes())
      );

      episodeListFragment.setArguments(arguments);

      fragmentManager.beginTransaction()
          .replace(R.id.content_frame, episodeListFragment)
          .commit();
    }
  }
}

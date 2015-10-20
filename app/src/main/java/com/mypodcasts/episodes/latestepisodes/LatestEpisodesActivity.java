package com.mypodcasts.episodes.latestepisodes;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mypodcasts.MyPodcastsActivity;
import com.mypodcasts.R;
import com.mypodcasts.episodes.EpisodeList;
import com.mypodcasts.episodes.EpisodeListFragment;
import com.mypodcasts.episodes.EpisodeListHeaderInfo;
import com.mypodcasts.repositories.UserLatestEpisodesRepository;
import com.mypodcasts.repositories.models.Episode;

import java.util.List;

import javax.inject.Inject;

public class LatestEpisodesActivity extends MyPodcastsActivity {

  @Inject
  private FragmentManager fragmentManager;

  @Inject
  private Bundle arguments;

  @Inject
  private EpisodeListFragment episodeListFragment;

  @Inject
  private ProgressDialog progressDialog;

  @Inject
  private UserLatestEpisodesRepository userLatestEpisodesRepository;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    new LatestEpisodesAsyncTask().execute();
  }

  class LatestEpisodesAsyncTask extends AsyncTask<Void, Void, List<Episode>> {
    @Override
    protected void onPreExecute() {
      progressDialog.show();
      progressDialog.setMessage(getResources().getString(R.string.loading_latest_episodes));
    }

    @Override
    protected List<Episode> doInBackground(Void... params) {
      return userLatestEpisodesRepository.getLatestEpisodes();
    }

    @Override
    protected void onPostExecute(List<Episode> latestEpisodes) {
      if (progressDialog != null && progressDialog.isShowing()) {
        progressDialog.cancel();
      }

      arguments.putSerializable(
          EpisodeList.HEADER,
          new EpisodeListHeaderInfo(getResources().getString(R.string.latest_episodes))
      );
      arguments.putSerializable(
          EpisodeList.LIST,
          new EpisodeList(latestEpisodes)
      );

      episodeListFragment.setArguments(arguments);

      fragmentManager.beginTransaction()
          .replace(R.id.content_frame, episodeListFragment)
          .commitAllowingStateLoss();
    }
  }
}

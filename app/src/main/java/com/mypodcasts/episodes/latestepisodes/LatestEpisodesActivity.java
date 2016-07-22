package com.mypodcasts.episodes.latestepisodes;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
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

import retryable.asynctask.RetryableAsyncTask;

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
  private LatestEpisodesAsyncTask latestEpisodesAsyncTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    latestEpisodesAsyncTask = new LatestEpisodesAsyncTask(this);

    latestEpisodesAsyncTask.execute();
  }

  @Override
  protected void onPause() {
    super.onPause();

    latestEpisodesAsyncTask.cancel();
  }

  @Override
  protected void onDestroy() {
    dismissProgressDialog();

    super.onDestroy();
  }

  private void showProgressDialog() {
    progressDialog.setIndeterminate(false);
    progressDialog.setCancelable(false);
    progressDialog.show();
    progressDialog.setMessage(getResources().getString(R.string.loading_latest_episodes));
  }

  private void dismissProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  class LatestEpisodesAsyncTask extends RetryableAsyncTask<Void, Void, List<Episode>> {
    private final Activity activity;

    public LatestEpisodesAsyncTask(Activity activity) {
      super(activity);

      this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
      showProgressDialog();
    }

    @Override
    protected List<Episode> doInBackground(Void... params) {
      return userLatestEpisodesRepository.getLatestEpisodes();
    }

    @Override
    protected void onPostExecute(List<Episode> latestEpisodes) {
      if (this.activity.isDestroyed()) return;

      dismissProgressDialog();

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

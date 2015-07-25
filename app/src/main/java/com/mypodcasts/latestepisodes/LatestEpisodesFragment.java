package com.mypodcasts.latestepisodes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerActivity;
import com.mypodcasts.podcast.EpisodesAdapter;
import com.mypodcasts.podcast.UserPodcasts;
import com.mypodcasts.podcast.models.Episode;

import java.util.List;

import javax.inject.Inject;

import roboguice.fragment.provided.RoboFragment;
import roboguice.inject.InjectView;

public class LatestEpisodesFragment extends RoboFragment {

  @InjectView(R.id.episodesListView)
  private ListView episodesListView;

  @Inject
  private ProgressDialog progressDialog;

  @Inject
  private UserPodcasts userPodcasts;

  @Override
  public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.latest_episodes, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    episodesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Episode episode = (Episode) episodesListView.getAdapter().getItem(position);
        Intent intent = new Intent(view.getContext(), AudioPlayerActivity.class);
        intent.putExtra(Episode.class.toString(), episode);

        startActivity(intent);
      }
    });

    LayoutInflater layoutInflater = getLayoutInflater(view);
    new LatestEpisodesAsyncTask(layoutInflater).execute();
  }

  private LayoutInflater getLayoutInflater(View view) {
    return ((Activity) view.getContext()).getLayoutInflater();
  }

  class LatestEpisodesAsyncTask extends AsyncTask<Void, Void, List<Episode>> {
    private final LayoutInflater inflater;

    public LatestEpisodesAsyncTask(LayoutInflater inflater) {
      this.inflater = inflater;
    }

    @Override
    protected void onPreExecute() {
      progressDialog.show();
      progressDialog.setMessage(getResources().getString(R.string.loading_latest_episodes));
    }

    @Override
    protected List<Episode> doInBackground(Void... params) {
      return userPodcasts.getLatestEpisodes();
    }

    @Override
    protected void onPostExecute(List<Episode> latestEpisodes) {
      if (progressDialog != null && progressDialog.isShowing()) {
        progressDialog.cancel();
      }

      episodesListView.setAdapter(new EpisodesAdapter(latestEpisodes, inflater));
    }
  }
}

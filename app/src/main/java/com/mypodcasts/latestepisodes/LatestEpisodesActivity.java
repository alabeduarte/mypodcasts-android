package com.mypodcasts.latestepisodes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerActivity;
import com.mypodcasts.podcast.EpisodesAdapter;
import com.mypodcasts.podcast.UserPodcasts;
import com.mypodcasts.podcast.models.Episode;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.latest_episodes)
public class LatestEpisodesActivity extends RoboActivity {

  @InjectView(R.id.episodesListView)
  private ListView episodesListView;

  @Inject
  private ProgressDialog progressDialog;

  @Inject
  private UserPodcasts userPodcasts;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    episodesListView.setAdapter(emptyAdapter());
    episodesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Episode episode = (Episode) episodesListView.getAdapter().getItem(position);
        Intent intent = new Intent(LatestEpisodesActivity.this, AudioPlayerActivity.class);
        intent.putExtra(Episode.class.toString(), episode);

        startActivity(intent);
      }
    });

    new LatestEpisodesAsyncTask().execute();
  }

  private EpisodesAdapter emptyAdapter() {
    return new EpisodesAdapter(Collections.<Episode>emptyList(), getLayoutInflater());
  }

  class LatestEpisodesAsyncTask extends AsyncTask<Void, Void, List<Episode>> {
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
      progressDialog.cancel();
      episodesListView.setAdapter(new EpisodesAdapter(latestEpisodes, getLayoutInflater()));
    }
  }
}

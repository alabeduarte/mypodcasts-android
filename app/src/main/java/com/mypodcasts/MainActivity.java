package com.mypodcasts;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.mypodcasts.podcasts.EpisodesAdapter;
import com.mypodcasts.podcasts.UserPodcasts;
import com.mypodcasts.rss.Episode;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

  @InjectView(R.id.episodesListView)
  ListView episodesListView;

  @Inject
  UserPodcasts userPodcasts;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    episodesListView.setAdapter(new EpisodesAdapter(Collections.<Episode>emptyList(), getLayoutInflater()));

    LatestEpisodesAsyncTask asyncTask = new LatestEpisodesAsyncTask();
    List<Episode> latestEpisodes = asyncTask.doInBackground();
    episodesListView.setAdapter(new EpisodesAdapter(latestEpisodes, getLayoutInflater()));
  }

  class LatestEpisodesAsyncTask extends AsyncTask<Void, Void, List<Episode>> {

    @Override
    protected List<Episode> doInBackground(Void... params) {
      return userPodcasts.getLatestEpisodes();
    }
  }
}

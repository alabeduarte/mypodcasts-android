package com.mypodcasts.latestepisodes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerActivity;
import com.mypodcasts.podcast.EpisodesAdapter;
import com.mypodcasts.podcast.UserPodcasts;
import com.mypodcasts.podcast.models.Episode;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static com.mypodcasts.R.layout.drawer_list_item;

@ContentView(R.layout.latest_episodes)
public class LatestEpisodesActivity extends RoboActionBarActivity {

  @InjectView(R.id.left_drawer)
  private ListView leftDrawer;

  @InjectView(R.id.episodesListView)
  private ListView episodesListView;

  @Inject
  private ProgressDialog progressDialog;

  @Inject
  private UserPodcasts userPodcasts;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);

    return super.onCreateOptionsMenu(menu);
  }

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

    leftDrawer.setAdapter(navigationDrawerAdapter());

    new LatestEpisodesAsyncTask().execute();
  }

  private ArrayAdapter<String> navigationDrawerAdapter() {
    String[] menuItems = {"Menu Item 1", "Menu Item 2"};

    return new ArrayAdapter<>(this, drawer_list_item, menuItems);
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
      if (progressDialog != null && progressDialog.isShowing()) {
        progressDialog.cancel();
      }

      episodesListView.setAdapter(new EpisodesAdapter(latestEpisodes, getLayoutInflater()));
    }
  }
}

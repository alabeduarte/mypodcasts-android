package com.mypodcasts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mypodcasts.feeds.FeedsAdapter;
import com.mypodcasts.feeds.episodes.FeedEpisodesActivity;
import com.mypodcasts.podcast.models.Feed;
import com.mypodcasts.podcast.repositories.UserPodcasts;

import java.util.List;

import javax.inject.Inject;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.navigation_drawer)
public class NavigationDrawerActivity extends RoboActionBarActivity {

  @InjectView(R.id.drawer_layout)
  private DrawerLayout drawerLayout;

  @InjectView(R.id.left_drawer)
  private ListView leftDrawer;

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

    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setIcon(R.drawable.ic_drawer);

    leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Feed feed = (Feed) leftDrawer.getAdapter().getItem(position);
        Intent intent = new Intent(view.getContext(), FeedEpisodesActivity.class);
        intent.putExtra(Feed.class.toString(), feed);

        startActivity(intent);
      }
    });

    new FeedsAsyncTask().execute();
  }

  private class FeedsAsyncTask extends AsyncTask<Void, Void, List<Feed>> {

    @Override
    protected List<Feed> doInBackground(Void... params) {
      return userPodcasts.getFeeds();
    }

    @Override
    protected void onPostExecute(List<Feed> feeds) {
      leftDrawer.setAdapter(new FeedsAdapter(feeds, getLayoutInflater()));
    }
  }
}

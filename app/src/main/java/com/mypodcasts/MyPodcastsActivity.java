package com.mypodcasts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mypodcasts.episodes.EpisodeFeedsActivity;
import com.mypodcasts.repositories.UserFeedsRepository;
import com.mypodcasts.repositories.models.Feed;

import java.util.List;

import javax.inject.Inject;

import retryable.asynctask.RetryableAsyncTask;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.base_layout)
public class MyPodcastsActivity extends RoboActionBarActivity {

  @InjectView(R.id.tool_bar)
  private Toolbar toolbar;

  @InjectView(R.id.drawer_layout)
  private DrawerLayout drawerLayout;

  @InjectView(R.id.left_drawer)
  private ListView leftDrawer;

  @Inject
  private UserFeedsRepository userFeedsRepository;
  private FeedsAsyncTask feedsAsyncTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setSupportActionBar(toolbar);
    setActionBarDrawerToggle(drawerLayout, toolbar);

    leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Feed feed = (Feed) leftDrawer.getAdapter().getItem(position);
        Intent intent = new Intent(view.getContext(), EpisodeFeedsActivity.class);
        intent.putExtra(Feed.class.toString(), feed);

        startActivity(intent);
      }
    });

    feedsAsyncTask = new FeedsAsyncTask(this);

    feedsAsyncTask.execute();
  }

  @Override
  protected void onPause() {
    super.onPause();

    feedsAsyncTask.cancel();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void setActionBarDrawerToggle(DrawerLayout drawerLayout, Toolbar toolbar) {
    ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
        this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

    drawerLayout.setDrawerListener(actionBarDrawerToggle);
    actionBarDrawerToggle.syncState();
  }

  private class FeedsAsyncTask extends RetryableAsyncTask<Void, Void, List<Feed>> {
    public FeedsAsyncTask(Activity activity) {
      super(activity);
    }

    @Override
    protected List<Feed> doInBackground(Void... params) {
      return userFeedsRepository.getFeeds();
    }

    @Override
    protected void onPostExecute(List<Feed> feeds) {
      leftDrawer.setAdapter(new FeedsAdapter(feeds, getLayoutInflater()));
    }
  }
}

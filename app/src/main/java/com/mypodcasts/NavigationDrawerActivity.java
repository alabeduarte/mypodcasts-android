package com.mypodcasts;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.base.Function;
import com.mypodcasts.podcast.UserPodcasts;
import com.mypodcasts.podcast.models.Feed;

import java.util.List;

import javax.inject.Inject;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static com.google.common.collect.FluentIterable.from;
import static com.mypodcasts.R.layout.drawer_list_item;

@ContentView(R.layout.navigation_drawer)
public class NavigationDrawerActivity extends RoboActionBarActivity {

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

    new FeedsAsyncTask().execute();
  }

  private class FeedsAsyncTask extends AsyncTask<Void, Void, List<Feed>> {
    private final Context context;

    public FeedsAsyncTask() {
      context = NavigationDrawerActivity.this;
    }

    @Override
    protected List<Feed> doInBackground(Void... params) {
      return userPodcasts.getFeeds();
    }

    @Override
    protected void onPostExecute(List<Feed> feeds) {
      String[] menuItems = from(feeds).transform(new Function<Feed, String>() {
        @Override
        public String apply(Feed feed) {
          return feed.getTitle();
        }
      }).toArray(String.class);

      leftDrawer.setAdapter(new ArrayAdapter<>(context, drawer_list_item, menuItems));
    }
  }
}

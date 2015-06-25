package com.mypodcasts;

import android.os.Bundle;
import android.widget.ListView;

import com.mypodcasts.podcasts.EpisodesAdapter;
import com.mypodcasts.rss.Episode;

import java.util.ArrayList;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

  @InjectView(R.id.episodesListView)
  ListView episodesListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    episodesListView.setAdapter(new EpisodesAdapter(new ArrayList<Episode>(), getLayoutInflater()));
  }
}

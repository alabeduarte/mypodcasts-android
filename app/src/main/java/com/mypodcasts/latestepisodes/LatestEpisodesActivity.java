package com.mypodcasts.latestepisodes;

import android.app.FragmentManager;
import android.os.Bundle;

import com.mypodcasts.NavigationDrawerActivity;
import com.mypodcasts.R;

import javax.inject.Inject;

public class LatestEpisodesActivity extends NavigationDrawerActivity {

  @Inject
  private FragmentManager fragmentManager;

  @Inject
  private LatestEpisodesFragment latestEpisodesFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    fragmentManager.beginTransaction()
        .replace(R.id.content_frame, latestEpisodesFragment)
        .commit();
  }

}

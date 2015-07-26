package com.mypodcasts.podcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerActivity;
import com.mypodcasts.podcast.models.Episode;

import java.util.List;

import roboguice.fragment.provided.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeListFragment extends RoboFragment {

  @InjectView(R.id.episodes_list_title)
  private TextView episodesListTitle;

  @InjectView(R.id.episodes_list_view)
  private ListView episodesListView;

  @Override
  public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.episode_list, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    episodesListTitle.setText(getTitle());
    episodesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Episode episode = (Episode) episodesListView.getAdapter().getItem(position);
        Intent intent = new Intent(view.getContext(), AudioPlayerActivity.class);
        intent.putExtra(Episode.class.toString(), episode);

        startActivity(intent);
      }
    });

    LayoutInflater inflater = getLayoutInflater(view);

    List<Episode> latestEpisodes = getEpisodeList().getEpisodes();

    episodesListView.setAdapter(new EpisodeListAdapter(latestEpisodes, inflater));
  }

  private EpisodeList getEpisodeList() {
    return (EpisodeList) getArguments().getSerializable(EpisodeList.LIST);
  }

  private String getTitle() {
    return getArguments().getString(EpisodeList.TITLE);
  }

  private LayoutInflater getLayoutInflater(View view) {
    return ((Activity) view.getContext()).getLayoutInflater();
  }
}

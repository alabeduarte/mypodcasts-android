package com.mypodcasts.episodes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerActivity;
import com.mypodcasts.repositories.models.Episode;

import java.util.List;

import javax.inject.Inject;

import roboguice.fragment.provided.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeListFragment extends RoboFragment {

  @InjectView(R.id.episode_list_thumbnail)
  private NetworkImageView episodeListImageView;

  @InjectView(R.id.episodes_list_title)
  private TextView episodesListTitle;

  @InjectView(R.id.episodes_list_view)
  private ListView episodesListView;

  @Inject
  private ImageLoader imageLoader;

  @Inject
  private EpisodeViewInflater episodeViewInflater;

  @Override
  public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.episode_list, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    EpisodeListHeaderInfo headerInfo = getHeaderInfo();

    episodeListImageView.setImageUrl(headerInfo.getImageUrl(), imageLoader);
    episodesListTitle.setText(headerInfo.getTitle());
    episodesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Episode episode = (Episode) episodesListView.getAdapter().getItem(position);
        Intent intent = new Intent(view.getContext(), AudioPlayerActivity.class);
        intent.putExtra(Episode.class.toString(), episode);

        startActivity(intent);
      }
    });

    List<Episode> latestEpisodes = getEpisodeList().getEpisodes();
    episodesListView.setAdapter(new EpisodeListAdapter(latestEpisodes, episodeViewInflater));
  }

  private EpisodeList getEpisodeList() {
    return (EpisodeList) getArguments().getSerializable(EpisodeList.LIST);
  }

  private EpisodeListHeaderInfo getHeaderInfo() {
    return (EpisodeListHeaderInfo) getArguments().getSerializable(EpisodeList.HEADER);
  }
}

package com.mypodcasts.episodes;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mypodcasts.repositories.models.Episode;

import java.util.List;

public class EpisodeListAdapter extends BaseAdapter {

  private final List<Episode> episodes;
  private final EpisodeViewInflater episodeViewInflater;

  public EpisodeListAdapter(List<Episode> episodes, EpisodeViewInflater episodeViewInflater) {
    this.episodes = episodes;
    this.episodeViewInflater = episodeViewInflater;
  }

  @Override
  public int getCount() {
    return episodes.size();
  }

  @Override
  public Episode getItem(int position) {
    return episodes.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View recycledView, ViewGroup parent) {
    final Episode episode = getItem(position);

    return episodeViewInflater.inflate(recycledView).with(episode).from(parent);
  }
}

package com.mypodcasts.podcast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Episode;

import java.util.List;

public class EpisodeListAdapter extends BaseAdapter {

  private final List<Episode> episodes;
  private final LayoutInflater inflater;

  public EpisodeListAdapter(List<Episode> episodes, LayoutInflater inflater) {
    this.episodes = episodes;
    this.inflater = inflater;
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
  public View getView(int position, View convertView, ViewGroup parent) {
    View row = inflater.inflate(R.layout.episode_list_item, parent, false);
    TextView textView = (TextView) row.findViewById(R.id.episode_title);
    textView.setText(getItem(position).getTitle());

    return row;
  }
}

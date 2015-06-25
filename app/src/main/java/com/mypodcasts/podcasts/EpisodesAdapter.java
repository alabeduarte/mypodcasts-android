package com.mypodcasts.podcasts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mypodcasts.R;

public class EpisodesAdapter extends BaseAdapter {

  private final LayoutInflater inflater;

  public EpisodesAdapter(LayoutInflater inflater) {
    this.inflater = inflater;
  }

  @Override
  public int getCount() {
    return 1;
  }

  @Override
  public Object getItem(int position) {
    return null;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return inflater.inflate(R.layout.episode_item, parent, false);
  }
}

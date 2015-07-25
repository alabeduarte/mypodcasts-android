package com.mypodcasts.feeds;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Feed;

import java.util.List;

public class FeedsAdapter extends BaseAdapter {
  private final List<Feed> feeds;
  private final LayoutInflater inflater;

  public FeedsAdapter(List<Feed> feeds, LayoutInflater inflater) {
    this.feeds = feeds;
    this.inflater = inflater;
  }

  @Override
  public int getCount() {
    return feeds.size();
  }

  @Override
  public Feed getItem(int position) {
    return feeds.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View row = inflater.inflate(R.layout.drawer_list_item, parent, false);
    TextView textView = (TextView) row.findViewById(R.id.feed_title);
    textView.setText(getItem(position).getTitle());

    return row;
  }
}

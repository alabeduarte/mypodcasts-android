package com.mypodcasts.podcast;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Episode;

import java.util.List;

public class EpisodeListAdapter extends BaseAdapter {

  private final List<Episode> episodes;
  private final LayoutInflater inflater;
  private ImageLoader imageLoader;

  public EpisodeListAdapter(List<Episode> episodes, LayoutInflater inflater, ImageLoader imageLoader) {
    this.episodes = episodes;
    this.inflater = inflater;
    this.imageLoader = imageLoader;
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
    Episode episode = getItem(position);

    View row = inflater.inflate(R.layout.episode_list_item, parent, false);

    setTitle(episode, row);
    setImageUrl(episode, row);
    disableMediaPlayButtonFocus(row);
    disableDownloadButtonFocus(row);

    return row;
  }

  private void setTitle(Episode episode, View row) {
    TextView textView = (TextView) row.findViewById(R.id.episode_title);
    textView.setText(episode.getTitle());
  }

  private void setImageUrl(Episode episode, View row) {
    NetworkImageView networkImageView = (NetworkImageView) row.findViewById(R.id.episode_thumbnail);
    String imageUrl = episode.getImage() == null ? episodeDefaultImageUrl() : episode.getImage().getUrl();

    networkImageView.setImageUrl(imageUrl, imageLoader);
  }

  private void disableMediaPlayButtonFocus(View row) {
    row.findViewById(R.id.media_play_button).setFocusable(false);
  }

  private void disableDownloadButtonFocus(View row) {
    row.findViewById(R.id.episode_download_button).setFocusable(false);
  }

  @NonNull
  private String episodeDefaultImageUrl() {
    return inflater.getContext().getResources().getString(R.string.episode_default_image);
  }
}

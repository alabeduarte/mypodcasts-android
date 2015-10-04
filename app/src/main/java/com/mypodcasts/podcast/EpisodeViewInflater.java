package com.mypodcasts.podcast;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerActivity;
import com.mypodcasts.podcast.models.Episode;
import com.mypodcasts.podcast.models.Image;
import com.mypodcasts.support.FileDownloadManager;

public class EpisodeViewInflater {
  private final LayoutInflater layoutInflater;
  private final ViewGroup parent;
  private final ImageLoader imageLoader;
  private final FileDownloadManager fileDownloadManager;

  public EpisodeViewInflater(LayoutInflater layoutInflater, ViewGroup parent, ImageLoader imageLoader, FileDownloadManager fileDownloadManager) {
    this.layoutInflater = layoutInflater;
    this.parent = parent;
    this.imageLoader = imageLoader;
    this.fileDownloadManager = fileDownloadManager;
  }

  public View inflate(View recycledView, Episode episode) {
    View view;
    if (recycledView == null) {
      view = layoutInflater.inflate(R.layout.episode_list_item, parent, false);
    } else {
      view = recycledView;
    }

    return new EpisodeView(view, episode).getView();
  }

  private class EpisodeView {
    private final View view;
    private TextView titleTextView;
    private NetworkImageView networkImageView;
    private ImageButton mediaPlayButton;
    private ImageButton downloadButton;

    protected EpisodeView(View view, Episode episode) {
      this.view = view;

      this.setTitle(episode.getTitle());
      this.setImageUrl(episode.getImage());
      this.setMediaPlayButton(episode);
      this.setDownloadButton(episode);
    }

    protected View getView() {
      return view;
    }

    private void setTitle(String title) {
      titleTextView = (TextView) view.findViewById(R.id.episode_title);
      titleTextView.setText(title);
    }

    private void setImageUrl(Image image) {
      networkImageView = (NetworkImageView) view.findViewById(R.id.episode_thumbnail);
      String imageUrl = image == null ? episodeDefaultImageUrl() : image.getUrl();

      networkImageView.setImageUrl(imageUrl, imageLoader);
    }

    private void setMediaPlayButton(final Episode episode) {
      mediaPlayButton = (ImageButton) view.findViewById(R.id.media_play_button);
      mediaPlayButton.setFocusable(false);

      mediaPlayButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Context context = view.getContext();
          Intent intent = new Intent(context, AudioPlayerActivity.class);
          intent.putExtra(Episode.class.toString(), episode);

          context.startActivity(intent);
        }
      });
    }

    private void setDownloadButton(final Episode episode) {
      downloadButton = (ImageButton) view.findViewById(R.id.episode_download_button);
      downloadButton.setFocusable(false);

      downloadButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          fileDownloadManager.enqueue(
              Uri.parse(episode.getAudio().getUrl()),
              Environment.DIRECTORY_PODCASTS,
              episode.getAudioFilePath()
          );
        }
      });
    }

    @NonNull
    private String episodeDefaultImageUrl() {
      return view.getContext().getResources().getString(R.string.episode_default_image);
    }
  }
}

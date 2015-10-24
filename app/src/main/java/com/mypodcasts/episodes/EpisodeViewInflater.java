package com.mypodcasts.episodes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Image;

import javax.inject.Inject;

import static android.view.View.INVISIBLE;

public class EpisodeViewInflater {
  private final LayoutInflater layoutInflater;
  private final ImageLoader imageLoader;
  private final EpisodeDownloader episodeDownloader;

  @Inject
  public EpisodeViewInflater(Activity activity, ImageLoader imageLoader, EpisodeDownloader episodeDownloader) {
    this.layoutInflater = activity.getLayoutInflater();
    this.imageLoader = imageLoader;
    this.episodeDownloader = episodeDownloader;
  }

  protected InflaterWith inflate(View view) {
    return new InflaterWith(view);
  }

  protected class InflaterWith {
    private final View view;
    public InflaterWith(View view) {
      this.view = view;
    }

    public InflaterFrom with(Episode episode) {
      return new InflaterFrom(view, episode);
    }
  }
  protected class InflaterFrom {
    private final View view;
    private final Episode episode;
    public InflaterFrom(View view, Episode episode) {
      this.view = view;
      this.episode = episode;
    }

    public View from(ViewGroup parent) {
      return inflate(view, parent, episode);
    }
  }

  private View inflate(View recycledView, ViewGroup parent, Episode episode) {
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

      this.setMediaPlayButtonLayout(episode);
      this.setDownloadButtonLayout(episode);
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

    private void setMediaPlayButtonLayout(final Episode episode) {
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

    private void setDownloadButtonLayout(final Episode episode) {
      ViewGroup downloadLayout = (ViewGroup) view.findViewById(R.id.episode_download_layout);
      if (episodeDownloader.isAlreadyDownloaded(episode)) {
        downloadLayout.setVisibility(INVISIBLE);
        return;
      } else {
        downloadLayout.setVisibility(View.VISIBLE);
      }

      downloadButton = (ImageButton) view.findViewById(R.id.episode_download_button);
      downloadButton.setFocusable(false);

      downloadButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          episodeDownloader.download(episode);
        }
      });
    }

    @NonNull
    private String episodeDefaultImageUrl() {
      return view.getContext().getResources().getString(R.string.episode_default_image);
    }
  }
}

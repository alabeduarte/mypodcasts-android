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

import org.joda.time.DateTime;

import java.util.Date;

import javax.inject.Inject;

import static android.text.Html.fromHtml;
import static android.text.format.Formatter.formatShortFileSize;
import static android.view.View.INVISIBLE;
import static java.lang.Long.valueOf;
import static org.joda.time.format.DateTimeFormat.forPattern;

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

    return new EpisodeView(view, episode)
        .withTitle()
        .withDescription()
        .withPublishedDate()
        .withDuration()
        .withAudioLength()
        .withImageUrl(imageLoader)
        .withMediaPlayButtonLayout()
        .withDownloadButtonLayout(episodeDownloader)
        .getView();
  }
}

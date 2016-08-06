package com.mypodcasts.episodes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import static android.text.Html.fromHtml;
import static android.text.format.Formatter.formatShortFileSize;
import static android.view.View.INVISIBLE;
import static java.lang.Long.valueOf;
import static org.joda.time.format.DateTimeFormat.forPattern;

class EpisodeView {
  private final View view;
  private final Episode episode;

  protected EpisodeView(View view, Episode episode) {
    this.view = view;
    this.episode = episode;
  }

  protected View getView() {
    return view;
  }

  EpisodeView withTitle() {
    TextView titleTextView = (TextView) view.findViewById(R.id.episode_title);
    titleTextView.setText(episode.getTitle());

    return this;
  }

  EpisodeView withDescription() {
    TextView descriptionTextView = (TextView) view.findViewById(R.id.episode_description);
    descriptionTextView.setText(fromHtml(episode.getDescription()));

    return this;
  }

  public EpisodeView withPublishedDate() {
    TextView publishedDateTextView = (TextView) view.findViewById(R.id.episode_published_date);

    Date publishedDate = episode.getPublishedDate();

    if (publishedDate == null) {
      publishedDateTextView.setVisibility(INVISIBLE);
      publishedDateTextView.setText("");
    } else {
      String formattedDateTime = new DateTime(publishedDate).toString(forPattern("MMM dd, yyyy"));
      publishedDateTextView.setText(formattedDateTime.toUpperCase());
    }

    return this;
  }

  EpisodeView withDuration() {
    TextView durationTextView = (TextView) view.findViewById(R.id.episode_duration);
    durationTextView.setText(episode.getDuration());

    return this;
  }

  EpisodeView withAudioLength() {
    TextView audioLengthTextView = (TextView) view.findViewById(R.id.episode_length);

    try {
      String formattedShortFileSize = formatShortFileSize(view.getContext(), valueOf(episode.getAudioLength()));
      audioLengthTextView.setText(formattedShortFileSize);
    } catch (NumberFormatException ex) {
      audioLengthTextView.setText("");
    }

    return this;
  }

  EpisodeView withImageUrl(final ImageLoader imageLoader) {
    NetworkImageView networkImageView = (NetworkImageView) view.findViewById(R.id.episode_thumbnail);

    Image image = episode.getImage();
    String imageUrl = image == null ? episodeDefaultImageUrl() : image.getUrl();

    networkImageView.setImageUrl(imageUrl, imageLoader);

    return this;
  }

  EpisodeView withMediaPlayButtonLayout() {
    ImageButton mediaPlayButton = (ImageButton) view.findViewById(R.id.media_play_button);
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

    return this;
  }

  EpisodeView withDownloadButtonLayout(final EpisodeDownloader episodeDownloader) {
    ViewGroup downloadLayout = (ViewGroup) view.findViewById(R.id.episode_download_layout);
    if (episodeDownloader.isAlreadyDownloaded(episode)) {
      downloadLayout.setVisibility(INVISIBLE);
      return this;
    } else {
      downloadLayout.setVisibility(View.VISIBLE);
    }

    ImageButton downloadButton = (ImageButton) view.findViewById(R.id.episode_download_button);
    downloadButton.setFocusable(false);

    downloadButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        episodeDownloader.download(episode);
      }
    });

    return this;
  }

  @NonNull
  private String episodeDefaultImageUrl() {
    return view.getContext().getResources().getString(R.string.episode_default_image);
  }
}

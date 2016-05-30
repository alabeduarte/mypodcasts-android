package com.mypodcasts.episodes;

import android.content.Context;
import android.content.SharedPreferences;

import com.mypodcasts.repositories.models.Episode;

import javax.inject.Inject;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.String.format;

public class EpisodeCheckpoint {
  public static final String STATE = EpisodeCheckpoint.class.toString();
  private final Context context;

  @Inject
  public EpisodeCheckpoint(Context context) {
    this.context = context;
  }

  public Integer markCheckpoint(Episode episode, Integer currentPosition) {
    SharedPreferences.Editor editor = getSharedPreferences().edit();
    editor.putInt(episodeKey(episode), currentPosition).apply();

    return currentPosition;
  }

  public Integer getLastCheckpointPosition(Episode episode, Integer defaultPosition) {
    SharedPreferences sharedPreferences = getSharedPreferences();

    return sharedPreferences.getInt(episodeKey(episode), defaultPosition);
  }

  private SharedPreferences getSharedPreferences() {
    return context.getSharedPreferences(STATE, MODE_PRIVATE);
  }

  private String episodeKey(Episode episode) {
    return format(
        "podcast_%s#episode_%s",
        replaceFromSpaceToUnderscore(episode.getPodcast().getTitle()),
        replaceFromSpaceToUnderscore(episode.getTitle())
    );
  }

  private String replaceFromSpaceToUnderscore(String value) {
    return value.toLowerCase().replace(" ", "_");
  }
}

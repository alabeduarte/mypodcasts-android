package com.mypodcasts.episodes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mypodcasts.repositories.models.Episode;

import javax.inject.Inject;

import static android.content.Context.MODE_PRIVATE;
import static com.mypodcasts.support.Support.MYPODCASTS_TAG;
import static java.lang.String.format;

public class EpisodeCheckpoint {
  public static final String STATE = EpisodeCheckpoint.class.toString();
  private final Context context;

  @Inject
  public EpisodeCheckpoint(Context context) {
    this.context = context;
  }

  public int markCheckpoint(Episode episode, int currentPosition) {
    SharedPreferences.Editor editor = getSharedPreferences().edit();

    Log.i(MYPODCASTS_TAG, "Mark playing episode checkpoint: " + episode);

    editor.putInt(episodeKey(episode), currentPosition).apply();

    return currentPosition;
  }

  public int getLastCheckpointPosition(Episode episode, int defaultPosition) {
    SharedPreferences sharedPreferences = getSharedPreferences();

    return sharedPreferences.getInt(episodeKey(episode), defaultPosition);
  }

  private SharedPreferences getSharedPreferences() {
    return context.getSharedPreferences(STATE, MODE_PRIVATE);
  }

  private String episodeKey(Episode episode) {
    return format(
        "podcast_%s#episode_%s",
        episode.getFeed().getId(),
        replaceFromSpaceToUnderscore(episode.getTitle())
    );
  }

  private String replaceFromSpaceToUnderscore(String value) {
    return value.toLowerCase().replace(" ", "_");
  }
}

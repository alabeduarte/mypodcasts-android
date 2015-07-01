package com.mypodcasts.player;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Episode;

import java.io.IOException;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;

import static android.media.AudioManager.STREAM_MUSIC;

public class AudioPlayerActivity extends RoboActivity {

  @Inject
  private ProgressDialog progressDialog;

  @Inject
  private MediaPlayer mediaPlayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    new Player().execute(getAudioUrl());
  }

  @Override
  protected void onPause() {
    super.onPause();
    mediaPlayer.reset();
    mediaPlayer.release();
  }

  class Player extends AsyncTask<String, Void, Boolean> {

    @Override
    protected void onPreExecute() {
      progressDialog.show();
      progressDialog.setMessage(getResources().getString(R.string.loading_episode));
    }

    @Override
    protected Boolean doInBackground(String... params) {
      startPlayer(getUrl(params));

      return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
      progressDialog.cancel();
    }

    private String getUrl(String... params) {
      return params[0];
    }

    private void startPlayer(String url) {
      mediaPlayer.setAudioStreamType(STREAM_MUSIC);
      try {
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
      } catch (IOException e) {
        e.printStackTrace();
      }

      if (!mediaPlayer.isPlaying()) {
        mediaPlayer.start();
      }
    }
  }

  private String getAudioUrl() {
    Episode episode = (Episode) getIntent().getSerializableExtra(Episode.class.toString());
    return episode.getAudio().getUrl();
  }
}

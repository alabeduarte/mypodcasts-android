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
import roboguice.inject.ContentView;

import static android.media.AudioManager.STREAM_MUSIC;

@ContentView(R.layout.audio_player)
public class AudioPlayerActivity extends RoboActivity {

  @Inject
  private ProgressDialog progressDialog;

  @Inject
  private AudioPlayerStreaming audioPlayerStreaming;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Episode episode = (Episode) getIntent().getSerializableExtra(Episode.class.toString());
    new Player().execute(episode);
  }

  @Override
  protected void onPause() {
    super.onPause();
    audioPlayerStreaming.release();
  }

  class Player extends AsyncTask<Episode, Void, MediaPlayer> {

    @Override
    protected void onPreExecute() {
      progressDialog.show();
      progressDialog.setMessage(getResources().getString(R.string.loading_episode));
    }

    @Override
    protected MediaPlayer doInBackground(Episode... params) {
      try {
        return audioPlayerStreaming.play(getEpisode(params));
      } catch (IOException e) {
        e.printStackTrace();
      }

      return null;
    }

    @Override
    protected void onPostExecute(MediaPlayer mediaPlayer) {
      progressDialog.cancel();
    }

    private Episode getEpisode(Episode... params) {
      return params[0];
    }
  }
}

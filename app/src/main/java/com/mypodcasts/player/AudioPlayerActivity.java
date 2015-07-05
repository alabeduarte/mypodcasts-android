package com.mypodcasts.player;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Episode;

import java.io.IOException;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static android.media.AudioManager.STREAM_MUSIC;
import static java.lang.String.valueOf;

@ContentView(R.layout.audio_player)
public class AudioPlayerActivity extends RoboActivity {

  @InjectView(R.id.play_pause_button)
  Button playPauseButton;

  @Inject
  private ProgressDialog progressDialog;

  @Inject
  private AudioPlayerStreaming audioPlayerStreaming;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    playPauseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        audioPlayerStreaming.pause();
        togglePlayButtonLabel();
      }
    });
    togglePlayButtonLabel();

    Episode episode = (Episode) getIntent().getSerializableExtra(Episode.class.toString());
    new Player(episode).execute();
  }

  @Override
  protected void onPause() {
    super.onPause();
    audioPlayerStreaming.release();
  }

  class Player extends AsyncTask<Void, Void, MediaPlayer> {
    private final Episode episode;

    Player(Episode episode) {
      this.episode = episode;
    }

    @Override
    protected void onPreExecute() {
      progressDialog.show();
      progressDialog.setMessage(getResources().getString(R.string.loading_episode));
    }

    @Override
    protected MediaPlayer doInBackground(Void... params) {
      try {
        return audioPlayerStreaming.play(episode);
      } catch (IOException e) {
        e.printStackTrace();
      }

      return null;
    }

    @Override
    protected void onPostExecute(MediaPlayer mediaPlayer) {
      progressDialog.cancel();
      togglePlayButtonLabel();
    }
  }

  private void togglePlayButtonLabel() {
    String buttonLabel = audioPlayerStreaming.isPlaying() ?
        valueOf(getResources().getText(R.string.pause)) :
        valueOf(getResources().getText(R.string.play));

    playPauseButton.setText(buttonLabel);
  }
}

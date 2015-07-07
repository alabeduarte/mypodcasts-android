package com.mypodcasts.player;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Episode;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static java.lang.String.valueOf;

@ContentView(R.layout.audio_player)
public class AudioPlayerActivity extends RoboActivity {

  @InjectView(R.id.play_pause_button)
  Button playPauseButton;

  @Inject
  private ProgressDialog progressDialog;

  @Inject
  private AudioPlayerMediator audioPlayerMediator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupPlayPauseButton();

    new AudioPlayerAsyncTask().execute();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  class AudioPlayerAsyncTask extends AsyncTask<Void, Void, AudioPlayerMediator> {

    @Override
    protected void onPreExecute() {
      progressDialog.show();
      progressDialog.setMessage(getResources().getString(R.string.loading_episode));
    }

    @Override
    protected AudioPlayerMediator doInBackground(Void... params) {
      return playAudio();
    }

    @Override
    protected void onPostExecute(AudioPlayerMediator audioPlayerMediator) {
      progressDialog.cancel();
      togglePlayButtonLabel();
    }
  }

  private AudioPlayerMediator playAudio() {
    final Episode episode = (Episode)
        getIntent().getSerializableExtra(Episode.class.toString());

    return audioPlayerMediator.startService(this, episode);
  }

  private void togglePlayButtonLabel() {
    String buttonLabel = audioPlayerMediator.isPlaying() ?
        valueOf(getResources().getText(R.string.pause)) :
        valueOf(getResources().getText(R.string.play));

    playPauseButton.setText(buttonLabel);
  }

  private void setupPlayPauseButton() {
    final Episode episode = (Episode)
        getIntent().getSerializableExtra(Episode.class.toString());

    playPauseButton.setText(valueOf(getResources().getText(R.string.play)));
    playPauseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        audioPlayerMediator.togglePlayPauseFor(episode);

        togglePlayButtonLabel();
      }
    });
  }
}

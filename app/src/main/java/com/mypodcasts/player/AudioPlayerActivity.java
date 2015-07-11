package com.mypodcasts.player;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Episode;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static java.lang.String.valueOf;

@ContentView(R.layout.audio_player)
public class AudioPlayerActivity extends RoboActivity {

  @InjectView(R.id.play_pause_button)
  Button playPauseButton;

  @Inject
  private EventBus eventBus;
  private AudioPlayerService audioPlayerService;

  @Inject
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    eventBus.register(this);

    setupPlayPauseButton();

    new AudioPlayerAsyncTask().execute();
  }

  @Override
  protected void onStop() {
    super.onStop();

    eventBus.unregister(this);
  }

  public void onEvent(AudioPlayingEvent event){
    audioPlayerService = event.getAudioPlayerService();
    togglePlayButtonLabel();
  }

  class AudioPlayerAsyncTask extends AsyncTask<Void, Void, Episode> {

    @Override
    protected void onPreExecute() {
      progressDialog.show();
      progressDialog.setMessage(getResources().getString(R.string.loading_episode));
    }

    @Override
    protected Episode doInBackground(Void... params) {
      return playAudio();
    }

    @Override
    protected void onPostExecute(Episode episode) {
      if (progressDialog != null && progressDialog.isShowing()) {
        progressDialog.cancel();
      }
    }
  }

  private Episode playAudio() {
    final Episode episode = (Episode)
        getIntent().getSerializableExtra(Episode.class.toString());

    Intent intent = new Intent(
        AudioPlayerActivity.this,
        AudioPlayerService.class
    );

    intent.putExtra(Episode.class.toString(), episode);
    startService(intent);

    return episode;
  }

  private void togglePlayButtonLabel() {
    String buttonLabel = audioPlayerService.isPlaying() ?
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
        audioPlayerService.togglePlayPauseFor(episode);

        togglePlayButtonLabel();
      }
    });
  }
}

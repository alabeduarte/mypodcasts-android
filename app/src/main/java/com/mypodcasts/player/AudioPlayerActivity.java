package com.mypodcasts.player;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Episode;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.audio_player)
public class AudioPlayerActivity extends RoboActivity {

  @Inject
  private EventBus eventBus;
  private AudioPlayerService audioPlayerService;

  @Inject
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    eventBus.register(this);

    new AudioPlayerAsyncTask().execute();
  }

  @Override
  protected void onStop() {
    super.onStop();

    eventBus.unregister(this);
  }

  public void onEvent(AudioPlayingEvent event){
    audioPlayerService = event.getAudioPlayerService();
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
}

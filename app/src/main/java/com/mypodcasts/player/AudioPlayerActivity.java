package com.mypodcasts.player;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.mypodcasts.R;
import com.mypodcasts.podcast.models.Episode;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import roboguice.activity.RoboActionBarActivity;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.audio_player)
public class AudioPlayerActivity extends RoboActionBarActivity {

  @Inject
  private EventBus eventBus;
  private AudioPlayer audioPlayer;

  @Inject
  private AudioPlayerController mediaController;

  @Inject
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    eventBus.register(this);

    showProgressDialog();
    playAudio();
  }

  @Override
  protected void onStop() {
    super.onStop();

    eventBus.unregister(this);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    mediaController.show();

    return super.onTouchEvent(event);
  }

  public void onEvent(AudioPlayingEvent event){
    cancelProgressDialog();

    audioPlayer = event.getAudioPlayer();

    mediaController.setMediaPlayer(audioPlayer);
    mediaController.setAnchorView(findViewById(R.id.audio_view));
    mediaController.show();
  }

  private void showProgressDialog() {
    progressDialog.show();
    progressDialog.setMessage(getResources().getString(R.string.loading_episode));
  }

  private void cancelProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.cancel();
    }
  }

  private void playAudio() {
    Intent intent = new Intent(
        AudioPlayerActivity.this,
        AudioPlayerService.class
    );

    intent.putExtra(Episode.class.toString(), getEpisode());
    stopService(intent);
    startService(intent);
  }

  private Episode getEpisode() {
    return (Episode) getIntent().getSerializableExtra(Episode.class.toString());
  }
}

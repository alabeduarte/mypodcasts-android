package com.mypodcasts.player;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.mypodcasts.R;
import com.mypodcasts.repositories.models.Episode;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static java.lang.String.format;

@ContentView(R.layout.audio_player)
public class AudioPlayerActivity extends RoboActionBarActivity {

  @InjectView(R.id.tool_bar)
  private Toolbar toolbar;

  @Inject
  private EventBus eventBus;
  private AudioPlayer audioPlayer;

  @Inject
  private AudioPlayerController mediaController;

  @Inject
  private ProgressDialog progressDialog;

  private Episode episode;
  private int playerCurrentPosition;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    eventBus.register(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    showProgressDialog();
    playAudio();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) { onBackPressed(); }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onStop() {
    super.onStop();

    eventBus.unregister(this);
  }

  @Override
  protected void onPause() {
    super.onPause();

    if (audioPlayer != null) {
      playerCurrentPosition = audioPlayer.getCurrentPosition();
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    mediaController.show();

    return super.onTouchEvent(event);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(AudioPlayer.class.toString(), playerCurrentPosition);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    playerCurrentPosition = savedInstanceState.getInt(AudioPlayer.class.toString());
  }

  public void onEvent(AudioPlayingEvent event){
    cancelProgressDialog();

    audioPlayer = event.getAudioPlayer();
    audioPlayer.seekTo(playerCurrentPosition);

    mediaController.setMediaPlayer(audioPlayer);
    mediaController.setAnchorView(findViewById(R.id.audio_view));
    mediaController.show();
  }

  private void showProgressDialog() {
    String episodeTitle = getEpisode() == null ? "" : getEpisode().getTitle();

    progressDialog.show();
    progressDialog.setMessage(
        format(
            getResources().getString(R.string.loading_episode),
            episodeTitle
        )
    );
  }

  private void cancelProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
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
    if (episode == null) {
      episode = (Episode) getIntent().getSerializableExtra(Episode.class.toString());
    }

    return episode;
  }
}

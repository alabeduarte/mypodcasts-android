package com.mypodcasts.player;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.mypodcasts.R;
import com.mypodcasts.episodes.EpisodeCheckpoint;
import com.mypodcasts.repositories.models.Episode;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import retryable.asynctask.RetryableAsyncTask;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static android.text.Html.fromHtml;
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

  @Inject
  private EpisodeCheckpoint episodeCheckpoint;

  @InjectView(R.id.episode_description)
  private TextView episodeDescription;

  private Episode episode;
  private int playerCurrentPosition;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    eventBus.register(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    new PlayAudioAsyncTask().execute();
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
      setPlayerCurrentPosition(audioPlayer.getCurrentPosition());
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
    setPlayerCurrentPosition(savedInstanceState.getInt(AudioPlayer.class.toString()));
  }

  public void onEvent(AudioPlayingEvent event){
    dismissProgressDialog();

    audioPlayer = event.getAudioPlayer();
    audioPlayer.seekTo(
        episodeCheckpoint.getLastCheckpointPosition(episode, playerCurrentPosition)
    );

    mediaController.setMediaPlayer(audioPlayer);
    mediaController.setAnchorView(findViewById(R.id.audio_view));
    mediaController.show();
  }

  private void setPlayerCurrentPosition(Integer newPosition) {
    playerCurrentPosition = newPosition;
    episodeCheckpoint.markCheckpoint(episode, audioPlayer.getCurrentPosition());
  }

  private void dismissProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  private class PlayAudioAsyncTask extends RetryableAsyncTask<Void, Void, Episode> {
    public PlayAudioAsyncTask() {
      super(AudioPlayerActivity.this);
    }

    @Override
    protected void onPreExecute() {
      showProgressDialog();
    }

    @Override
    protected Episode doInBackground(Void... params) {
      return playAudio();
    }

    @Override
    protected void onPostExecute(Episode episode) {
      if (episode == null) { return; }

      episodeDescription.setText(fromHtml(episode.getDescription()));
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

    private Episode playAudio() {
      Intent intent = new Intent(
          AudioPlayerActivity.this,
          AudioPlayerService.class
      );

      Episode episode = getEpisode();
      intent.putExtra(Episode.class.toString(), episode);

      Log.i("[mypodcasts]", "playing episode: " + episode);

      stopService(intent);
      startService(intent);

      return episode;
    }

    private Episode getEpisode() {
      if (episode == null) {
        episode = (Episode) getIntent().getSerializableExtra(Episode.class.toString());
      }

      return episode;
    }
  }
}

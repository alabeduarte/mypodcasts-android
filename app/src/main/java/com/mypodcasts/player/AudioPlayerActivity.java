package com.mypodcasts.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.mypodcasts.R;
import com.mypodcasts.episodes.EpisodeCheckpoint;
import com.mypodcasts.player.events.AudioPlayingEvent;
import com.mypodcasts.player.events.AudioStoppedEvent;
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.support.Support;

import javax.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static android.text.Html.fromHtml;
import static com.mypodcasts.player.AudioPlayerService.ACTION_PLAY;
import static com.mypodcasts.support.Support.MYPODCASTS_TAG;

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

    mediaController.hide();

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

  @Subscribe
  public void onEvent(AudioPlayingEvent event) {
    Log.d(Support.MYPODCASTS_TAG, "[AudioPlayerActivity][onEvent][AudioPlayingEvent]");

    audioPlayer = event.getAudioPlayer();
    audioPlayer.seekTo(
        episodeCheckpoint.getLastCheckpointPosition(episode, playerCurrentPosition)
    );

    mediaController.setMediaPlayer(audioPlayer);
    mediaController.setAnchorView(findViewById(R.id.audio_view));
    mediaController.show();

    if (episode != null) {
      episodeDescription.setText(fromHtml(episode.getDescription()));
    }
  }

  @Subscribe
  public void onEvent(AudioStoppedEvent event) {
    Log.d(Support.MYPODCASTS_TAG, "[AudioPlayerActivity][onEvent][AudioStoppedEvent]");

    finish();
  }

  private Episode playAudio() {
    Intent intent = new Intent(AudioPlayerActivity.this, AudioPlayerService.class);
    intent.setAction(ACTION_PLAY);

    Episode episode = getEpisode();
    intent.putExtra(Episode.class.toString(), episode);

    Log.i(MYPODCASTS_TAG, "playing episode: " + episode);

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

  private void setPlayerCurrentPosition(int newPosition) {
    if (episode == null || audioPlayer == null) return;

    playerCurrentPosition = newPosition;
    episodeCheckpoint.markCheckpoint(episode, audioPlayer.getCurrentPosition());
  }
}

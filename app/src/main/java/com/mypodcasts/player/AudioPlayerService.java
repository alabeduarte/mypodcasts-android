package com.mypodcasts.player;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;

import com.mypodcasts.podcast.models.Episode;

import java.io.IOException;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import roboguice.service.RoboService;

public class AudioPlayerService extends RoboService {

  public static final int ONGOING_NOTIFICATION_ID = 1;

  @Inject
  private Context context;

  @Inject
  private EventBus eventBus;

  @Inject
  private AudioPlayerStreaming audioPlayerStreaming;

  @Inject
  private Notification.Builder notificationBuilder;

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    final Episode episode = (Episode)
        intent.getSerializableExtra(Episode.class.toString());

    Notification notification =
        notificationBuilder
            .setContentTitle("My Podcasts")
            .setContentText("Some awesome podcast!")
            .build();

    startForeground(ONGOING_NOTIFICATION_ID, notification);

    new Player(episode).execute();
    eventBus.post(new AudioPlayingEvent(this));

    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    audioPlayerStreaming.release();
  }

  public void pause() {
    audioPlayerStreaming.pause();
  }

  public void unPause(Episode episode) {
    audioPlayerStreaming.unPause(episode);
  }

  class Player extends AsyncTask<Void, Void, MediaPlayer> {
    private final Episode episode;

    Player(Episode episode) {
      this.episode = episode;
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
  }
}

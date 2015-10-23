package com.mypodcasts.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;

import com.mypodcasts.R;
import com.mypodcasts.repositories.models.Episode;

import java.io.IOException;

import javax.inject.Inject;

import roboguice.service.RoboService;

public class AudioPlayerService extends RoboService {

  public static final int ONGOING_NOTIFICATION_ID = 1;

  @Inject
  private Context context;

  @Inject
  private AudioPlayer audioPlayer;

  @Inject
  private Notification.Builder notificationBuilder;

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    final Episode episode = (Episode) intent.getSerializableExtra(Episode.class.toString());
    startForeground(ONGOING_NOTIFICATION_ID, buildNotification());

    new Player(episode).execute();

    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    audioPlayer.release();
  }

  private Notification buildNotification() {
    Notification.MediaStyle mediaStyle = new Notification.MediaStyle();

    return notificationBuilder
        .setSmallIcon(R.drawable.ic_av_play_circle_fill)
        .setContentTitle("My Podcasts")
        .setContentText("Some awesome podcast!")
        .setStyle(mediaStyle)
        .setVisibility(Notification.VISIBILITY_PUBLIC)
        .addAction(generateAction(android.R.drawable.ic_media_previous, "Previous", "previous"))
        .addAction(generateAction(android.R.drawable.ic_media_rew, "Rewind", "rewind"))
        .addAction(generateAction(android.R.drawable.ic_media_pause, "Pause", "pause"))
        .addAction(generateAction(android.R.drawable.ic_media_ff, "Fast Foward", "fastForward"))
        .addAction(generateAction(android.R.drawable.ic_media_next, "Next", "next"))
        .build();
  }

  private Notification.Action generateAction( int icon, String title, String intentAction ) {
    Intent intent = new Intent(getApplicationContext(), this.getClass());
    intent.setAction( intentAction );
    PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);

    return new Notification.Action.Builder(icon, title, pendingIntent).build();
  }

  class Player extends AsyncTask<Void, Void, MediaPlayer> {
    private final Episode episode;

    Player(Episode episode) {
      this.episode = episode;
    }

    @Override
    protected MediaPlayer doInBackground(Void... params) {
      try {
        return audioPlayer.play(episode);
      } catch (IOException e) {
        e.printStackTrace();
      }

      return null;
    }
  }
}

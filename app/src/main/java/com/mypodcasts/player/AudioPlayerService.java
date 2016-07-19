package com.mypodcasts.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.mypodcasts.R;
import com.mypodcasts.repositories.models.Episode;

import java.io.IOException;

import javax.inject.Inject;

import roboguice.service.RoboService;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.mypodcasts.support.Support.MYPODCASTS_TAG;

public class AudioPlayerService extends RoboService {

  public static final int ONGOING_NOTIFICATION_ID = 1;

  public static final String ACTION_PREVIOUS = "action_previous";
  public static final String ACTION_REWIND = "action_rewind";
  public static final String ACTION_PAUSE = "action_pause";
  public static final String ACTION_PLAY = "action_play";
  public static final String ACTION_STOP = "action_stop";
  public static final String ACTION_FAST_FORWARD = "action_fast_foward";
  public static final String ACTION_NEXT = "action_next";

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

    try {
      audioPlayer.play(episode);
    } catch (IOException e) {
      Log.e(MYPODCASTS_TAG, e.getMessage());
      e.printStackTrace();
    }

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
        .addAction(generateAction(android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS))
        .addAction(generateAction(android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND))
        .addAction(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE))
        .addAction(generateAction(android.R.drawable.ic_media_ff, "Fast Forward", ACTION_FAST_FORWARD))
        .addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT))
        .build();
  }

  private Notification.Action generateAction( int icon, String title, String intentAction ) {
    Intent intent = new Intent(getApplicationContext(), this.getClass());
    intent.setAction(intentAction);
    PendingIntent pendingIntent = PendingIntent.getService(
        getApplicationContext(), 1, intent, FLAG_UPDATE_CURRENT
    );

    return new Notification.Action.Builder(icon, title, pendingIntent).build();
  }
}
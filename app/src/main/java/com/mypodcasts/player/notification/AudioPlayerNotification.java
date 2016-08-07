package com.mypodcasts.player.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;

import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerService;
import com.mypodcasts.repositories.models.Episode;

import javax.inject.Inject;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.mypodcasts.player.AudioPlayerService.ACTION_FAST_FORWARD;
import static com.mypodcasts.player.AudioPlayerService.ACTION_PAUSE;
import static com.mypodcasts.player.AudioPlayerService.ACTION_REWIND;
import static com.mypodcasts.player.AudioPlayerService.ACTION_STOP;

public class AudioPlayerNotification {
  private final Context context;
  private final Notification.Builder notificationBuilder;

  protected static final String REWIND = "Rewind";
  protected static final String PAUSE = "Pause";
  protected static final String STOP = "Stop";
  protected static final String FAST_FORWARD = "Fast Forward";

  @Inject
  public AudioPlayerNotification(Context context, Notification.Builder notificationBuilder) {
    this.context = context;
    this.notificationBuilder = notificationBuilder;
  }

  public Notification buildNotification(Episode episode) {
    Notification.MediaStyle mediaStyle = new Notification.MediaStyle();

    return notificationBuilder
        .setSmallIcon(R.drawable.ic_av_play_circle_fill)
        .setContentTitle(episode.getFeed().getTitle())
        .setContentText(episode.getTitle())
        .setStyle(mediaStyle)
        .setVisibility(Notification.VISIBILITY_PUBLIC)
        .addAction(generateAction(episode, R.drawable.ic_fast_rewind, REWIND, ACTION_REWIND))
        .addAction(generateAction(episode, R.drawable.ic_pause, PAUSE, ACTION_PAUSE))
        .addAction(generateAction(episode, R.drawable.ic_stop_black, STOP, ACTION_STOP))
        .addAction(generateAction(episode, R.drawable.ic_fast_forward, FAST_FORWARD, ACTION_FAST_FORWARD))
        .build();
  }

  private Notification.Action generateAction(Episode episode, int icon, String title, String intentAction) {
    Intent intent = new Intent(context, AudioPlayerService.class);
    intent.setAction(intentAction);

    intent.putExtra(Episode.class.toString(), episode);

    PendingIntent pendingIntent = PendingIntent.getService(context, 1, intent, FLAG_UPDATE_CURRENT);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      return new Notification.Action.Builder(Icon.createWithResource("", icon), title, pendingIntent).build();
    } else {
      return new Notification.Action.Builder(icon, title, pendingIntent).build();
    }
  }
}
package com.mypodcasts.player.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.mypodcasts.R;
import com.mypodcasts.player.AudioPlayerService;
import com.mypodcasts.repositories.models.Episode;

import javax.inject.Inject;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.mypodcasts.player.AudioPlayerService.ACTION_FAST_FORWARD;
import static com.mypodcasts.player.AudioPlayerService.ACTION_PAUSE;
import static com.mypodcasts.player.AudioPlayerService.ACTION_REWIND;

public class AudioPlayerNotification {
  private final Context context;
  private final Notification.Builder notificationBuilder;

  @Inject
  public AudioPlayerNotification(Context context, Notification.Builder notificationBuilder) {
    this.context = context;
    this.notificationBuilder = notificationBuilder;
  }

  public Notification buildNotification(Episode episode) {
    Notification.MediaStyle mediaStyle = new Notification.MediaStyle();

    return notificationBuilder
        .setSmallIcon(R.drawable.ic_av_play_circle_fill)
        .setContentTitle(episode.getPodcast().getTitle())
        .setContentText(episode.getTitle())
        .setStyle(mediaStyle)
        .setVisibility(Notification.VISIBILITY_PUBLIC)
        .addAction(generateAction(episode, android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND))
        .addAction(generateAction(episode, android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE))
        .addAction(generateAction(episode, android.R.drawable.ic_media_ff, "Fast Forward", ACTION_FAST_FORWARD))
        .build();
  }

  private Notification.Action generateAction(Episode episode, int icon, String title, String intentAction) {
    Intent intent = new Intent(context, AudioPlayerService.class);
    intent.setAction(intentAction);

    intent.putExtra(Episode.class.toString(), episode);

    PendingIntent pendingIntent = PendingIntent.getService(context, 1, intent, FLAG_UPDATE_CURRENT);

    return new Notification.Action.Builder(icon, title, pendingIntent).build();
  }
}
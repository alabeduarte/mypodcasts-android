package com.mypodcasts.player.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.graphics.drawable.Icon;
import android.os.Build;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.mypodcasts.player.notification.AudioPlayerNotification.FAST_FORWARD;
import static com.mypodcasts.player.notification.AudioPlayerNotification.PAUSE;
import static com.mypodcasts.player.notification.AudioPlayerNotification.REWIND;
import static com.mypodcasts.player.notification.AudioPlayerNotification.STOP;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AudioPlayerNotificationTest {

  Notification.Builder notificationBuilder = new Notification.Builder(application);
  AudioPlayerNotification audioPlayerNotification = new AudioPlayerNotification(application, notificationBuilder);

  Episode episode = new Episode() {
    @Override
    public Feed getFeed() {
      return new Feed() {
        @Override
        public String getTitle() {
          return "Foo Bar";
        }
      };
    }

    @Override
    public String getTitle() {
      return "Lorem ipsum dolor sit amet";
    }
  };

  @Test
  public void itSetsNotificationSmallIcon() {
    Notification notification = audioPlayerNotification.buildNotification(episode);

    assertThat(notification.icon, is(R.drawable.ic_av_play_circle_fill));
  }

  @Test
  public void itSetsNotificationVisibility() {
    Notification notification = audioPlayerNotification.buildNotification(episode);

    assertThat(notification.visibility, is(Notification.VISIBILITY_PUBLIC));
  }

  @Test
  public void itSetsNotificationContentTitle() {
    Notification notification = audioPlayerNotification.buildNotification(episode);

    assertThat(notification.extras.getString(Notification.EXTRA_TITLE), is("Foo Bar"));
  }

  @Test
  public void itSetsNotificationContentText() {
    Notification notification = audioPlayerNotification.buildNotification(episode);

    assertThat(notification.extras.getString(Notification.EXTRA_TEXT), is("Lorem ipsum dolor sit amet"));
  }

  @TargetApi(Build.VERSION_CODES.M)
  @Test
  public void itReturnsNotificationWithMediaControlActions() {
    Notification notification = audioPlayerNotification.buildNotification(episode);

    assertThat(String.valueOf(notification.actions[0].title), is(REWIND));
    assertThat(
        notification.actions[0].getIcon().toString(),
        is(Icon.createWithResource("", R.drawable.ic_fast_rewind).toString())
    );

    assertThat(String.valueOf(notification.actions[1].title), is(PAUSE));
    assertThat(
        notification.actions[1].getIcon().toString(),
        is(Icon.createWithResource("", R.drawable.ic_pause).toString())
    );

    assertThat(String.valueOf(notification.actions[2].title), is(STOP));
    assertThat(
        notification.actions[2].getIcon().toString(),
        is(Icon.createWithResource("", R.drawable.ic_stop_black).toString())
    );

    assertThat(String.valueOf(notification.actions[3].title), is(FAST_FORWARD));
    assertThat(
        notification.actions[3].getIcon().toString(),
        is(Icon.createWithResource("", R.drawable.ic_fast_forward).toString())
    );
  }
}
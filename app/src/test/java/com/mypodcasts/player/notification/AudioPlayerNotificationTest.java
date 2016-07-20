package com.mypodcasts.player.notification;

import android.app.Notification;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.mypodcasts.player.notification.AudioPlayerNotification.FAST_FORWARD;
import static com.mypodcasts.player.notification.AudioPlayerNotification.PAUSE;
import static com.mypodcasts.player.notification.AudioPlayerNotification.REWIND;
import static com.mypodcasts.player.notification.AudioPlayerNotification.STOP;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricGradleTestRunner.class)
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

  @Test
  public void itReturnsNotificationWithMediaControlActions() {
    Notification notification = audioPlayerNotification.buildNotification(episode);

    assertThat(String.valueOf(notification.actions[0].title), is(REWIND));
    assertThat(notification.actions[0].icon, is(android.R.drawable.ic_media_rew));

    assertThat(String.valueOf(notification.actions[1].title), is(PAUSE));
    assertThat(notification.actions[1].icon, is(android.R.drawable.ic_media_pause));

    assertThat(String.valueOf(notification.actions[2].title), is(STOP));
    assertThat(notification.actions[2].icon, is(android.R.drawable.ic_lock_power_off));

    assertThat(String.valueOf(notification.actions[3].title), is(FAST_FORWARD));
    assertThat(notification.actions[3].icon, is(android.R.drawable.ic_media_ff));
  }
}
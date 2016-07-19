package com.mypodcasts.player.notification;

import android.app.Notification;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Podcast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

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
    public Podcast getPodcast() {
      return new Podcast() {
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
}
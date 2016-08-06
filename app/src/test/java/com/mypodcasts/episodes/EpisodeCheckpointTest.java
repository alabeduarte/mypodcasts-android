package com.mypodcasts.episodes;

import android.content.SharedPreferences;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Feed;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.mypodcasts.util.EpisodeHelper.anEpisode;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EpisodeCheckpointTest {

  EpisodeCheckpoint episodeCheckpoint;

  @Before
  public void setup() {
    episodeCheckpoint = new EpisodeCheckpoint(application);
  }

  @Test
  public void itStoresCurrentStateOfPlayingEpisode() {
    int audioPosition = new Random().nextInt();

    assertThat(episodeCheckpoint.markCheckpoint(anEpisode(), audioPosition), is(audioPosition));
    assertThat(
        getSharedPreferences().getInt("podcast_crazy_id_123#episode_an_awesome_episode", 0),
        is(audioPosition)
    );
  }

  @Test
  public void itFetchesCurrentStateOfPlayingEpisode() {
    int audioPosition = new Random().nextInt();

    SharedPreferences.Editor editor = getSharedPreferences().edit();
    editor.putInt("podcast_crazy_id_123#episode_an_awesome_episode", audioPosition).apply();

    assertThat(episodeCheckpoint.getLastCheckpointPosition(anEpisode(), 0), is(audioPosition));
  }

  @Test
  public void itReturnsDefaultValueWhenCouldNotFindEpisodeCheckpointOfPlayingEpisode() {
    assertThat(episodeCheckpoint.getLastCheckpointPosition(anEpisode(), 42), is(42));
  }

  private SharedPreferences getSharedPreferences() {
    return application.getSharedPreferences(
        EpisodeCheckpoint.STATE, MODE_PRIVATE
    );
  }
}
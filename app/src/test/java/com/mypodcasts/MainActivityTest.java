package com.mypodcasts;

import android.app.Activity;

import com.mypodcasts.support.CustomTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(CustomTestRunner.class)
public class MainActivityTest {

  private MainActivity activity;

  @Before
  public void setup() {
    activity = buildActivity(MainActivity.class).create().get();
  }

  @Test
  public void itLoadsLatestEpisodeGivenUrl() {
    activity.onCreate(null);

  }
}
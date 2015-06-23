package com.mypodcasts;

import android.app.Activity;

import com.mypodcasts.support.CustomTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(CustomTestRunner.class)
public class MainActivityTest {

  @Test
  public void testSomething() {
    Activity activity = buildActivity(MainActivity.class).create().get();

    assertNotNull(activity);
  }
}
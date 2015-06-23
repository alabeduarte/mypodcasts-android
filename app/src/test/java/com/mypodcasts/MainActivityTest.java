package com.mypodcasts;

import com.mypodcasts.support.CustomTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(CustomTestRunner.class)
public class MainActivityTest {
  @Test
  public void testSomething() {
    assertTrue(buildActivity(MainActivity.class).create().get() != null);
  }
}
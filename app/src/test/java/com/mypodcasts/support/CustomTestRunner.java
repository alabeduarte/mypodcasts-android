package com.mypodcasts.support;

import android.app.Activity;

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.res.Fs;

public class CustomTestRunner extends RobolectricTestRunner {
  private static final int MAX_SDK_SUPPORTED_BY_ROBOLECTRIC = 18;

  public CustomTestRunner(Class<?> testClass) throws InitializationError {
    super(testClass);
  }

  @Override
  protected AndroidManifest getAppManifest(Config config) {
    String manifestProperty = "../app/src/main/AndroidManifest.xml";
    String resProperty = "../app/src/main/res";
    return new AndroidManifest(Fs.fileFromPath(manifestProperty), Fs.fileFromPath(resProperty)) {
      @Override
      public int getTargetSdkVersion() {
        return MAX_SDK_SUPPORTED_BY_ROBOLECTRIC;
      }

      @Override
      public String getThemeRef(Class<? extends Activity> activityClass) {
        return "@style/RoboAppTheme";
      }
    };
  }
}

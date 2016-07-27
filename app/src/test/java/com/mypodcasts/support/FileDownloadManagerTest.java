package com.mypodcasts.support;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Environment;

import com.google.inject.AbstractModule;
import com.mypodcasts.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;
import static roboguice.RoboGuice.Util.reset;
import static roboguice.RoboGuice.overrideApplicationInjector;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class FileDownloadManagerTest {
  private DownloadManager downloadManagerMock = mock(DownloadManager.class);

  @Before
  public void setup() {
    overrideApplicationInjector(application, new MyTestModule());
  }

  @After
  public void teardown() {
    reset();
  }

  @Test
  public void itDelegatesToDownloadManageEnqueueRequest() {
    FileDownloadManager fileDownloadManager = new FileDownloadManager(downloadManagerMock);

    Uri uri = Uri.parse("http://google.com");
    String directory = Environment.DIRECTORY_DOWNLOADS;
    String filePath = "download.html";
    fileDownloadManager.enqueue(uri, directory, filePath);

    verify(downloadManagerMock).enqueue(any(DownloadManager.Request.class));
  }

  public class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(DownloadManager.class).toInstance(downloadManagerMock);
    }
  }
}
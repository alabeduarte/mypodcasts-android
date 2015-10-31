package com.mypodcasts.support.asyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mypodcasts.BuildConfig;
import com.mypodcasts.R;
import com.mypodcasts.support.asynkTask.RetryableAsyncTask;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNetworkInfo;

import java.util.concurrent.ExecutionException;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.net.NetworkInfo.DetailedState.CONNECTED;
import static android.net.NetworkInfo.DetailedState.DISCONNECTED;
import static java.lang.String.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Robolectric.flushBackgroundThreadScheduler;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class RetryableAsyncTaskTest {

  RetryableAsyncTask retryableAsyncTask;
  Activity activity;

  AsyncTaskStep onPreExecuteMock = mock(AsyncTaskStep.class);
  AsyncTaskStep doInBackgroundMock = mock(AsyncTaskStep.class);
  AsyncTaskStep onPostExecuteMock = mock(AsyncTaskStep.class);

  @Before
  public void setUp() {
    activity = buildActivity(Activity.class).create().get();
    retryableAsyncTask = new RetryableAsyncTask<Object, Void, String>(activity) {

      @Override
      protected void onPreExecute() {
        onPreExecuteMock.call();
      }

      @Override
      protected String doInBackground(Object... params) {
        doInBackgroundMock.call(params);

        return "bar";
      }

      @Override
      protected void onPostExecute(String result) {
        onPostExecuteMock.call(result);
      }
    };
  }

  @Test
  public void itShowsDialogWhenThereIsNoConnectivity() {
    disconnectToWifiNetwork();
    retryableAsyncTask.execute();

    assertThatAlertDialogIsShown();
  }

  @Test
  public void itRetriesAsyncTaskExecutionOnButtonTouch() throws ExecutionException, InterruptedException {
    disconnectToWifiNetwork();
    retryableAsyncTask.execute();

    flushBackgroundThreadScheduler();

    assertThatAlertDialogIsShown();
    assertNull(retryableAsyncTask.get());

    connectToWifiNetwork();

    AlertDialog alert = getLatestAlertDialog();
    alert.getButton(BUTTON_POSITIVE).performClick();

    flushBackgroundThreadScheduler();

    assertThat(retryableAsyncTask.get(), CoreMatchers.<Object>is("bar"));
    assertThat(getLatestAlertDialog().isShowing(), is(false));
  }

  @Test
  public void itShowsDialogWhenActiveNetworkIsNull() {
    shadowOf(getConnectivityManager()).setActiveNetworkInfo(null);

    retryableAsyncTask.execute();

    assertThatAlertDialogIsShown();
  }

  @Test
  public void itDoesNotShowDialogWhenThereIsConnectivity() {
    connectToWifiNetwork();
    retryableAsyncTask.execute();

    assertNull(getLatestAlertDialog());
  }

  @Test
  public void itDoesNotExecutAsyncTaskIfThereIsNoConnectivity() throws ExecutionException, InterruptedException {
    disconnectToWifiNetwork();
    retryableAsyncTask.execute();

    flushBackgroundThreadScheduler();

    assertNull(retryableAsyncTask.get());
  }

  @Test
  public void itDelegatesTaskToAsyncTaskWhenGetIsCalled() throws ExecutionException, InterruptedException {
    retryableAsyncTask.execute("foo");

    flushBackgroundThreadScheduler();

    assertThat(retryableAsyncTask.get(), CoreMatchers.<Object>is("bar"));
  }

  @Test
  public void itDelegatesTaskToAsyncTaskWhenOnPreExecuteIsCalled() {
    retryableAsyncTask.execute("foo");

    verify(onPreExecuteMock).call();
  }

  @Test
  public void itDelegatesTaskToAsyncTaskWhenDoInBackgroundIsCalled() {
    retryableAsyncTask.execute("foo");

    verify(doInBackgroundMock).call("foo");
  }

  @Test
  public void itDelegatesTaskToAsyncTaskWhenOnPostExecuteIsCalled() {
    retryableAsyncTask.execute("foo");

    verify(onPostExecuteMock).call("bar");
  }

  private void connectToWifiNetwork() {
    setWitiNetworkConnectivity(CONNECTED, true);
  }

  private void disconnectToWifiNetwork() {
    setWitiNetworkConnectivity(DISCONNECTED, false);
  }

  private void setWitiNetworkConnectivity(NetworkInfo.DetailedState status, boolean isConnected) {
    ConnectivityManager connectivityManager = getConnectivityManager();
    int subType = 0;
    boolean isAvailable = true;

    NetworkInfo networkInfo = ShadowNetworkInfo.newInstance(
        status, TYPE_WIFI, subType, isAvailable, isConnected
    );

    shadowOf(connectivityManager).setActiveNetworkInfo(networkInfo);
  }

  private ConnectivityManager getConnectivityManager() {
    return (ConnectivityManager) application.getSystemService(CONNECTIVITY_SERVICE);
  }

  private void assertThatAlertDialogIsShown() {
    AlertDialog alert = getLatestAlertDialog();
    String noInternetConnection = valueOf(shadowOf(alert).getMessage());
    String retry = valueOf(alert.getButton(BUTTON_POSITIVE).getText());

    assertThat(noInternetConnection, is(application.getString(R.string.no_internet_connection)));
    assertThat(retry, is(application.getString(R.string.retry)));
  }

  private class AsyncTaskStep {
    public void call() {}

    public void call(Object... args) {}
  }
}
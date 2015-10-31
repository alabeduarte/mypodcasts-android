package com.mypodcasts.support.asynkTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.mypodcasts.R;

import java.util.concurrent.ExecutionException;

public abstract class RetryableAsyncTask<Params, Progress, Result> {
  private final Context context;
  private AsyncTask task;

  public RetryableAsyncTask(Context context) {
    this.context = context;
  }

  public final AsyncTask<Params, Progress, Result> execute(final Params... params) {
    if (isNotConnected()) {
      showDialog(context.getResources().getString(R.string.no_internet_connection), params);

      task = new NullableAsyncTask();
    } else {
      task = new ConcreteAsyncTask(this);
    }

    return task.execute(params);
  }

  public final Result get() throws InterruptedException, ExecutionException {
    return (Result) task.get();
  }

  protected void onPreExecute() {};

  protected abstract Result doInBackground(final Params... params);
  protected abstract void onPostExecute(final Result result);

  private void showDialog(String message, final Params... params) {
    AlertDialog dialog = new AlertDialog.Builder(context)
        .setMessage(message)
        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            RetryableAsyncTask.this.execute(params);
            dialog.dismiss();
          }
        })
        .create();

    dialog.show();
  }

  private boolean isNotConnected() {
    return !isConnected();
  }

  private boolean isConnected() {
    return getActiveNetworkInfo() != null &&
        getActiveNetworkInfo().isConnectedOrConnecting();
  }

  private NetworkInfo getActiveNetworkInfo() {
    return getConnectivityManager().getActiveNetworkInfo();
  }

  private ConnectivityManager getConnectivityManager() {
    return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  }
}

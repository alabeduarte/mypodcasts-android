package com.mypodcasts.support.asynkTask;

import android.os.AsyncTask;

class ConcreteAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
  private RetryableAsyncTask parentTask;

  public ConcreteAsyncTask(RetryableAsyncTask parentTask) {
    this.parentTask = parentTask;
  }

  @Override
  protected void onPreExecute() {
    parentTask.onPreExecute();
  }

  @Override
  protected Result doInBackground(final Params... params) {
    return (Result) parentTask.doInBackground(params);
  }

  @Override
  protected void onPostExecute(Result result) {
    parentTask.onPostExecute(result);
  }
}

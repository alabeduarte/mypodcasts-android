package com.mypodcasts.support.asynkTask;

import android.os.AsyncTask;

class NullableAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
  @Override
  protected Result doInBackground(Params... params) {
    return null;
  }
}

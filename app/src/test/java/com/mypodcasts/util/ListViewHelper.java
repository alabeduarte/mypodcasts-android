package com.mypodcasts.util;

import android.widget.ListView;

public class ListViewHelper {

  public static void performItemClickAtPosition(ListView listView, int position) {
    listView.performItemClick(
        listView.getAdapter().getView(position, null, null),
        position,
        position
    );
  }
}
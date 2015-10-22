package org.robolectric.shadows;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarDrawerToggle;

import org.robolectric.annotation.Implements;

import static org.robolectric.RuntimeEnvironment.application;

/*
  Shadow for ActionBarDrawerToggle

  https://github.com/robolectric/robolectric/issues/1446
 */
@Implements(ActionBarDrawerToggle.class)
public class ShadowActionBarDrawerToggle {

  public Drawable getThemeUpIndicator() {
    final TypedArray typedArray = application.obtainStyledAttributes(
        new int[] { android.R.attr.homeAsUpIndicator }
    );
    final Drawable result = typedArray.getDrawable(0);
    typedArray.recycle();

    return result;
  }
}
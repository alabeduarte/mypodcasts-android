package com.mypodcasts.support;

import java.io.File;

public class ExternalPublicFileLookup {
  public boolean exists(File directory, String filePath) {
    return new File(directory, filePath).exists();
  }
}

package com.mypodcasts.support;

import java.io.File;

public class ExternalPublicFileLookup {
  public boolean exists(String directory, String filePath) {
    return new File("file://" + directory + "/" + filePath).exists();
  }
}

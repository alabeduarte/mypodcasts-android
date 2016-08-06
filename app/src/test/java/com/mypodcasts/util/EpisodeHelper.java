package com.mypodcasts.util;

import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Feed;

public class EpisodeHelper {
  public static Episode anEpisode() {
    return new Episode() {
      @Override public String getTitle() {
        return "An awesome episode";
      }

      @Override public Feed getFeed() {
        return new Feed() {
          @Override
          public String getId() {
            return "crazy_id_123";
          }
        };
      }
    };
  }
}

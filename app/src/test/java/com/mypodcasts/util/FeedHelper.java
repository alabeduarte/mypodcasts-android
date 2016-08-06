package com.mypodcasts.util;

import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Feed;
import com.mypodcasts.repositories.models.Image;

import java.util.Collections;
import java.util.List;

public class FeedHelper {
  public static Feed aFeed(final String title) {
    return aFeed(title, Collections.<Episode>emptyList());
  }

  public static Feed aFeedWith(final List<Episode> episodes) {
    return aFeed(null, episodes);
  }

  public static Feed aFeed() {
    return aFeed("Awesome Podcast");
  }

  public static Feed aFeed(final String title, final List<Episode> episodes) {
    return new Feed() {
      @Override
      public String getId() {
        return "123";
      }

      @Override
      public Image getImage() {
        return new Image() {
          @Override
          public String getUrl() {
            return "http://example.com/feed.png";
          }
        };
      }

      @Override
      public String getTitle() {
        return title;
      }

      @Override
      public List<Episode> getEpisodes() {
        return episodes;
      }
    };
  }
}

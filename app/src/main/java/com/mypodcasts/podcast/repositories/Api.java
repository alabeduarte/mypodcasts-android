package com.mypodcasts.podcast.repositories;

import com.mypodcasts.podcast.models.Episode;
import com.mypodcasts.podcast.models.Feed;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

public interface Api {
  @GET("/api/user/johndoe/latest_episodes")
  List<Episode> getLatestEpisodes();

  @GET("/api/user/johndoe/feeds")
  List<Feed> getUserFeeds();

  @GET("/api/feeds/{id}")
  Feed getFeed(@Path("id") String id);
}

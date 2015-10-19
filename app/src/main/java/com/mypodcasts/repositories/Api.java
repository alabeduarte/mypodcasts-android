package com.mypodcasts.repositories;

import com.mypodcasts.repositories.models.Episode;
import com.mypodcasts.repositories.models.Feed;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

public interface Api {
  @GET("/api/user/johndoe/latest_episodes")
  List<Episode> getLatestEpisodes();

  @GET("/api/user/johndoe/feeds")
  List<Feed> getUserFeeds();

  @GET("/api/user/johndoe/feeds/{id}/episodes")
  Feed getFeed(@Path("id") String id);
}

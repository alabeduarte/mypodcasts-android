package com.mypodcasts.podcast;

import com.mypodcasts.podcast.models.Episode;

import java.util.List;

import retrofit.http.GET;

public interface HttpClient {
  @GET("/api/user/johndoe/latest_episodes")
  List<Episode> getLatestEpisodes();
}

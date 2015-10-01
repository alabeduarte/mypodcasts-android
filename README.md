[![Build Status](https://snap-ci.com/alabeduarte/mypodcasts-android/branch/master/build_image)](https://snap-ci.com/alabeduarte/mypodcasts-android/branch/master)

# My Podcasts

Podcast client for android.

![alt latest_episodes](screenshots/latest_episodes.png)

![alt menu](screenshots/menu.png)

![alt player](screenshots/player.png)

## Running tests:
```
$ ./gradlew test
```

## Running locally with stubbed service

As we don't have any API live yet, there is a stub that return some feeds for local tests purposes.

- Install [nodejs](https://nodejs.org/)
- Run stub:
```
$ ./stub.sh
```

Once the server is up, you can run your app normally as a regular android project.

*Note: Android API 19 is required*
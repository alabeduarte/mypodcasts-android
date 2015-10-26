# My Podcasts

[![Build Status](https://snap-ci.com/alabeduarte/mypodcasts-android/branch/master/build_image)](https://snap-ci.com/alabeduarte/mypodcasts-android/branch/master)

Podcast client for android.

<img src="screenshots/menu.png" width="220" height="380" />
<img src="screenshots/latest_episodes.png" width="220" height="380" />
<img src="screenshots/player.png" width="220" height="380" />

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

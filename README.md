# My Podcasts

[![Build Status](https://snap-ci.com/alabeduarte/mypodcasts-android/branch/master/build_image)](https://snap-ci.com/alabeduarte/mypodcasts-android/branch/master)

Podcast client for android.

Let's make a good app to listen our favorite podcasts with all amazing features that we imagine for free!

<img src="screenshots/menu.png" width="220" height="380" />
<img src="screenshots/latest_episodes.png" width="220" height="380" />
<img src="screenshots/player.png" width="220" height="380" />

## Contributing:
I have a set of features to be worked and I plan to make them available as soon as possible which are:

* Improve MediaPlayer notification with controls.
* Fix orientation change bug on audio playing.
* Download Complete Feedback.
* Feed search (using [mypodcasts-api](https://github.com/alabeduarte/mypodcasts-api) on top of some existing API that retrieves those). 
 
Some of this [issues](https://github.com/alabeduarte/mypodcasts-android/issues) are listed already, so any help will be well received.

If you want to use the app and report bugs, please [send me an email](alabeduarte@gmail.com) so I can add you to the Beta testers list on [TestFairy](https://free.testfairy.com).

## Running tests:
I strongly recommend to use [Android Studio](http://developer.android.com/sdk/index.html) for a smooth integration, but follow the command below if you wanna run all unit tests on your own environment:

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

Take a look at the backend API [mypodcasts-api](https://github.com/alabeduarte/mypodcasts-api) if you want to contribute in there as well.

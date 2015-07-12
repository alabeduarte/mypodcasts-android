# My Podcasts

Podcast client for android.

## Running tests:
```
$ ./gradlew test
```

## Running locally with stubbed service

As we don't have any API live yet, there is a stub that return some feeds for local tests purposes.

- Install [nodejs](https://nodejs.org/)
- Setup dependencies:
```
$ npm install
```

- Run stub:
```
$ node stub/index.js
```

Once the server is up, you can run your app normally as a regular android project.

*Note: Android API 19 is required*
#!/usr/bin/env bash

BRANCH="master"

if [ "$TRAVIS_BRANCH" = "$BRANCH" ]; then
  if [ "$TRAVIS_PULL_REQUEST" = false ]; then
    if [ -z "$TRAVIS_TAG" ]; then
      echo -e "Starting to tag commit.\n"

      git config --global user.email "travis@travis-ci.org"
      git config --global user.name "Travis"

      APK_VERSION=$(cat app/build.gradle | grep versionName | awk '{print $2}' | sed 's/"//g')

      git tag -a v${APK_VERSION}-${TRAVIS_BUILD_NUMBER} -m "Travis build $TRAVIS_BUILD_NUMBER pushed a tag."
      git push origin --tags
      git fetch origin

      echo -e "Pushed the tag v${APK_VERSION}-${TRAVIS_BUILD_NUMBER}"
    fi
  fi
fi
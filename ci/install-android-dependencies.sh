#!/usr/bin/env bash

download-android
echo y | android update sdk --no-ui --filter platform-tool
echo y | android update sdk --no-ui --filter android-21
echo y | android update sdk --no-ui --all --filter build-tools-21.1.2 --force
echo y | android update sdk --no-ui --filter extra

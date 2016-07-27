#!/usr/bin/env bash

download-android
echo y | android update sdk --no-ui --filter platform-tool
echo y | android update sdk --no-ui --filter android-23
echo y | android update sdk --no-ui --all --filter build-tools-23.0.3 --force
echo y | android update sdk --no-ui --filter extra

#!/bin/bash

./gradlew assemble

echo "Upload app to TestFairy"

rm app-release-unsigned.apk
cp app/build/outputs/apk/app-release-unsigned.apk .

curl https://app.testfairy.com/api/upload -F api_key=$API_KEY -F file=@app-release-unsigned.apk

./gradlew clean
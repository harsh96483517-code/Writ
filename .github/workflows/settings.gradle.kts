name: Build APK
on:
  push:
    branches: [main]
  pull_request:
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17
      - uses: gradle/actions/setup-gradle@v4
        with:
          gradle-version: 8.9
      - name: Find Gradle project
        id: find
        run: |
          f=$(find . -name settings.gradle.kts | head -1)
          if [ -z "$f" ]; then echo "settings.gradle.kts not found in repo"; exit 1; fi
          echo "dir=$(dirname "$f")" >> "$GITHUB_OUTPUT"
      - name: Build debug APK
        working-directory: ${{ steps.find.outputs.dir }}
        run: gradle assembleDebug --stacktrace
      - uses: actions/upload-artifact@v4
        with:
          name: HindiPatra-debug-apk
          path: "**/build/outputs/apk/debug/*.apk"

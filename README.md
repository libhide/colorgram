# Colorgram

<img src="https://github.com/libhide/colorgram/blob/master/art/logo.png" alt="Colorgram Branding">

Colorgram is tiny app I built over a weekend to fix a particular itch of mine.

## Story

### The Itch

Instagram for the longest time did not have a way to fill a "story" slide with a solid color. I wanted to do this on my personal Instagram to make stories more engaging because hey! FUN! 

## The App

All Colorgram does is allows the user to create a color using RGB sliders and save the resulting color has a JPG to the phone. The user can then go to Instagram and use the created image as the background for their story update!

## Development

### Requirements

- JDK 17
- Android SDK with API 37

### Setup

Build the debug app; local signing credentials are not required:

```shell
./gradlew :app:assembleDebug
```

Run the project checks with:

```shell
./gradlew testDebugUnitTest lintDebug
```

### Release signing

Release builds require a local signing keystore. If this app is already published, use its existing signing or upload key; generating another key will not allow updates signed by the old key. Keep the keystore and its credentials backed up securely.

To create a new upload keystore for a new distribution:

```shell
keytool -genkeypair -v \
  -keystore keystore-colorgram \
  -alias colorgram-upload \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

Copy the signing-properties template and replace its placeholder passwords with the values used above:

```shell
cp keystore.properties.example keystore.properties
```

The default template expects `keystore-colorgram` in the repository root. To use another location, update `storeFile` in `keystore.properties`; relative paths are resolved from the repository root.

Create the signed Android App Bundle with:

```shell
./gradlew :app:bundleRelease
```

`keystore.properties` and common keystore file formats are ignored by Git. Never commit signing credentials or the private keystore.

## Repo Structure

The repo has two long-lived branches: `master` and `gh-pages`.

- `master` contains the Android app written in Kotlin.
- `gh-pages` houses the landing page for the website because of course I'm extra af :)

## Postmortem

Soooooo, Instagram started supporting this feature natively a couple of months after I released this app. Aaaand with that, my dreams of becomnig a rockstart indie-developer died. RIP.


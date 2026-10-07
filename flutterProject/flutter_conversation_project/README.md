# Flutter Samples

Shared Flutter implementation for the [iOS / Android / Flutter comparison](../../README.md).

This app is the **recommended multi-platform path** in the repository: one Dart codebase for the same sample surfaces that the SwiftUI and Jetpack Compose ports implement separately.

## Run

```sh
flutter pub get
flutter analyze
flutter devices
flutter run -d <device-id>
```

Use an iOS simulator and an Android emulator with the same build to see shared behaviour from one implementation.

## Scope

Primary UI lives in [`lib/main.dart`](lib/main.dart). Samples cover gradients, forms, animations, task lists, adaptive grids, navigation, and tips.

Cupertino widgets match the iOS-oriented reference visuals; Material (or a custom design system) is equally valid for a product app.

## Why this port matters

Native iOS and Android each need their own feature implementation. Flutter keeps UI, state, navigation, and most tests in one place, then validates on each host. That is the delivery model this comparison recommends when more than one platform must ship.

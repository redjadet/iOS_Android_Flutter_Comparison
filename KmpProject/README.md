# KMP Samples (Compose Multiplatform)

Shared **Kotlin + Compose Multiplatform** port of the comparison demos. The UI lives in [`shared/src/commonMain/kotlin/com/example/kmpsamples/SamplesApp.kt`](shared/src/commonMain/kotlin/com/example/kmpsamples/SamplesApp.kt) and runs on Android and iOS from one Kotlin tree.

## Role in the repository

| Stack | Multi-platform UI | Typical team fit |
|---|---|---|
| **Flutter** | Dart widgets, broad official targets | **Default** when you must develop and deploy across platforms |
| **KMP (this project)** | Shared Compose UI in `commonMain` | Kotlin-first teams already using Compose on Android |
| Native SwiftUI + Compose | Separate apps | Single-platform fidelity |

KMP is a credible multi-platform path, but it still requires Xcode for iOS, expect/actual for some platform APIs, and Compose-on-iOS trade-offs. In this repository **Flutter remains the preferred general multi-platform solution**.

- Overview and decision tables: [../README.md](../README.md)
- Detailed assessment: [../CODEBASE_COMPARISON.md](../CODEBASE_COMPARISON.md)

## Run — Android

Requirements: **JDK 17 or 21** (JDK 25 is not supported by this Gradle/AGP stack), Android SDK (API 24+).

```sh
cd KmpProject
# Optional local SDK path (gitignored):
# echo "sdk.dir=$HOME/Library/Android/sdk" > local.properties
./gradlew :androidApp:assembleDebug
```

Open `KmpProject/` in Android Studio with the KMP plugin, select the **androidApp** configuration, and run on an emulator or device.

## Run — iOS (local macOS)

Requirements: macOS, Xcode, Kotlin Multiplatform / Compose Multiplatform tooling.

1. Open `KmpProject/iosApp/iosApp.xcodeproj` in Xcode.
2. Build and run on a simulator or device. The Xcode build phase runs `./gradlew :shared:embedAndSignAppleFrameworkForXcode`.
3. Swift entry point: `IosMainKt.MainViewController()` in `ContentView.swift`.

If Gradle cannot find a JDK, set `JAVA_HOME` to a 17/21 install before building from Xcode.

Optional compile check without Xcode UI:

```sh
./gradlew :shared:compileKotlinIosSimulatorArm64
```

## Project layout

```text
KmpProject/
  shared/
    src/commonMain/   # SamplesApp UI, datetime helpers
    src/androidMain/  # Android expect/actual (date/time dialogs)
    src/iosMain/      # iOS expect/actual + MainViewController
  androidApp/         # Android application entry (src/main)
  iosApp/             # SwiftUI shell hosting ComposeUIViewController
```

## Notes

- Same demo catalogue as the native Compose and Flutter ports (gradients, forms, animation, tasks, grid, tips).
- Date/time pickers: native Android dialogs; iOS uses a lightweight demo dialog (`ReminderPicker.*.kt`).
- Prefer `formatFixed2` / kotlinx-datetime helpers over JVM-only APIs (`String.format`, `java.time`) in `commonMain`.
- CI builds `:androidApp:assembleDebug` on Ubuntu; iOS remains a local Xcode build (same pattern as the SwiftUI reference).

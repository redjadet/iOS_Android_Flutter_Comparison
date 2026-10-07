# iOS, Android, Flutter & KMP Comparison

[![CI](https://github.com/redjadet/iOS_Android_Flutter_Comparison/actions/workflows/ci.yml/badge.svg)](https://github.com/redjadet/iOS_Android_Flutter_Comparison/actions/workflows/ci.yml)

Side-by-side implementations of the same sample app in **SwiftUI**, **Jetpack Compose**, **Flutter**, and **Kotlin Multiplatform (Compose Multiplatform)**—so delivery cost, reuse, and platform trade-offs stay visible.

Not a production starter. Purpose: show when native specialization wins, when a shared codebase is better, and how Flutter and KMP differ as multi-platform strategies.

> **Recommendation**
>
> | Need | Choose |
> |---|---|
> | iOS only | **SwiftUI** |
> | Android only | **Jetpack Compose** |
> | **Develop & deploy on multiple platforms** (iOS + Android, with optional web/desktop) | **Flutter** (default) |
> | Multi-platform with a **Kotlin-first** team and shared Compose UI | **KMP** ([`KmpProject/`](KmpProject/)) |

## Project Overview

The four implementations reproduce the same interactive examples: gradients, forms, animations, task lists, adaptive grids, navigation, and a tips sheet.

| Implementation | Location | Language | UI framework | Role in this repository |
|---|---|---|---|---|
| Native iOS | [`iosSwiftUITest/Test/`](iosSwiftUITest/Test/) | Swift | SwiftUI | Apple-platform reference |
| Native Android | [`AndroidProject/`](AndroidProject/) | Kotlin | Jetpack Compose with Material 3 | Android-platform reference |
| Multi-platform (Flutter) | [`flutterProject/flutter_conversation_project/`](flutterProject/flutter_conversation_project/) | Dart | Flutter with Cupertino widgets | **Preferred** shared implementation for multi-platform delivery |
| Multi-platform (KMP) | [`KmpProject/shared/`](KmpProject/shared/) | Kotlin | Compose Multiplatform (Material 3) | Kotlin-native multi-platform reference |

Primary source files:

- [`ContentView.swift`](iosSwiftUITest/Test/Test/ContentView.swift)
- [`SamplesApp.kt`](AndroidProject/app/src/main/java/com/example/androidfromios/SamplesApp.kt)
- [`main.dart`](flutterProject/flutter_conversation_project/lib/main.dart)
- [`SamplesApp.kt` (KMP)](KmpProject/shared/src/commonMain/kotlin/com/example/kmpsamples/SamplesApp.kt)

Each implementation intentionally keeps most UI and state in one primary file. This makes equivalent behaviour easy to locate and compare; it is not a proposed architecture for a large application.

## Getting Started

### Native iOS

Requirements: macOS, Xcode, and a simulator or device compatible with the project's iOS 26 deployment target.

```sh
open iosSwiftUITest/Test/Test.xcodeproj
```

Select the `Test` scheme and run it from Xcode. The SwiftUI source includes `#Preview` declarations for the sample screens.

### Native Android

Requirements: Android Studio or a compatible JDK, plus Android SDK API 36.

```sh
cd AndroidProject
./gradlew :app:assembleDebug
```

Open `AndroidProject/` in Android Studio to run the application on an emulator or connected device.

### Flutter (recommended multi-platform path)

Requirements: a Flutter SDK compatible with the package's Dart `^3.9.2` constraint and the toolchain for the selected target.

```sh
cd flutterProject/flutter_conversation_project
flutter pub get
flutter analyze
flutter devices
flutter run -d <device-id>
```

Run the same sample on iOS and Android from one Dart tree. Optional web/desktop runners in the same package show how far a shared Flutter app can reach without a second product implementation.

### Kotlin Multiplatform (Compose Multiplatform)

Requirements: **JDK 17 or 21** (JDK 25 is not supported by this Gradle/AGP stack), Android SDK; iOS also requires macOS and Xcode.

```sh
cd KmpProject
./gradlew :androidApp:assembleDebug
```

Open `KmpProject/` in Android Studio (KMP plugin) for Android, or `KmpProject/iosApp/iosApp.xcodeproj` in Xcode for iOS. See [KmpProject/README.md](KmpProject/README.md).

## Notes

- The SwiftUI implementation uses native Apple controls, navigation, previews, and accessibility APIs.
- The Compose implementation uses Material 3 and Navigation Compose. Some screen scaffolding opts into `ExperimentalMaterial3Api`.
- The Flutter implementation uses Cupertino widgets because the original sample follows an iOS-oriented visual language. Flutter can also use Material widgets or an application-specific design system.
- The KMP port shares Material 3 Compose UI in `commonMain`; Android and iOS hosts load the same tree. Platform APIs (for example date pickers) use `expect`/`actual` boundaries.
- The native Android sample targets API 36 with `minSdk` 33. The native iOS sample declares an iOS 26 deployment target. KMP Android uses `minSdk` 24. These values describe this repository, not general framework requirements.
- The Flutter package includes mobile, web, and desktop runners. Primary comparison focus is shared iOS + Android delivery; extra runners illustrate Flutter’s broader multi-platform reach from the same app.
- State is local and in memory. The repository does not compare networking, persistence, authentication, dependency injection, or large-scale modular architecture.
- CI runs Flutter checks, native Android `assembleDebug`, and KMP `:androidApp:assembleDebug` on GitHub-hosted Ubuntu. iOS simulator jobs are not included—build iOS locally with Xcode.

Current upstream context, checked 27 July 2026: Flutter documentation reflects Flutter 3.44.7 and lists Android API 24-37 and iOS 13-26 as supported framework deployment ranges. Application targets can be stricter. [Flutter supported platforms](https://docs.flutter.dev/reference/supported-platforms) · [Flutter release notes](https://docs.flutter.dev/release/release-notes)

## Comparison Takeaways

### Native iOS: strongest Apple-platform specialization

SwiftUI provides direct Apple-framework access, familiar iOS behaviour, Xcode previews, and native accessibility integration. It is the most direct choice when iOS is the only required platform.

### Native Android: strongest Android-platform specialization

Jetpack Compose provides direct Android and AndroidX integration, Kotlin-first development, Material 3, and Android-specific lifecycle and tooling support. It is the most direct choice when Android is the only required platform.

### Flutter: strongest multi-platform solution

When the product must **develop and deploy on more than one platform**, Flutter is the best overall trade-off in this repository:

- One shared feature implementation instead of parallel Swift and Kotlin apps.
- One primary language and structure for UI, navigation, and state.
- Feature-parity by default—change once, validate on each target.
- Consistent rendering and test surface across iOS and Android (and additional Flutter targets when needed).
- Broader first-party reach: mobile plus documented web and desktop support from the same toolkit. [Flutter supported platforms](https://docs.flutter.dev/reference/supported-platforms)
- Native escape hatches via plugins, platform channels, FFI, and host code when a capability must stay platform-specific.
- Hot reload for rapid UI iteration while preserving application state. [Flutter hot reload](https://docs.flutter.dev/tools/hot-reload)

Flutter does not erase store signing, device QA, or some SDK integrations. Advantage: native work becomes a focused boundary, not a second full product.

### Kotlin Multiplatform: credible Kotlin-native multi-platform path

[`KmpProject/`](KmpProject/) adds **Compose Multiplatform** with the same sample screens in one Kotlin `commonMain` tree. It is the best comparison point when your organization is already Kotlin- and Compose-centric on Android and wants to reuse that skill on iOS.

Compared to Flutter in this repository, KMP shares UI code but typically carries a heavier iOS footprint (Skia/Compose runtime), more expect/actual work for platform APIs, and no web/desktop runner in this demo. Flutter remains the **default multi-platform recommendation** for breadth, integrated tooling, and a single product UI stack; KMP is the **Kotlin-native alternative** worth evaluating side by side.

## Platform Comparison

| Aspect | SwiftUI | Jetpack Compose | Flutter | KMP (Compose MP) |
|---|---|---|---|---|
| Primary target | Apple platforms | Android | **Multi-platform** (iOS, Android, plus web/desktop when needed) | iOS + Android (shared Compose UI) |
| Language | Swift | Kotlin | Dart | Kotlin |
| UI ownership | Native Apple framework | Native Android framework | Flutter rendering framework | Compose Multiplatform (Skia on iOS) |
| Apps needed for iOS + Android | Two native implementations (+ Compose) | Two native implementations (+ SwiftUI) | **One shared implementation** | **One shared UI** (+ thin hosts) |
| Product-code reuse across platforms | Low without a separate sharing strategy | Low without a separate sharing strategy | **High by default** | High for Compose UI; platform APIs via expect/actual |
| Develop / deploy once for multiple stores | No | No | **Yes for shared product surface** | Yes for shared Compose surface |
| Native API access | Direct | Direct | Plugins, platform channels, or host code | expect/actual, cinterop, or host Swift/Kotlin |
| Default visual strength | Apple conventions | Android / Material conventions | Consistent shared UI with deliberate adaptation | Material-oriented shared UI; iOS feel needs deliberate work |
| Feature-parity cost | Duplicate Android work | Duplicate iOS work | **Shared feature change** | Shared Compose change; platform bridges separately |
| Best use here | iOS reference | Android reference | **Preferred multi-platform solution** | Kotlin-native multi-platform reference |

Recommendation reflects delivery structure—not a claim that Flutter always looks more “native.” Single-platform products still favor SwiftUI or Compose.

## Code Quality & Literacy

All four stacks use declarative UI, but their code communicates intent differently:

- **SwiftUI** is concise and expressive. Modifier chains and property wrappers reduce visible ceremony, although state and environment dependencies can become implicit in larger views.
- **Jetpack Compose** makes state and events explicit through composable parameters and Kotlin functions. Its unidirectional data-flow model is readable when state is hoisted deliberately. [Compose state hoisting](https://developer.android.com/develop/ui/compose/state-hoisting)
- **Flutter** is more verbose, but its widget hierarchy, named parameters, and explicit `State` lifecycle make structure easy to trace. The same code remains visible to the iOS and Android implementations because they are one application.
- **KMP (Compose Multiplatform)** reuses the Compose literacy model in `commonMain`. Teams already fluent in Jetpack Compose can read the shared UI immediately; they still need expect/actual discipline for JVM-only APIs and platform controls.

For this small comparison, single-file (or single primary-file) implementations improve readability by keeping equivalent screens easy to find. If any version grows beyond demonstration scope, the next responsible step would be feature-based separation:

```text
feature/
  presentation/
  state/
  model/
```

That change should follow real complexity. Adding production architecture to this comparison before it is needed would obscure the framework differences the repository exists to demonstrate.

## Flutter vs. KMP

Both Flutter and KMP can ship one product surface to iOS and Android. This repository includes both so the trade-off is concrete, not theoretical.

| Area | Flutter | KMP (Compose Multiplatform) | Advantage in this repository |
|---|---|---|---|
| Primary language | Dart | Kotlin | **KMP** if the team is already Kotlin-centric; **Flutter** for a dedicated multi-platform stack |
| Shared UI model | Flutter widgets | Compose Multiplatform | Comparable reuse; different ecosystems |
| Tooling cohesion | One Flutter SDK (analyze, test, DevTools, hot reload) | Gradle + Android Studio / Xcode + CMP plugins | **Flutter** for fewer moving parts |
| Targets in this repo | iOS, Android, plus web/desktop runners | iOS + Android | **Flutter** for broader in-repo reach |
| Platform APIs | Plugins / channels / FFI | expect/actual / cinterop / host code | Comparable; both need boundaries |
| iOS UI runtime | Flutter engine | Compose Multiplatform (Skia) | Measure both; Flutter is the default recommendation here |
| Learning path from Android Compose | New (Dart + Flutter) | Incremental (shared Compose) | **KMP** for Compose-only teams |
| Evidence in this repo | Full sample + CI analyze/test | Full sample + CI Android assemble; iOS local | Both demonstrated |

### When to prefer Flutter

- You need a **general** multi-platform default (mobile now, web/desktop later) without committing to Kotlin.
- You want one SDK for project creation, analysis, testing, and hot reload.
- You want the product UI stack independent of Android’s Compose ecosystem choices.

### When to prefer KMP

- The organization is already standardized on **Kotlin + Jetpack Compose** for Android.
- Sharing Compose UI (and Kotlin domain code) to iOS is more valuable than adopting Dart.
- You accept Xcode + expect/actual + Compose-on-iOS trade-offs as the cost of that reuse.

For a greenfield product without a Kotlin constraint, **Flutter remains the better general multi-platform choice** in this comparison. KMP is the right alternative when Kotlin reuse is the strategic priority.

## Flutter vs. React Native

Flutter and React Native both reduce duplicate iOS and Android feature work, support native integrations, and require the underlying Apple and Android toolchains for release delivery. Their main difference is architectural: Flutter owns the UI framework and rendering pipeline, while React Native applies the React programming model to native host components.

Modern React Native must be evaluated through its New Architecture. Fabric, TurboModules, JSI, and Hermes replace the historical bridge-based model; describing current React Native only as a slow JavaScript bridge is inaccurate. [React Native New Architecture](https://reactnative.dev/architecture/landing-page)

| Area | Flutter | React Native | Advantage |
|---|---|---|---|
| Primary language | Dart with one framework-level type and widget model | JavaScript or TypeScript with React | **Flutter** for a cohesive application stack; React Native for teams already centred on React |
| Rendering model | Framework widgets are laid out, composited, and painted by Flutter using Impeller on supported targets | React components produce native host views through Fabric | **Flutter** for predictable cross-platform rendering and visual consistency |
| UI consistency | Same widget implementation and rendering behaviour across platforms | Native host components can expose platform-specific behaviour and differences | **Flutter** when identical product design and behaviour are priorities |
| Native appearance | Material, Cupertino, adaptive widgets, or a custom design system | Native host components naturally inherit more platform behaviour | React Native for native-control defaults; **Flutter** for controlled consistency |
| Runtime model | Dart development VM; mobile release code compiled to native machine code | JavaScript commonly runs on Hermes and communicates through the New Architecture/JSI | **Flutter** for a more vertically integrated runtime and rendering path |
| Development tooling | One Flutter SDK provides project creation, package resolution, analysis, formatting, testing, DevTools, and hot reload | Metro, package-manager tooling, React Native/Expo tooling, and native build systems | **Flutter** for a more unified first-party workflow |
| Platform support | First-party support for iOS, Android, web, macOS, Windows, and Linux | Core emphasis on iOS and Android; frameworks and out-of-tree projects extend reach | **Flutter** for one officially documented multi-platform toolkit |
| Native integration | Plugins, platform channels, FFI, and embedded native views | Native Modules, Native Components, Codegen, and platform-specific files | Comparable; both can reach Swift, Objective-C, Kotlin, Java, and C/C++ boundaries |
| Dependency strategy | Core UI, rendering, testing, and tooling are delivered as one coordinated SDK | Production projects commonly adopt a framework such as Expo plus ecosystem packages | **Flutter** for fewer architectural choices before feature development begins |
| Performance profile | Direct framework rendering and native-compiled mobile release code provide a predictable baseline | New Architecture removes the legacy bridge and substantially improves native/JavaScript interoperability | **Flutter** for rendering-heavy consistency; measure both on the real critical journey |
| Existing web expertise | Requires learning Dart and Flutter's widget model | Reuses React, JavaScript/TypeScript, and related organizational knowledge | React Native for established React teams |

Flutter's architectural advantage comes from vertical integration. The framework controls widgets, layout, text, animation, compositing, rendering, diagnostics, and testing. Mobile release code is compiled to machine code, while platform embedders provide access to operating-system services. This produces a more consistent execution and UI model across iOS and Android. [Flutter architectural overview](https://docs.flutter.dev/resources/architectural-overview)

React Native's strongest advantage is organizational reuse: teams with substantial React and TypeScript experience can apply familiar component, state, and package patterns to mobile development. Its native-component model can also be attractive when platform-specific controls should remain visibly different. React Native itself recommends using a framework such as Expo for most new applications, which improves the starting experience but adds another framework and release-tooling decision. [React Native setup guidance](https://reactnative.dev/docs/0.82/environment-setup)

### Why Flutter is the better general multi-platform solution

For a new application without a pre-existing React or Kotlin constraint, Flutter is the stronger default when you need to **develop and deploy across platforms**:

- **One product surface:** one widget tree for the interface you ship to each target.
- **Cohesive tooling:** the Flutter SDK owns analysis, formatting, testing, packages, debugging, and builds.
- **Broader first-party reach:** documented mobile, web, and desktop targets as one suite. [Flutter supported platforms](https://docs.flutter.dev/reference/supported-platforms)
- **Predictable rendering:** Flutter owns layout and painting instead of host-component drift.
- **Focused native escape hatches:** channels, plugins, FFI, and native views for OS-specific needs. [Flutter platform integration](https://docs.flutter.dev/platform-integration/platform-channels)
- **Lower parity risk:** shared widgets, state, navigation, and tests cut iOS/Android drift.

React Native remains credible for React-heavy orgs. KMP remains credible for Kotlin/Compose-heavy orgs. Flutter is still the better **general multi-platform choice** for one coherent codebase, consistent behaviour, integrated tooling, and reach beyond two mobiles.

In this repository the evidence is direct: Flutter owns the full sample surface in one Dart app with the broadest host coverage; KMP owns the same sample in shared Compose; there is no React Native port.

## Conclusion

Clear platform strategy:

- **SwiftUI** — iOS-only product.
- **Jetpack Compose** — Android-only product.
- **Flutter** — default when multiple platforms must be developed and deployed (especially iOS + Android; web/desktop available from the same toolkit).
- **KMP** — Kotlin-native multi-platform alternative when shared Compose UI and Kotlin reuse are the priority.

For general multi-platform delivery, Flutter consolidates behaviour, UI, state, and most tests into one codebase. KMP shows that Kotlin can do the same for Compose UI, with different tooling and iOS trade-offs. SwiftUI and Compose-only apps stay valuable as native references. When a shared stack needs a native API, add the smallest platform boundary—do not rebuild the whole feature twice.

See [CODEBASE_COMPARISON.md](CODEBASE_COMPARISON.md) for the detailed technical assessment.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

# iOS, Android & Flutter Comparison

A focused comparison of the same simple application implemented with SwiftUI, Jetpack Compose, and Flutter.

This repository is not a production application or starter template. Its purpose is to make the engineering trade-offs visible: native platform fidelity versus shared implementation, duplicated work versus feature parity, and platform-specific tooling versus one cross-platform workflow.

> **Recommendation:** use SwiftUI for an iOS-only application, Jetpack Compose for an Android-only application, and **Flutter when the same application must support both iOS and Android**.

## Project Overview

The three implementations reproduce the same small set of interactive examples: gradients, forms, animations, task lists, adaptive grids, navigation, and a tips sheet.

| Implementation | Location | Language | UI framework | Role in this repository |
|---|---|---|---|---|
| Native iOS | [`iosSwiftUITest/Test/`](iosSwiftUITest/Test/) | Swift | SwiftUI | Apple-platform reference |
| Native Android | [`AndroidProject/`](AndroidProject/) | Kotlin | Jetpack Compose with Material 3 | Android-platform reference |
| Cross-platform | [`flutterProject/flutter_conversation_project/`](flutterProject/flutter_conversation_project/) | Dart | Flutter with Cupertino widgets | Preferred shared iOS/Android implementation |

Primary source files:

- [`ContentView.swift`](iosSwiftUITest/Test/Test/ContentView.swift)
- [`SamplesApp.kt`](AndroidProject/app/src/main/java/com/example/androidfromios/SamplesApp.kt)
- [`main.dart`](flutterProject/flutter_conversation_project/lib/main.dart)

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

### Flutter

Requirements: a Flutter SDK compatible with the package's Dart `^3.9.2` constraint and the toolchain for the selected target.

```sh
cd flutterProject/flutter_conversation_project
flutter pub get
flutter analyze
flutter devices
flutter run -d <device-id>
```

Run the same sample on iOS and Android to evaluate shared behaviour and platform-specific differences from one Dart implementation.

## Notes

- The SwiftUI implementation uses native Apple controls, navigation, previews, and accessibility APIs.
- The Compose implementation uses Material 3 and Navigation Compose. Some screen scaffolding opts into `ExperimentalMaterial3Api`.
- The Flutter implementation uses Cupertino widgets because the original sample follows an iOS-oriented visual language. Flutter can also use Material widgets or an application-specific design system.
- The native Android sample targets API 36 with `minSdk` 33. The native iOS sample declares an iOS 26 deployment target. These values describe this repository, not general framework requirements.
- The Flutter directory contains mobile, web, and desktop runners. This comparison recommends Flutter specifically for shared iOS and Android delivery; the additional runners are outside the demonstrated scope.
- State is local and in memory. The repository does not compare networking, persistence, authentication, dependency injection, or large-scale modular architecture.
- Product-level automated coverage is not part of the current comparison. A successful build confirms compilation, not complete parity or release readiness.

Current upstream context, checked 27 July 2026: Flutter documentation reflects Flutter 3.44.7 and lists Android API 24-37 and iOS 13-26 as supported framework deployment ranges. Application targets can be stricter. [Flutter supported platforms](https://docs.flutter.dev/reference/supported-platforms) · [Flutter release notes](https://docs.flutter.dev/release/release-notes)

## Comparison Takeaways

### Native iOS: strongest Apple-platform specialization

SwiftUI provides direct Apple-framework access, familiar iOS behaviour, Xcode previews, and native accessibility integration. It is the most direct choice when iOS is the only required platform.

### Native Android: strongest Android-platform specialization

Jetpack Compose provides direct Android and AndroidX integration, Kotlin-first development, Material 3, and Android-specific lifecycle and tooling support. It is the most direct choice when Android is the only required platform.

### Flutter: strongest combined iOS and Android solution

When both mobile platforms are required, Flutter offers the best overall trade-off for this comparison:

- One shared feature implementation instead of parallel Swift and Kotlin implementations.
- One primary language and application structure for UI and state.
- Faster feature-parity work because behaviour changes are implemented once.
- A consistent rendering and testing surface across iOS and Android.
- Native extension points through plugins, platform channels, and host code when platform-specific capability is required.
- Hot reload for rapid UI iteration while preserving the current application state. [Flutter hot reload](https://docs.flutter.dev/tools/hot-reload)

Flutter does not remove native responsibilities. iOS and Android signing, store delivery, device testing, accessibility validation, and some SDK integrations still require platform knowledge. The advantage is that native code becomes a focused integration boundary rather than a second complete product implementation.

## Platform Comparison

| Aspect | SwiftUI | Jetpack Compose | Flutter |
|---|---|---|---|
| Primary target | Apple platforms | Android | iOS, Android, and additional supported targets |
| Language | Swift | Kotlin | Dart |
| UI ownership | Native Apple framework | Native Android framework | Flutter rendering framework |
| App implementations needed for iOS + Android | Two native implementations together with Compose | Two native implementations together with SwiftUI | **One shared implementation** |
| Product-code reuse across iOS + Android | Low without a separate sharing strategy | Low without a separate sharing strategy | **High by default** |
| Native API access | Direct | Direct | Plugins, platform channels, or host code |
| Default visual strength | Apple conventions | Android and Material conventions | Consistent shared UI with deliberate platform adaptation |
| Feature-parity cost | Duplicate Android implementation required | Duplicate iOS implementation required | **Shared feature change** |
| Best use in this repository | iOS reference | Android reference | **Preferred dual-platform solution** |

The recommendation is based on delivery structure, not a claim that Flutter always produces better platform-specific UI. Native frameworks retain the advantage when one platform is the entire product or when platform-specific behaviour is the principal differentiator.

## Code Quality & Literacy

All three frameworks use declarative UI, but their code communicates intent differently:

- **SwiftUI** is concise and expressive. Modifier chains and property wrappers reduce visible ceremony, although state and environment dependencies can become implicit in larger views.
- **Jetpack Compose** makes state and events explicit through composable parameters and Kotlin functions. Its unidirectional data-flow model is readable when state is hoisted deliberately. [Compose state hoisting](https://developer.android.com/develop/ui/compose/state-hoisting)
- **Flutter** is more verbose, but its widget hierarchy, named parameters, and explicit `State` lifecycle make structure easy to trace. The same code remains visible to the iOS and Android implementations because they are one application.

For this small comparison, single-file implementations improve readability by keeping equivalent screens easy to find. If any version grows beyond demonstration scope, the next responsible step would be feature-based separation:

```text
feature/
  presentation/
  state/
  model/
```

That change should follow real complexity. Adding production architecture to this comparison before it is needed would obscure the framework differences the repository exists to demonstrate.

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

### Why Flutter is the better general cross-platform solution

For a new application without a pre-existing React constraint, Flutter offers the stronger default:

- **More consistent UI:** one widget tree produces the product interface across platforms.
- **More cohesive tooling:** the Flutter SDK owns analysis, formatting, testing, package management, debugging, and build orchestration.
- **Broader first-party reach:** Flutter documents supported mobile, web, and desktop deployment targets as one platform suite. [Flutter supported platforms](https://docs.flutter.dev/reference/supported-platforms)
- **More predictable rendering:** Flutter controls layout and painting instead of depending on differences between host-component implementations.
- **Focused native escape hatches:** platform channels, plugins, FFI, and native views cover capabilities that should remain platform-specific. [Flutter platform integration](https://docs.flutter.dev/platform-integration/platform-channels)
- **Lower parity risk:** shared widgets, state, navigation, and tests reduce the chance that iOS and Android implementations drift.

React Native remains a credible solution, especially for organizations already invested in React or products that intentionally prefer native host controls. Flutter is nevertheless the better **general-purpose cross-platform choice** when the goal is one coherent codebase, consistent behaviour, integrated tooling, and support beyond two mobile platforms.

For this repository, the decision is stronger: the Flutter implementation already expresses the complete comparison surface in one Dart application, while no React Native implementation exists. Flutter is therefore both the better general cross-platform recommendation and the better evidence-backed choice for the sample.

## Conclusion

The comparison leads to a clear platform strategy:

- Choose **SwiftUI** when only iOS must be supported.
- Choose **Jetpack Compose** when only Android must be supported.
- Choose **Flutter** when both iOS and Android must be supported.

For dual-platform delivery, Flutter is the recommended solution because it consolidates product behaviour, UI, state, and most testing into one codebase. SwiftUI and Compose remain valuable reference implementations for native conventions and platform-specific validation. When Flutter needs a native capability, add the smallest required Swift or Kotlin integration instead of maintaining the entire feature twice.

See [CODEBASE_COMPARISON.md](CODEBASE_COMPARISON.md) for the detailed technical assessment.

# Codebase Comparison

## SwiftUI vs. Jetpack Compose vs. Flutter vs. KMP

**Assessment scope:** the four simple demo implementations in this repository.

**Decision context:** choosing one implementation strategy for iOS, Android, or both.

**Recommendation:** use native UI for a single-platform product; use **Flutter when you must develop and deploy across multiple platforms** (especially iOS + Android). Use **KMP** when a Kotlin-first team wants shared Compose UI instead.

## Executive Summary

This repository implements the same small interaction set four times:

- [SwiftUI](iosSwiftUITest/Test/) for native iOS.
- [Jetpack Compose](AndroidProject/) for native Android.
- [Flutter](flutterProject/flutter_conversation_project/) for the recommended shared multi-platform codebase.
- [Kotlin Multiplatform](KmpProject/shared/) for a Kotlin-native shared Compose Multiplatform codebase.

The examples cover navigation, gradients, forms, animation, task management, an adaptive grid, and a tips sheet. They use local in-memory state and deliberately simple file structures. This is a framework comparison, not a production architecture benchmark.

The decision is straightforward:

| Required platforms | Recommended implementation | Architectural reason |
|---|---|---|
| iOS only | **SwiftUI** | Direct Apple-framework access and native iOS conventions |
| Android only | **Jetpack Compose** | Direct Android/AndroidX access and native Android conventions |
| Multiple platforms (iOS + Android, optional web/desktop) | **Flutter** | One shared feature implementation, one primary language, one parity surface |
| Multi-platform, Kotlin-first with shared Compose UI | **KMP** | One shared Kotlin UI tree; expect/actual for platform APIs |

When multiple platforms are required, **Flutter remains the default** here: integrated tooling, optional web/desktop runners in the Flutter package, and a single Dart UI stack. **KMP** ([`KmpProject/`](KmpProject/)) is the Kotlin-native alternative—shared Compose in `commonMain`—but it adds iOS runtime weight and platform bridging compared with the Flutter port for the same sample surface. Two native apps (SwiftUI + Compose) still mean duplicate product implementations.

## Scope and Method

This assessment compares:

- Source organization.
- UI and navigation models.
- State ownership.
- Cross-platform code reuse.
- Platform fidelity.
- Native integration options.
- Accessibility and test evidence visible in the repository.
- Maintenance implications when the same feature must ship on both mobile platforms.

This assessment does **not** claim:

- Production readiness.
- Measured performance superiority.
- Equal visual fidelity on every device.
- Complete accessibility coverage.
- Release validation.
- A universal framework winner independent of required platforms.

No device, performance, or end-to-end benchmark was run for this document. Repository claims below come from inspected source and configuration.

## Repository Baseline

| Area | SwiftUI | Jetpack Compose | Flutter | KMP (Compose MP) |
|---|---|---|---|---|
| Primary source | [`ContentView.swift`](iosSwiftUITest/Test/Test/ContentView.swift) | [`SamplesApp.kt`](AndroidProject/app/src/main/java/com/example/androidfromios/SamplesApp.kt) | [`main.dart`](flutterProject/flutter_conversation_project/lib/main.dart) | [`SamplesApp.kt`](KmpProject/shared/src/commonMain/kotlin/com/example/kmpsamples/SamplesApp.kt) |
| Primary source size | 638 lines | 1,260 lines | 1,478 lines | ~1,220 lines (`commonMain`) |
| Language | Swift | Kotlin | Dart | Kotlin |
| UI system | SwiftUI | Material 3 | Cupertino | Material 3 (shared) |
| Navigation | `NavigationStack` | `NavHost` / `NavController` | `CupertinoApp` / `CupertinoPageRoute` | `NavHost` (shared) |
| Local state | `@State` | `remember`, `rememberSaveable`, state lists | `StatefulWidget`, `setState`, `AnimationController` | Same Compose patterns as Android reference |
| Modal presentation | `.sheet` | `ModalBottomSheet` | `showCupertinoModalPopup` | `ModalBottomSheet` |
| Explicit accessibility evidence | Labels and identifiers on key elements | Content descriptions on interactive icons | Semantics on home demo tiles and tips entry | Material content descriptions |
| Product behaviour tests | None found | Template tests only | Widget smoke test | None found |
| CI coverage (Ubuntu) | Not in CI (local Xcode) | `:app:assembleDebug` | `analyze` + `test` | `:androidApp:assembleDebug` |

Line counts describe the current files; they are not a quality score. Flutter's primary file is the largest individual file, but the equivalent SwiftUI and Compose files total 1,898 lines. More importantly, a real dual-platform native solution requires ongoing changes in two implementations, while Flutter and KMP each keep the shared feature in one place—with different tooling and host stories.

### Configuration Snapshot

- The SwiftUI reference declares an iOS 26 deployment target.
- The Android project uses `compileSdk = 36`, `targetSdk = 36`, and `minSdk = 33`.
- The Android project pins Kotlin 2.0.21 and Compose BOM 2024.09.00.
- Navigation Compose is declared once through the version catalogue.
- The Flutter package uses Dart `^3.9.2`.
- Flutter host runners exist for mobile, web, and desktop. Primary evaluation is shared iOS + Android; extra runners support the multi-platform delivery argument.
- KMP lives in [`KmpProject/`](KmpProject/) (Compose Multiplatform 1.7.x, Kotlin 2.0.21, Android `minSdk` 24). Shared UI is in `commonMain`; reminder pickers use `expect`/`actual`. CI builds `:androidApp:assembleDebug`; iOS uses `iosApp` + Xcode locally. Prefer JDK 17 or 21 for Gradle.

These are repository settings, not recommended minimums for every application.

## Implementation Analysis

### SwiftUI

The SwiftUI implementation is the most concise expression of the iOS-oriented sample. It benefits from native controls, Apple navigation semantics, Xcode previews, SF Symbols, and direct accessibility modifiers.

**Strengths**

- Lowest friction for Apple-specific APIs and behaviours.
- Concise view composition through modifiers.
- Native platform styling and interaction conventions.
- Direct Xcode preview and instrumentation workflow.

**Trade-offs**

- Solves only the Apple side of an iOS-and-Android requirement.
- Requires a separate Android implementation and parity process.
- Property wrappers and environment dependencies can make state ownership less visible as views grow.

**Best use here:** native iOS reference and validation of Apple-platform behaviour.

### Jetpack Compose

The Compose implementation expresses the same samples through Kotlin, Material 3, and Android navigation. State is generally visible near the composables that consume it through `remember` and `rememberSaveable`.

**Strengths**

- Direct access to Android and AndroidX APIs.
- Kotlin-first, declarative UI model.
- Strong Material 3 integration.
- Explicit state/event flow and Android lifecycle tooling.

**Trade-offs**

- Solves only the Android side of an iOS-and-Android requirement.
- Requires a separate iOS implementation and parity process.
- Some Material 3 APIs used by the demo are experimental.

**Best use here:** native Android reference and validation of Android-platform behaviour.

### Flutter

The Flutter implementation reproduces the complete sample in one Dart application. It uses Cupertino widgets to stay close to the iOS-oriented reference, while the same code runs through Flutter’s iOS and Android embedders (with web/desktop runners available in-package).

**Strengths**

- One implementation for multi-platform delivery—not two native product trees.
- Shared UI, state, animation, navigation, and product logic.
- Feature changes do not require parallel Swift and Kotlin rewrites.
- Consistent widget and testing model across targets.
- Hot reload supports rapid UI iteration while preserving current state. [Flutter hot reload](https://docs.flutter.dev/tools/hot-reload)
- First-party path beyond two mobiles when product scope grows. [Flutter supported platforms](https://docs.flutter.dev/reference/supported-platforms)
- Native capability remains available through plugins, platform channels, and host code.

**Trade-offs**

- Platform conventions must be designed deliberately rather than assumed.
- iOS and Android release toolchains are still required.
- Plugin quality and native SDK compatibility must be evaluated for platform-specific features.
- Accessibility coverage should keep expanding beyond the home catalogue as screens grow.

**Best use here:** authoritative implementation when the product must develop and deploy on multiple platforms.

### Kotlin Multiplatform (Compose Multiplatform)

The KMP port mirrors the Jetpack Compose sample in `commonMain`, hosted by `androidApp` and `iosApp`. Platform-specific reminder pickers live behind `expect`/`actual` (`ReminderPicker.*.kt`). Date/time helpers use `kotlinx-datetime` so `commonMain` stays free of JVM-only `java.time` / `String.format` APIs.

**Strengths**

- One Kotlin UI implementation for Android and iOS.
- Natural fit for teams already standardized on Kotlin and Compose on Android.
- Shared navigation, state, and widgets with the same Material 3 vocabulary as the Android reference.
- Thin hosts: Android `MainActivity` and a SwiftUI shell that hosts `ComposeUIViewController`.

**Trade-offs**

- iOS ships Compose Multiplatform runtime cost and platform adaptation work.
- Platform APIs still need expect/actual or native bridges (date pickers in this demo).
- No web/desktop runner in this repository’s KMP module (Flutter includes those hosts in-package).
- Gradle + Xcode + JDK version constraints (17/21 recommended) are more fragmented than the Flutter SDK workflow.
- iOS CI is local Xcode only, same as the SwiftUI reference.

**Best use here:** compare a Kotlin-native multi-platform strategy against Flutter and against two native apps.

## Architecture and State

All four implementations are intentionally screen-centric and keep state in memory. For a comparison demo, this is a strength: equivalent behaviour is easy to find without navigating production layers.

| Concern | SwiftUI | Jetpack Compose | Flutter | KMP (Compose MP) |
|---|---|---|---|---|
| Ephemeral state | `@State` | `remember` / `rememberSaveable` | widget-local `State` | `remember` / `rememberSaveable` in `commonMain` |
| State-driven rendering | View recomputation | Recomposition | Widget rebuild | Recomposition |
| Animation ownership | View modifiers and state | Compose animation APIs and coroutine scopes | `AnimationController` and animated widgets | Compose animation APIs (shared) |
| Shared feature state if the demo grows | Observable model | State holder or `ViewModel` | Controller, notifier, BLoC, or Cubit | Shared state holder / ViewModel-style types in common |
| Side-effect responsibility | Explicit task/lifecycle ownership | Lifecycle-aware coroutine ownership | Explicit async lifecycle and controller disposal | Coroutines in common; platform scopes at edges |

Declarative UI does not eliminate architecture. It changes how state becomes UI. The same core rule applies to every stack: keep state close to its owner, expose immutable values where practical, and make events and side effects explicit.

Compose formally recommends hoisting state to the lowest common reader and writer. The equivalent principle applies to SwiftUI, Flutter, and shared Compose Multiplatform. [Compose state-hoisting guidance](https://developer.android.com/develop/ui/compose/state-hoisting)

No feature-layer extraction is necessary for the current demo. If persistence, networking, or authentication is added later, split by feature only when the added complexity justifies it:

```text
feature/
  presentation/
  state/
  model/
```

Adding domain, repository, and dependency-injection layers before the comparison needs them would make the examples harder to understand without improving the decision.

## UI, Navigation, and Platform Fidelity

### Native implementations

SwiftUI and Compose naturally inherit their platform's UI vocabulary:

- SwiftUI follows Apple navigation, control, typography, and accessibility conventions.
- Compose follows Android and Material conventions.

That native fidelity is their strongest advantage when only one platform matters.

### Flutter implementation

Flutter owns a consistent widget and rendering layer. The current implementation uses Cupertino widgets, so it intentionally looks and behaves closer to the SwiftUI reference.

For a real iOS-and-Android product, Flutter can follow either strategy:

1. **Adaptive interface:** use platform-appropriate navigation, controls, input behaviour, and interaction conventions.
2. **Shared product design:** use one branded design system while adapting accessibility, input, navigation, and platform policies.

Flutter recommends separating device capabilities from product policies as applications grow. This is more maintainable than scattering platform checks through UI code. [Flutter capabilities and policies](https://docs.flutter.dev/ui/adaptive-responsive/capabilities)

The need for platform adaptation does not weaken the Flutter recommendation. It is focused design work inside one product implementation, not a requirement to maintain two complete applications.

### KMP implementation

Compose Multiplatform shares a Material-oriented UI tree. On Android it feels close to the Jetpack Compose reference; on iOS it still renders through Compose (not SwiftUI controls). Platform-native widgets (for example system date pickers) are not automatic—this demo bridges them with `expect`/`actual`.

Fidelity strategy for a real KMP product is the same fork as Flutter: adapt deliberately, or ship one branded design system. The difference is ecosystem: KMP reuses Compose skills; Flutter reuses the Flutter widget model.

## Cross-Platform Delivery Economics

For a feature that must exist on both iOS and Android:

```text
Native strategy:
  product change
    -> SwiftUI implementation
    -> Compose implementation
    -> two review paths
    -> two parity checks

Flutter strategy:
  product change
    -> shared Flutter implementation
    -> iOS and Android validation
    -> native integration only where required

KMP strategy:
  product change
    -> shared Compose (commonMain)
    -> iOS and Android validation
    -> expect/actual or host code where required
```

Flutter and KMP both reduce duplicate product UI work. Neither halves every cost: platform QA, signing, store delivery, permissions, and native SDK verification remain platform-specific. The architectural gain is a single source of truth for most feature behaviour.

| Maintenance concern | Two native implementations | Flutter | KMP |
|---|---|---|---|
| Feature implementation | Repeated in Swift and Kotlin | Shared in Dart | Shared in Kotlin Compose |
| Behaviour parity | Reconciled after each change | Shared by construction for common code | Shared by construction for `commonMain` |
| UI review | Separate code paths | Shared widget path plus platform validation | Shared Compose path plus platform validation |
| Native integrations | Direct in each app | Isolated behind plugin/channel boundaries | Isolated behind expect/actual or hosts |
| Platform release work | Required twice | Still required twice | Still required twice |
| Primary product ownership | Split between two apps | Consolidated in one app | Consolidated in shared module + thin hosts |
| Extra targets (web/desktop) in this repo | N/A | Present as Flutter hosts | Not included |

This consolidation is why a shared stack beats two native apps for multi-platform work. **Flutter is preferred here as the general default**; KMP is preferred when Kotlin/Compose reuse is the organizational constraint.

## Accessibility and Testing

The current repository is useful for source comparison but does not prove complete accessibility or behavioural parity.

- SwiftUI includes explicit accessibility labels and identifiers on key content.
- Compose includes content descriptions for interactive icons, but custom semantics coverage is limited.
- Flutter includes explicit `Semantics` on key interactive controls in the primary source; continue expanding coverage as samples grow.
- KMP relies on Material content descriptions similar to the Android Compose port; expand semantics as screens grow.
- Android contains template unit and instrumented tests; no product-flow tests were found.
- Flutter includes a widget smoke test; no deep product-flow widget tests were found.
- No SwiftUI or KMP product tests were found.

A fair comparison should eventually exercise the same observable journeys:

1. Open each sample from the catalogue.
2. Change gradient and form controls.
3. Run and reset the animation.
4. Complete, reset, and reorder task data.
5. Open and dismiss the tips sheet.
6. Verify grid behaviour across compact and wide layouts.

Accessibility validation should include VoiceOver, TalkBack, text scaling, focus order, contrast, touch targets, and reduced motion. Compose semantics support both assistive technologies and UI testing; Flutter provides equivalent semantics and widget-test APIs. [Compose accessibility](https://developer.android.com/develop/ui/compose/accessibility) · [Flutter testing](https://docs.flutter.dev/testing)

These gaps are limitations of the demo, not evidence that one framework is inherently inaccessible or untestable.

## Performance and Tooling

No repository benchmark supports a universal performance ranking. All four stacks can deliver responsive applications when state, layout, animation, and resource work are implemented well.

| Stack | Primary iteration and profiling tools |
|---|---|
| SwiftUI | Xcode previews, Instruments, SwiftUI performance analysis |
| Jetpack Compose | Android Studio previews, Layout Inspector, profiler, Macrobenchmark |
| Flutter | Hot reload, DevTools, profile/release-mode device measurement |
| KMP | Android Studio / IntelliJ KMP tooling, Compose previews where supported, Xcode for iOS host, Gradle |

Flutter compiles mobile releases to native machine code and owns the scene composition pipeline. Compose Multiplatform also renders through its own runtime on iOS (Skia-based). Real performance still needs measurement on representative devices for either shared stack. [Flutter architectural overview](https://docs.flutter.dev/resources/architectural-overview) · [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/)

Performance is therefore a validation responsibility, not a reason to maintain duplicate native product implementations without measured evidence.

## Decision Matrix

| Decision factor | SwiftUI | Jetpack Compose | Flutter | KMP (Compose MP) |
|---|---|---|---|---|
| iOS-only delivery | **Best fit** | Not applicable | Viable, but adds a cross-platform layer | Viable, but adds a cross-platform layer |
| Android-only delivery | Not applicable | **Best fit** | Viable, but adds a cross-platform layer | Viable; overlaps heavily with Compose-only |
| Multi-platform develop & deploy (general) | Requires Compose counterpart | Requires SwiftUI counterpart | **Best fit** | Strong alternative |
| Multi-platform, Kotlin/Compose team | Requires Compose counterpart | Requires SwiftUI counterpart | Viable (learn Dart) | **Best fit** |
| Shared feature implementation across platforms | Not by itself | Not by itself | **Yes** | **Yes** (Compose UI) |
| Native platform access | Direct | Direct | Plugins/channels/host code | expect/actual / cinterop / hosts |
| Default platform fidelity | Apple-native | Android-native | Requires deliberate adaptation | Material-shared; iOS needs deliberate adaptation |
| Feature-parity effort | High across two apps | High across two apps | **Low for shared code** | **Low for shared Compose** |
| Web/desktop in this repo | No | No | **Yes** (hosts present) | No |
| Repository role | Native iOS reference | Native Android reference | **Preferred multi-platform implementation** | Kotlin-native multi-platform reference |

### Final Decision

Use:

- **SwiftUI** when the requirement is iOS only.
- **Jetpack Compose** when the requirement is Android only.
- **Flutter** when the requirement is to develop and deploy on multiple platforms without a Kotlin constraint.
- **KMP** when the requirement is multi-platform **and** the team’s strategic stack is Kotlin + Compose.

If a Flutter or KMP feature needs a platform-specific API, add the smallest required Swift or Kotlin integration behind a clear boundary. Do not duplicate the complete feature in both native applications unless a measured platform constraint makes the shared stack unsuitable.

## Current Ecosystem Snapshot

Official documentation changes frequently, so release facts belong in a dated snapshot rather than the architectural recommendation.

- **Checked 27 July 2026:** Flutter documentation reflects Flutter 3.44.7. Its supported-platform table lists Android API 24-37 and iOS 13-26 for the framework. Repository deployment targets are stricter. [Flutter supported platforms](https://docs.flutter.dev/reference/supported-platforms) · [Flutter release notes](https://docs.flutter.dev/release/release-notes)
- SwiftUI remains Apple's declarative UI framework with previews, accessibility APIs, performance tooling, and UIKit/AppKit interoperability. [SwiftUI documentation](https://developer.apple.com/documentation/SwiftUI)
- Current Compose guidance emphasizes state hoisting, unidirectional data flow, lifecycle-aware collection, semantics, and UI testing. [Compose UI architecture](https://developer.android.com/develop/ui/compose/architecture) · [Compose state](https://developer.android.com/develop/ui/compose/state)
- Compose Multiplatform extends Compose UI beyond Android; this repository pins CMP 1.7.x with Kotlin 2.0.21. [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/) · [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)

## Recommended Repository Posture

This repository should continue to present all four implementations because side-by-side source is its purpose.

- Treat SwiftUI as the native iOS reference.
- Treat Jetpack Compose as the native Android reference.
- Treat Flutter as the recommended solution for multi-platform development and deployment.
- Treat KMP as the Kotlin-native multi-platform reference (Compose Multiplatform).
- Keep equivalent demo scenarios recognizable across all four ports.
- Document deliberate platform differences instead of forcing visual identity.
- Avoid leftover template branding in any port.
- Avoid adding production infrastructure unless it demonstrates a framework trade-off relevant to the comparison.

The native ports explain what platform specialization looks like. The Flutter and KMP ports demonstrate two ways to own the same product surface once and deliver it to both mobile platforms—with Flutter as the general default and KMP as the Kotlin-path alternative.

## Conclusion

SwiftUI is strongest for iOS-only. Jetpack Compose is strongest for Android-only. When multiple platforms must be developed and deployed, **Flutter is the strongest overall solution here** for a general multi-platform strategy: one shared codebase, cohesive tooling, and broader host coverage in this repository.

**KMP is the strongest Kotlin-native alternative** for the same sample surface: shared Compose in `commonMain`, thin Android/iOS hosts, and expect/actual at platform edges. Choose it when Kotlin reuse outweighs adopting Dart.

That recommendation is not based on Flutter being universally more native, faster, or simpler in every situation. It is based on architecture and delivery economics for the common case: one feature source, one state model, one primary test surface, and focused native integration where necessary. The SwiftUI and Compose implementations remain valuable as native references; Flutter should be the default shared implementation for multi-platform work unless the team’s constraint is Kotlin + Compose.

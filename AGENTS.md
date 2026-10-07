# Repository Guidelines

## Project Structure & Module Organization

- Flutter source lives in `flutterProject/flutter_conversation_project/`; primary UI logic is in `lib/main.dart`, with platform runners under `android/`, `ios/`, and additional hosts (web/desktop) outside the core comparison scope.
- Place new Flutter tests in `flutterProject/flutter_conversation_project/test/` using mirrored directory names from `lib/` (e.g., `lib/features/tasks/` → `test/features/tasks/`).
- SwiftUI (`iosSwiftUITest/Test/`) and Jetpack Compose (`AndroidProject/`) are **native reference ports** for parity checks.
- Kotlin Multiplatform lives in `KmpProject/` (shared Compose UI in `shared/src/commonMain/`). Prefer new shared multi-platform feature work in Flutter unless the change is KMP-specific.
- Store shared design assets in `flutterProject/flutter_conversation_project/assets/` and register paths in `pubspec.yaml`.

## Build, Test, and Development Commands

- `flutter pub get` — install or update Dart dependencies defined in `pubspec.yaml`.
- `flutter run -d <device>` — launch on iOS, Android, or another Flutter target.
- `flutter analyze` — static analysis; fix every reported issue before opening a PR.
- `flutter test` — execute automated unit/widget tests in the `test/` tree.
- `cd AndroidProject && ./gradlew :app:assembleDebug` — native Android reference build.
- `cd KmpProject && ./gradlew :androidApp:assembleDebug` — KMP Android host build (JDK 17 or 21).
- KMP iOS: open `KmpProject/iosApp/iosApp.xcodeproj` and build with Xcode (embeds shared framework via Gradle).

## Coding Style & Naming Conventions

- Use Dart’s default formatting (`dart format .`); commit only formatted code.
- Follow the Cupertino-oriented sample styling already established in `lib/main.dart` unless a change intentionally demonstrates Material or adaptive UI; prefer composition over deep inheritance.
- Name classes and enums in `UpperCamelCase`, functions and local variables in `lowerCamelCase`, files in `snake_case.dart`.
- Keep widget build methods concise; extract helpers into private widgets or functions when they exceed ~80 lines.
- Prefer adaptive colors (`CupertinoColors.label`, `secondaryLabel`) over hardcoded black/white.
- Add `Semantics` (or equivalent) on interactive controls so Flutter accessibility stays competitive with native ports.
- In KMP `commonMain`, avoid JVM-only APIs (`java.time`, `String.format`); use kotlinx-datetime and multiplatform helpers. Put platform pickers behind `expect`/`actual`.

## Comparison Narrative

- Docs and UI copy should present Flutter as the default when **multiple platforms** must be developed and deployed.
- Present KMP as the **Kotlin-native multi-platform alternative** (Compose Multiplatform)—credible for Kotlin/Compose teams, not equal to Flutter as the general default.
- Native ports stay valuable for single-platform fidelity; do not weaken that framing, but do not position them as equal multi-platform strategies.
- Keep demo scenarios recognizable across all four implementations (SwiftUI, Compose, Flutter, KMP).
- Avoid leftover template branding (e.g. “SwiftUI Samples” inside the Flutter app, “A new Flutter project.”, “My application” in KMP hosts).
- Keep README.md and CODEBASE_COMPARISON.md aligned when adding or changing a port.

## Testing Guidelines

- Write widget tests with `testWidgets` for interactive screens; mock platform channels with `MethodChannel` test bindings when needed.
- Naming: align test file names with the source file (`samples_home_page_test.dart` for `samples_home_page.dart`).
- Aim for meaningful assertions (UI state, navigation, animations); avoid snapshot-only tests.
- Run `flutter test --coverage` before merging major features and ensure coverage does not regress.

## Commit & Pull Request Guidelines

- Use concise, imperative commit messages (`Add task list swipe handling`). Group related changes in a single commit when practical.
- PRs must include: problem summary, implementation notes, testing evidence (`flutter analyze`, `flutter test`), and screenshots for UI updates.
- For KMP-only changes, include `./gradlew :androidApp:assembleDebug` (and note iOS local verification when UI/host code changes).
- Reference related issues with `Fixes #ID` when applicable, and call out follow-up work in a checklist or bullet list.

# Repository Guidelines

## Project Structure & Module Organization

- Flutter source lives in `flutterProject/flutter_conversation_project/`; primary UI logic is in `lib/main.dart`, with platform runners under `android/`, `ios/`, and additional hosts (web/desktop) outside the core comparison scope.
- Place new Flutter tests in `flutterProject/flutter_conversation_project/test/` using mirrored directory names from `lib/` (e.g., `lib/features/tasks/` → `test/features/tasks/`).
- SwiftUI (`iosSwiftUITest/Test/`) and Jetpack Compose (`AndroidProject/`) are **native reference ports** for parity checks. Prefer new shared feature work in Flutter.
- Store shared design assets in `flutterProject/flutter_conversation_project/assets/` and register paths in `pubspec.yaml`.

## Build, Test, and Development Commands

- `flutter pub get` — install or update Dart dependencies defined in `pubspec.yaml`.
- `flutter run -d <device>` — launch on iOS, Android, or another Flutter target.
- `flutter analyze` — static analysis; fix every reported issue before opening a PR.
- `flutter test` — execute automated unit/widget tests in the `test/` tree.

## Coding Style & Naming Conventions

- Use Dart’s default formatting (`dart format .`); commit only formatted code.
- Follow the Cupertino-oriented sample styling already established in `lib/main.dart` unless a change intentionally demonstrates Material or adaptive UI; prefer composition over deep inheritance.
- Name classes and enums in `UpperCamelCase`, functions and local variables in `lowerCamelCase`, files in `snake_case.dart`.
- Keep widget build methods concise; extract helpers into private widgets or functions when they exceed ~80 lines.
- Prefer adaptive colors (`CupertinoColors.label`, `secondaryLabel`) over hardcoded black/white.
- Add `Semantics` (or equivalent) on interactive controls so Flutter accessibility stays competitive with native ports.

## Comparison Narrative

- Docs and UI copy should present Flutter as the default when **multiple platforms** must be developed and deployed.
- Native ports stay valuable for single-platform fidelity; do not weaken that framing, but do not position them as equal multi-platform strategies.
- Keep demo scenarios recognizable across all three implementations.
- Avoid leftover template branding (e.g. “SwiftUI Samples” inside the Flutter app, “A new Flutter project.”).

## Testing Guidelines

- Write widget tests with `testWidgets` for interactive screens; mock platform channels with `MethodChannel` test bindings when needed.
- Naming: align test file names with the source file (`samples_home_page_test.dart` for `samples_home_page.dart`).
- Aim for meaningful assertions (UI state, navigation, animations); avoid snapshot-only tests.
- Run `flutter test --coverage` before merging major features and ensure coverage does not regress.

## Commit & Pull Request Guidelines

- Use concise, imperative commit messages (`Add task list swipe handling`). Group related changes in a single commit when practical.
- PRs must include: problem summary, implementation notes, testing evidence (`flutter analyze`, `flutter test`), and screenshots for UI updates.
- Reference related issues with `Fixes #ID` when applicable, and call out follow-up work in a checklist or bullet list.

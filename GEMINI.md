<!--
Author: Feryael Justice
Date: 2025-11-08
-->
# GEMINI.md

## Project Overview

MiraiLink is an Android dating/social app built with Kotlin and Jetpack Compose. The app features real-time chat via Socket.IO, user matching, profile management, encrypted data storage, and Firebase integration for analytics.

**Architecture**: Clean Architecture (Data/Domain/UI layers)
**Package**: `com.feryaeljustice.mirailink`
**Min SDK**: 26 (Android 8.0) | **Target SDK**: 36

## Interaction Guide for Gemini

As an AI assistant, your primary role is to understand the project's structure and assist in development tasks. Use the tools provided by your environment to read, analyze, and modify files. All actions must be guided by the user's requests and conform to the project's established patterns.

### Analyzing the Project

- **Understand the architecture**: The project follows Clean Architecture. Before making changes, identify whether your task belongs to the `data`, `domain`, or `ui` layer.
- **Consult existing files**: Use `read_file` to understand the implementation details of a specific component or screen.
- **Search for patterns**: Use `grep` or `code_search` to find examples of how similar features are implemented.

### Making Changes

- **Targeted modifications**: When asked to modify a file, use `write_file` for small changes or `replace_text` for larger ones. Always confirm the file path and the exact changes required.
- **Follow code style**: Adhere to the existing Kotlin and Jetpack Compose conventions. Use Hilt for dependency injection and follow the Atomic Design pattern for UI components.
- **Create new features**: When adding a new feature, follow the steps outlined in the "Common Tasks" section. Create files in the appropriate directories and follow the established data flow.

### Validation and Testing

- **Build and test**: After making changes, use the `gradle_build` tool to run builds and tests. Start with local unit tests (`testDebugUnitTest`) before moving to full builds (`assembleDebug`).
- **Analyze results**: If a build or test fails, carefully analyze the output to identify the cause. Use `read_file` to inspect the problematic code and `write_file` to apply corrections.
- **Iterate**: Continue this cycle of building, testing, and fixing until all validations pass.

## Build and Test Commands (for `gradle_build` tool)

- **Run unit tests**: `testDebugUnitTest`
- **Run instrumented tests**: `connectedDebugAndroidTest`
- **Run lint checks**: `lintDebug`
- **Build a debug version**: `assembleDebug`

## Key Development Patterns

### Data Flow

1.  **UI (`screens`)**: Triggers actions in the ViewModel.
2.  **ViewModel**: Calls a `usecase` from the `domain` layer.
3.  **UseCase**: Orchestrates one or more `repository` calls.
4.  **Repository (`data` layer)**: Fetches data from a `datasource` (remote or local).
5.  **Data Flow Back**: Data is mapped and flows back to the UI to be displayed.

### Security Practices

- Store sensitive data in the encrypted DataStore.
- Use the `AuthInterceptor` for network requests.
- Validate user input in both ViewModels and UseCases.

## Common Tasks

### Adding a New Feature

1.  **Domain**: Create models in `domain/model/` and define repository interfaces in `domain/repository/`.
2.  **UseCase**: Create a use case in `domain/usecase/`.
3.  **Data**: Implement the repository in `data/repository/` and add API services in `data/remote/` if needed.
4.  **UI**: Build UI components in `ui/components/` and the screen with its ViewModel in `ui/screens/`.
5.  **Navigation**: Wire up the new screen in `ui/navigation/`.

# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

MiraiLink is an Android dating/social app built with Kotlin and Jetpack Compose. The app features real-time chat, user matching, profile management, and social interactions.

**Package**: `com.feryaeljustice.mirailink`
**Min SDK**: 26 (Android 8.0)
**Target SDK**: 36

## Architecture

The project follows **Clean Architecture** with three main layers:

### Data Layer (`data/`)

- **DataSources**: Remote API calls and local data storage
- **Repositories**: Implementation of domain repository contracts
- **Models**: DTOs and data transfer objects
- **Mappers**: Convert between DTOs and domain models
- **Remote**: API services and network configuration
- **DataStore**: Encrypted preferences and session management using AndroidX DataStore

### Domain Layer (`domain/`)

- **Models**: Core business entities (User, Chat, Match, etc.)
- **Repositories**: Abstract repository contracts
- **Use Cases**: Business logic encapsulation (auth, chat, matching, etc.)
- **Utils**: Core utility functions

### UI Layer (`ui/`)

- **Screens**: Compose screens with ViewModels
- **Components**: Follows **Atomic Design Pattern**:
  - **Atoms**: Basic UI elements (buttons, text fields, cards)
  - **Molecules**: Combinations of atoms (gender selector, date picker)
  - **Organisms**: Complex components (user cards, chat lists)
- **Navigation**: Navigation setup and screen definitions (navigation 3)

## Key Technologies

- **UI**: Jetpack Compose with Material 3
- **DI**: Koin
- **Networking**: Retrofit + OkHttp with custom auth interceptor
- **Real-time**: Socket.IO for chat functionality
- **Storage**: Encrypted DataStore with AES-GCM encryption
- **Images**: Coil for image loading
- **Analytics**: Firebase Analytics and Crashlytics
- **Monetization**: Google Ads integration
- **Security**: Two-factor authentication, encrypted local storage
- **Testing**: Koin for testing, Kotzilla for journey testing

## Common Development Commands

### Building

```powershell
# Debug build
.\gradlew.bat assembleDebug

# Release build
.\gradlew.bat assembleRelease

# Generate signed AAB for Play Store
.\gradlew.bat bundleRelease
```

### Testing

```powershell
# Run unit tests
.\gradlew.bat testDebugUnitTest

# Run instrumented tests
.\gradlew.bat connectedDebugAndroidTest

# Run all tests
.\gradlew.bat test
```

### Code Quality

```powershell
# Run lint checks
.\gradlew.bat lintDebug

# Generate lint report
.\gradlew.bat lint

# Clean project
.\gradlew.bat clean
```

### Development Tasks

```powershell
# Install debug APK
.\gradlew.bat installDebug

# Uninstall debug APK
.\gradlew.bat uninstallDebug

# List all tasks
.\gradlew.bat tasks
```

## Important Configuration Files

- **`keystore.properties`**: Release signing configuration (not in repo)
- **`google-services.json`**: Firebase configuration
- **`gradle/libs.versions.toml`**: Centralized dependency management
- **`proguard-rules.pro`**: Code obfuscation rules for release builds

## Key Features & Components

### Authentication System

- Login/Registration with validation
- Two-factor authentication support
- Auto-login functionality
- Password reset flow

### Chat System

- Real-time messaging with Socket.IO
- Private and group chat support
- Message read status
- File/image sharing capabilities

### User Management

- Profile creation and editing
- Photo management (upload, delete, reorder)
- User matching and discovery
- Swipe functionality (like/dislike)

### Security Features

- Encrypted local data storage using Android Keystore
- Secure network communication
- Session management
- User content reporting system

## Development Notes

- The UI follows Atomic Design principles mentioned in README.md
- All network requests go through the auth interceptor for session management
- Socket connections are managed through a dedicated service
- Firebase is integrated for analytics and crash reporting
- The app supports deep linking for `mirailink.xyz` domain

## Data Flow Pattern

1. **UI Layer**: Compose screens trigger ViewModels
2. **ViewModels**: Call domain use cases
3. **Use Cases**: Orchestrate business logic, call repositories
4. **Repositories**: Fetch data from remote/local sources
5. **DataSources**: Make API calls or access local storage
6. **Mappers**: Convert between data and domain models

## Environment Requirements

- Android Studio with Kotlin support
- Java 11 (configured in gradle)
- Android SDK 26+
- Valid `keystore.properties` for release builds
- Firebase project setup for analytics/crashlytics

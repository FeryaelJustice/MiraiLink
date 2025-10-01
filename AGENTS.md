# AGENTS.md

## Project Overview

MiraiLink is an Android dating/social app built with Kotlin and Jetpack Compose. The app features real-time chat via Socket.IO, user matching, profile management, encrypted data storage, and Firebase integration for analytics.

**Architecture**: Clean Architecture (Data/Domain/UI layers)  
**Package**: `com.feryaeljustice.mirailink`  
**Min SDK**: 26 (Android 8.0) | **Target SDK**: 36

## Setup Commands

```powershell
# Clean and build debug
.\gradlew.bat clean assembleDebug

# Install debug APK to connected device
.\gradlew.bat installDebug

# Start development (requires Android Studio or connected device)
.\gradlew.bat installDebug && adb shell am start -n com.feryaeljustice.mirailink/.ui.MainActivity
```

## Build Commands

```powershell
# Debug build
.\gradlew.bat assembleDebug

# Release build (requires keystore.properties)
.\gradlew.bat assembleRelease

# Generate signed AAB for Play Store
.\gradlew.bat bundleRelease

# Clean build artifacts
.\gradlew.bat clean
```

## Testing Commands

```powershell
# Run unit tests
.\gradlew.bat testDebugUnitTest

# Run instrumented tests (requires device/emulator)
.\gradlew.bat connectedDebugAndroidTest

# Run lint checks
.\gradlew.bat lintDebug

# Run all tests
.\gradlew.bat test
```

## Code Style Guidelines

- **Language**: Kotlin with strict null safety
- **UI**: Jetpack Compose following Material 3 design system
- **Architecture**: Follow Clean Architecture layers (data/domain/ui)
- **Dependency Injection**: Use Hilt annotations (@Inject, @Module, @InstallIn)
- **Naming**: Use descriptive names, follow Android/Kotlin conventions
- **Components**: Follow Atomic Design Pattern:
  - Atoms: Basic UI elements (MiraiLinkButton, MiraiLinkTextField)
  - Molecules: Combinations of atoms (GenderSelector, BirthdateField)  
  - Organisms: Complex components (UserCard, ChatList)

## Project Structure

```
app/src/main/java/com/feryaeljustice/mirailink/
├── data/           # Data layer implementation
│   ├── datasource/ # Remote API calls
│   ├── repository/ # Repository implementations
│   ├── remote/     # API services and Socket.IO
│   ├── datastore/  # Encrypted preferences
│   └── mappers/    # DTO to domain model conversion
├── domain/         # Business logic layer
│   ├── model/      # Core business entities
│   ├── repository/ # Repository contracts
│   ├── usecase/    # Business logic encapsulation
│   └── util/       # Domain utilities
├── ui/             # Presentation layer
│   ├── screens/    # Compose screens + ViewModels
│   ├── components/ # Reusable UI components
│   └── navigation/ # Navigation setup
└── di/             # Hilt dependency injection modules
```

## Key Development Patterns

### Data Flow
1. UI triggers ViewModel actions
2. ViewModel calls Domain Use Cases
3. Use Cases orchestrate Repository calls
4. Repositories fetch from DataSources (Remote/Local)
5. Data flows back through mappers to UI

### Security Practices
- All sensitive data stored in encrypted DataStore
- Network requests go through AuthInterceptor
- Use Android Keystore for encryption keys
- Validate user input in ViewModels and Use Cases

### Compose Guidelines
- Use `@Composable` functions for UI elements
- Manage state with `remember`, `rememberSaveable`
- Use `LaunchedEffect` for side effects
- Follow Material 3 theming conventions

## Testing Instructions

### Unit Tests
- Test Use Cases with mocked repositories
- Test ViewModels with fake use cases
- Test mappers with sample data
- Run: `.\gradlew.bat testDebugUnitTest`

### Integration Tests  
- Test API integration with mock servers
- Test database operations
- Test navigation flows
- Run: `.\gradlew.bat connectedDebugAndroidTest`

### Before Committing
1. Run `.\gradlew.bat lintDebug` - Fix all lint issues
2. Run `.\gradlew.bat testDebugUnitTest` - Ensure unit tests pass
3. Verify build: `.\gradlew.bat assembleDebug`
4. Test on device if UI changes were made

## Important Files

- `build.gradle.kts`: App-level build configuration
- `gradle/libs.versions.toml`: Centralized dependency versions
- `keystore.properties`: Release signing (not in repo, required for releases)
- `google-services.json`: Firebase configuration
- `proguard-rules.pro`: Code obfuscation for release builds

## Common Tasks

### Adding New Features
1. Create domain models in `domain/model/`
2. Define repository interface in `domain/repository/`
3. Create use case in `domain/usecase/`
4. Implement repository in `data/repository/`
5. Add API service if needed in `data/remote/`
6. Create UI components in `ui/components/`
7. Build screen with ViewModel in `ui/screens/`
8. Wire up navigation in `ui/navigation/`

### Adding Dependencies
1. Add version to `gradle/libs.versions.toml`
2. Add library reference in `[libraries]` section
3. Use in `app/build.gradle.kts` with `implementation(libs.libraryName)`

### Database Changes
- Modify DataStore schema in `data/datastore/`
- Update mappers and serializers accordingly
- Consider migration strategy for existing users

## Security Considerations

- Never log sensitive user data (passwords, tokens, personal info)
- Use encrypted DataStore for all persistent user data
- Validate all network responses before processing
- Implement proper error handling to avoid data leaks
- Use ProGuard rules to obfuscate sensitive code in release builds

## Firebase Integration

- Analytics events should be tracked via `AnalyticsTracker`
- Crash reporting handled automatically via Crashlytics
- Test Firebase integration with debug builds before release

## Socket.IO Chat System

- Real-time messaging managed through `SocketService`
- Connection lifecycle tied to app lifecycle
- Handle connection errors gracefully
- Implement message queuing for offline scenarios

## Deployment Notes

- Release builds require valid `keystore.properties` file
- Generate AAB files for Play Store: `.\gradlew.bat bundleRelease`
- Test release builds on multiple devices before deployment
- Ensure all Firebase services are configured for production
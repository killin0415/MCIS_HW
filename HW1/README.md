# HW1: Building Layout & Floating Action Button

This directory contains the implementation for Homework 1.

## Objective

The primary goal of this assignment is to recreate the UI from the [Flutter Building Layout Tutorial](https://docs.flutter.dev/ui/layout/tutorial) using Native Android (Jetpack Compose) and to implement a Floating Action Button (FAB).

## Key Technical Points

### 1. Jetpack Compose Navigation 3 (Nav3)

This project adopts the brand-new **Navigation 3** (`androidx.navigation3`, version `1.1.0-beta01`), Google's latest navigation framework that replaces the traditional Navigation Component.

- **`NavDisplay` + `NavEntry`**: Declaratively maps routes to screens (`NavigationRoot.kt`)
- **`rememberNavBackStack`**: Manually manages the back stack via `backStack.add()` / `backStack.remove()` for navigation
- **`NavEntryDecorator`**: Uses `rememberSaveableStateHolderNavEntryDecorator()` and `rememberViewModelStoreNavEntryDecorator()` to decorate navigation entries with state preservation and ViewModel lifecycle management

### 2. Type-Safe Routes with Kotlinx Serialization

Routes are defined as a **Kotlin Sealed Interface** (`Route.kt`), combined with `@Serializable` and the `NavKey` interface:

```kotlin
@Serializable
sealed interface Route : NavKey {
    @Serializable data object Main : Route, NavKey
    @Serializable data class Edit(val content: String) : Route, NavKey
}
```

- Configures polymorphic serialization via `SavedStateConfiguration` with `SerializersModule`, enabling routes to be correctly restored after process death
- The `Edit` route carries a `content: String` parameter, achieving type-safe data passing between screens

### 3. Koin Dependency Injection

Uses **Koin 4.1.1+** (with BOM) as the dependency injection framework, integrated with a Nav3-specific extension (`koin-compose-navigation3-android:4.2.0-RC1`):

- **Application-level initialization**: Starts Koin in `MainApplication` via the `initKoin` function (`initKoin.kt`)
- **Module definition**: Uses the `viewModelOf(::ClassName)` syntactic sugar to automatically infer ViewModel constructor parameters (`Modules.kt`)
- **Parameterized ViewModel injection**: `EditProfileViewModel` requires a `content: String` parameter, injected at the navigation layer via `koinViewModel { parametersOf(key.content) }`

### 4. ResultStore — Cross-Screen Result Passing

A custom `ResultStore` class (`ResultStore.kt`) serves as a bridge for passing results between screens, replacing the traditional `setFragmentResult` pattern:

- Annotated with `@Stable` to ensure optimal Compose recomposition performance
- Survives **configuration changes** via `rememberSaveable` with a custom `Saver`
- `EditProfileScreen` writes the edited result via `resultStore.setResult("edit_content", ...)` on save
- `MainScreen` reads the result via `resultStore.getResult<String>("edit_content")` during recomposition and updates the ViewModel

### 5. MVI Architecture (Model-View-Intent)

The presentation layer follows the **MVI architecture** with unidirectional data flow for UI state management:

- **State**: Encapsulates all UI data in `data class` types (e.g., `MainState`, `EditState`)
- **Action / Intent**: Defines all user actions via `sealed interface` types (e.g., `MainAction`, `EditAction`)
- **ViewModel**: Exposes read-only state via `MutableStateFlow` + `asStateFlow()`, handling all actions in the `onAction()` method with a `when` expression
- **View**: Subscribes to state using `collectAsStateWithLifecycle()`, ensuring lifecycle-safe Flow collection


### 6. Material 3 & Dynamic Color

- Uses **Material 3** (`androidx.compose.material3`) as the UI component library
- Supports **Dynamic Color** (Android 12+): `Theme.kt` automatically selects `dynamicDarkColorScheme` / `dynamicLightColorScheme` based on the system version
- Defines both Dark and Light color schemes with automatic switching based on the system dark mode setting

### 7. Floating Action Button (FAB)

The FAB is placed in the `floatingActionButton` slot of `Scaffold` in `MainScreen`:

- Custom container color `Purple80.copy(0.8f)` and content color `Pink40`
- On click, navigates to `EditProfileScreen` carrying the current text content

## Project Structure

```
app/src/main/java/com/example/hw1/
├── MainActivity.kt              # Entry point, edge-to-edge setup
├── MainApplication.kt           # Application class, Koin initialization
├── core/ui/theme/               # Material 3 theme (Color, Type, Theme)
├── di/                          # Koin modules & initialization
├── domain/                      # Domain model (Profile data class)
├── navigation/                  # Nav3 routes, NavigationRoot, ResultStore
├── presentation/
│   ├── screens/                 # MainScreen, EditProfileScreen
│   └── viewmodels/              # MainViewModel, EditProfileViewModel (MVI)
└── utils/                       # Constants
```

## References

* [Android Nav3 Playlist](https://youtube.com/playlist?list=PLQkwcJG4YTCRjyfVKB8vcK7zeC1BNmBz4&si=pWZW4CLkrmzcTZeX)
* [Koin Nav3 Documentation](https://insert-koin.io/docs/reference/koin-compose/navigation3/)
* [Google Nav3 Example Repository](https://github.com/android/nav3-recipes)

> **Note:** This is the only assignment where the entire project directory is uploaded. For all subsequent homework submissions, only the `src` folder and `build.gradle.kts` file will be included.
# HW2 & HW3: Bookpedia with Voice Integration

This directory contains the combined implementation for **Homework 2** and **Homework 3**, along with **Creative Implementation** features.

## Objective

The goal of this project is to build a robust "Bookpedia" application using modern Android development practices. It covers UI navigation and lists (HW2), integrates voice-to-text and text-to-speech capabilities (HW3), and demonstrates creative extension through real-world API integration and local data persistence.

## Key Technical Points

### 1. Advanced Navigation with Nav3 (HW2)

This project utilizes the latest **Navigation 3** (`androidx.navigation3`) framework to manage complex screen flows.
- **`Navigator` & `NavigationState`**: A custom navigation wrapper that manages multiple backstacks concurrently.
- **Bottom Navigation**: Implements "Search" and "Favorites" tabs, each maintaining its own independent navigation history using `backStacks: Map<NavKey, NavBackStack<NavKey>>`.
- **State Preservation**: Uses `rememberSaveableStateHolderNavEntryDecorator` to ensure UI state is preserved when switching between tabs.

### 2. Voice Integration (HW3)

The application incorporates multi-modal interaction through custom Speech-to-Text (STT) and Text-to-Speech (TTS) services.
- **Voice Search (STT)**: Users can search for books by voice. The app uses `MediaRecorder` to capture audio, which is then base64-encoded and sent to a custom STT API (`/api/base64_recognition`) for transcription.
- **Voice Synopsis (TTS)**: A "Volume" button on the Book Detail screen allows the app to read the book's description. This is implemented via a direct TCP socket connection to a TTS server, receiving binary `.wav` data for local playback.

### 3. Creative Implementation & Persistence

Beyond the core requirements, several features were added to enhance the "Bookpedia" experience:
- **Open Library API**: The app performs real-time searches and fetches rich metadata (ratings, page counts, authors, and high-resolution covers) using the OpenLibrary REST API.
- **Local Favorites (Room Database)**: A local persistence layer implemented with **Room** and **KSP**.
    - **`FavoriteBookDatabase`**: Manages saved books.
    - **Offline Support**: Favorited books can be viewed even without an internet connection.
    - **Type Converters**: Handles complex data types like `List<String>` for author and language metadata.

### 4. Modern Tech Stack & Architecture

- **MVI (Model-View-Intent)**: Unidirectional data flow in the presentation layer, ensuring predictable UI states across `BookList`, `BookDetail`, and `Favorites`.
- **Clean Architecture**: Clear separation of concerns between `data`, `domain`, and `presentation` layers.
- **Ktor Client**: Handles all network requests with a centralized `HttpClientFactory` and `safeCall` wrapper for robust error handling.
- **Koin DI**: Modular dependency injection, including specialized support for Nav3 ViewModels and activity-scoped shared ViewModels (`SelectedBookViewModel`).
- **Coil**: Image loading with Ktor3 integration for optimized book cover rendering.

## Project Structure

```
app/src/main/main/java/com/example/hw2/
├── book/
│   ├── data/                 # Networking (Ktor), Database (Room), DTOs, Mappers
│   ├── domain/               # Core Models & Repository Interfaces
│   └── presentation/         # MVI ViewModels & Compose Screens (List, Detail, Favorites)
├── core/
│   ├── data/                 # STT/TTS Clients, Audio Management, HTTP Factory
│   ├── domain/               # Generic Result & Error types
│   └── presentation/         # UI Utilities & Shared Components
├── navigation/               # Nav3 specialized implementation (Navigator, Routes)
├── di/                       # Koin Module definitions
└── ui/theme/                 # Material 3 Design System
```

## References

* [CMP-Bookpedia (Philipp Lackner)](https://github.com/philipplackner/CMP-Bookpedia) - The primary architectural reference for this project.
* [Android Navigation 3 Recipes](https://github.com/android/nav3-recipes)
* [OpenLibrary API Documentation](https://openlibrary.org/developers/api)

---
*Developed for the "Multilingual and Crosslingual Information System" course.*

# GalaxyGram

GalaxyGram is an Android app built with Kotlin, Jetpack Compose, Hilt, and Retrofit.  
It connects to NASA’s Astronomy Picture of the Day (APOD) API and provides a feed of daily images with detailed information.

The project demonstrates modern Android development best practices:

- MVVM architecture with ViewModel and StateFlow  
- Hilt for dependency injection (built on top of Dagger 2)  
- Retrofit and Moshi for networking and JSON parsing  
- Jetpack Compose for declarative UI  
- Coil for image loading  
- Unit testing with JUnit, Coroutines Test, and Mockito  

---


## Features

- Browse NASA APOD feed in a scrollable grid  
- Image support with HD preview and external open option  
- Video support  
  - Direct MP4 playback with ExoPlayer  
  - Inline YouTube and Vimeo playback with WebView embeds  
  - Fallback external open for non-embeddable providers  
- View additional details including title, date, and explanation  
- Pull-to-refresh with error and loading handling  
- Repository pattern and MVVM with StateFlow  

---

## Tech Stack

- **Language:** Kotlin  
- **UI:** Jetpack Compose (Material 3, Compose BOM)  
- **Architecture:** MVVM + Repository Pattern  
- **Networking:** Retrofit, OkHttp, Moshi  
- **Asynchronous:** Kotlin Coroutines, StateFlow  
- **Dependency Injection:** Hilt  
- **Media:**  
  - Coil for image loading  
  - ExoPlayer (Media3) for MP4 playback  
  - WebView for YouTube, Vimeo, and APOD HTML embeds  
- **Persistence (planned/partial):** Room, Paging 3  
- **Testing:** JUnit, Coroutines Test, Mockito, MockWebServer  

---

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/Junaid2478/GalaxyGram.git

2. Obtain a NASA API key from api.nasa.gov and add it to local.properties
    ```bash
   NASA_API_KEY=your_key_here

3. Open the project in Android Studio and run it on a device or emulator (API 24+).

Purpose

This project was developed as a demonstration of clean Android architecture, API integration, and testing practices.
It can serve as a learning project, interview skeleton, or foundation for further exploration.

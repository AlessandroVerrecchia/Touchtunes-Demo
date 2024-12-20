Simple demo app to search albums by artist name.
-------------
-------------
**App**

 Responsibilities:
 - Handles the UI and user interactions.
 - Processes events and updates the UI state in response to changes.
 - Uses ViewModels to interact with the business logic through Domain use cases.

Dependencies:
 - Depends on the Domain module to access the SearchAlbumUseCase and Album model.

Key Components:
 - UI Layer: Composables, ViewModels, and event-driven interactions.
 - ViewState: Represents the UI state for album search.
 - Event: Represents one-time actions like showing toast messages or navigation.
-------------

**Domain (touchtunes)**

 Responsibilities:
 - Defines the core business logic and contracts (interfaces).
 - Abstracts the AlbumRepository, which defines data-fetching operations without coupling to implementation details.
 - Encapsulates use case logic, ensuring single-responsibility and testable business rules.
 - Models the core entities such as Album, which represent the application's core domain.

 Independence:
 - Abstracted from both the App and Data layers.
 - Contains no dependencies on UI frameworks, APIs, or data sources.

 Key Components:
 - Use Cases: Encapsulate business logic (SearchAlbumUseCase).
 - Models: Define domain models (e.g., Album) used across the app.
 - Repository Interfaces: Specify contracts for data operations (e.g., AlbumRepository).
-------------

**Data (itunes)**

 Responsibilities:
 - Implements the AlbumRepository interface defined in the Domain module.
 - Handles data-fetching operations from external sources (iTunes API).
 - Maps data models (DTOs) from the API to domain models.
 - Contains Retrofit services, network calls, and model mapping logic.

 Dependencies:
 - Depends on the Domain module for AlbumRepository and Album model definitions.

 Key Components:
 - Repository Implementations: E.g., ITuneAlbumRepository implementing AlbumRepository.
 - DTOs: Represent raw data from the API (AlbumDto, AlbumResponse).
 - Data Mappers: Convert DTOs to domain models (AlbumDto.toAlbum()).
-------------

**Notes**
- I had prior experience with Jetpack Compose, but I was still utilizing the MVVM pattern. This project provided a valuable opportunity to gain hands-on experience with the MVI architecture.
- I leveraged this as an opportunity to learn Hilt, transitioning from my previous use of Dagger and Koin.
- Additionally, I took this as a chance to explore Coil, moving away from my prior use of Glide.

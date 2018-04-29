h1. InstantAppImageBrowser
Instant App Image Browser (Flickr)

h2. Requirements:
* Home Screen [x]
* Grid Image Viewer / Browser (Flickr) [x]
** Single Search [x]
* Paging [x]
** Vertical paging in portrait [x]
** Horizontal paging in landscape [x]
* Base Unit tests [x]

h2. Optional:
* Image Captions (title) [x]
* Detail Image View [x]
* Tablet / Phone Multi screen [x]
* Paging [x]
* Infinite scroll Paging (load next page when reach end of current list) [x]
* Image Search (2nd feature?) [ ]

h2. Features:
* Instant App Architecture [x]
* MVP Architecture [x]

h2. Extra Features:
* Master-Detail [x]
* FeatureModuleManager (easily swap out views / presenters, etc [x]
* Pull to Refresh + Accessiblity [x]
* Navigation to and from home page [x]
* Reusable / Extensible [x]
* Debug Feature: Recent Item Browser + Default Search Browser [x]
* Espresso Tests [ ]
* Build.gradle extensions [x]
* Updated icons [x]

h2. Third Party Libraries:
* Retrofit (Networking)
* Okhttp3 (Networking)
* Moshi (Json parsing)
* Picasso (Images)
* Mockito-Kotlin (Unit testing helper for Kotlin)

h2. Future Considerations
* Smooth Scroll Paging (pre-load)
* Moving page cache (don't keep all the paged data for space reasons)
* Offline version + Google Room POC w/ Observables + MVVM

h2. Resources
* App icons generated with: https://romannurik.github.io/AndroidAssetStudio/icons-generic.html

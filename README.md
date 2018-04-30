# InstantAppImageBrowser
Instant App Image Browser (Flickr)

## Requirements:
- [x] Home Screen
- [x] Grid Image Viewer / Browser (Flickr)
- [x] Single Search
- [x] Paging
- [x] Vertical paging in portrait
- [x] Horizontal paging in landscape
- [x] Base Unit tests

## Optional:
- [x] Image Captions (title) 
- [x] Detail Image View
- [x] Tablet / Phone Multi screen
- [x] Paging
- [x] Infinite scroll Paging (load next page when reach end of current list) 
- [x] Image Search
- [x] Search Suggestions Feature (2nd Feature Module)

## Features:
- [x] Instant App Architecture
- [x] MVP Architecture

## Extra Features:
- [x] Master-Detail
- [x] FeatureModuleManager (easily swap out views / presenters, etc 
- [x] Pull to Refresh + Accessiblity
- [x] Navigation to and from home page 
- [x] Reusable / Extensible 
- [x] Debug Feature: Recent Item or Default Search  on empty search 
- [x] Build.gradle extensions
- [x] Updated icons 

## Third Party Libraries:
* Retrofit (Networking)
* Okhttp3 (Networking)
* Moshi (Json parsing)
* Picasso (Images)
* Mockito-Kotlin (Unit testing helper for Kotlin)

## Future Considerations
* Smooth Scroll Paging (pre-load)
* Moving page cache (don't keep all the paged data for space reasons)
* Offline version 
* Google Room POC w/ Observables + MVVM
	* Pre-search, update view
* Initial Fetch images animated progress screen
* Espresso tests

## Resources
* App icons generated with: https://romannurik.github.io/AndroidAssetStudio/icons-generic.html

## Architecture Notes
* Base Module:
	* Networking 
	* Data Models
	* FeatureModuleManager
* Feature Modules:
	* HomePage
	* Browser (Image Browser)
		* Includes Search
		* Search keyboard hides on scroll so still have context on what searching for
	* Search (Search / Suggestions Feature)
		* Search toolbar collapses if click outside of search view

## Notes
* If user rotates, search is cleared and default 'search' browser is loaded 

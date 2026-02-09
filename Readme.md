# Te Papa Collections

The Te Papa Collections app provides simple and straightforward
access to the official "Museum of New Zealand" online collections API.

All data is sourced in real-time from museum servers using the .json
format, then processed within the app using Kotlin Serialization APIs.

Search history, Favourites, and Settings are stored
locally in a Room database using official APIs.

For this app to function **fully**, you will need to provide **two**
API keys: one from [Te Papa Museum](https://data.tepapa.govt.nz/docs/),
and the other from [Google Maps](https://developers.google.com/maps/get-started)
# Famous Movies #
This is an app that uses [TheMovieDB.org][1]'s database. By their policy, the app needs an API key to make the requests, so **before trying to run the app, be sure to**:

1. Register on [TheMovieDB.org site][1]
2. On your profile's [API settings][2], request the API key
3. Follow the "Setting up" section below

### Setting up ###
After importing the project in Android Studio, open the local.properties file located on the repository's root folder and add the property below with your API key (be sure to include quotes):
> tmdb.apiKey="your API key here"

After this you can compile and run the app.

[1]: https://www.themoviedb.org/
[2]: https://www.themoviedb.org/settings/api
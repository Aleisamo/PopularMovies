package aleisamo.github.com.popular_movie_stage1;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class BuildUrl {

    private static final String LOG_TAG = BuildUrl.class.getSimpleName();
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_PARAM = "api_key";

    private final Context context;

    public BuildUrl(Context context) {
        this.context = context;
    }

    public URL buildMoviesUrl(String filterBy) throws MalformedURLException {
        Uri movieUrl = Uri.parse(BASE_URL).buildUpon()
                .appendPath(filterBy)
                .appendQueryParameter(API_PARAM, context.getString(R.string.MOVIEAPIKEY))
                .build();
        URL url = new URL(movieUrl.toString());
        Log.v(LOG_TAG, "Uribuild:" + url);
        return url;
    }

    public URL buildMovieUrl(String movieId, String extraDetail) throws MalformedURLException {
        //https://api.themoviedb.org/3/movie/343611/videos?api_key=APIKEY&append_to_reponse=reviews
        Uri movieTrailer = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(extraDetail)
                .appendQueryParameter(API_PARAM, context.getString(R.string.MOVIEAPIKEY))
                .build();

        return new URL(movieTrailer.toString());
    }

}

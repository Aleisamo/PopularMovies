package aleisamo.github.com.popular_movie_stage1;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieJson {

    private static final String LOG_TAG = MovieJson.class.getSimpleName();
    Context mContext;

    public MovieJson(Context context) {
        mContext = context;
    }

    final String RESULTS = "results";

    final String POSTER_PATH = "poster_path";
    final String SYNOPSIS = "overview";
    final String RELEASE_DAY = "release_date";
    final String TITLE = "title";
    final String POPULARITY = "popularity";
    final String VOTE = "vote_average";
    final String MOVIE_ID = "id";
    final String KEY = "key";
    final String NAME = "name";
    final String REVIEWS = "content";
    final String AUTHOR = "author";


    /**
     * Patterns:
     * - If we want to return more than one type of value, e.g. overview and title, create a class for this
     * <p>
     * - First, define the input and desired output, in this case String => Movie[]
     * - Then make the necessary changes to make it compile
     */
    public Movie[] getMovieDataFromJson(String moviesJson) {
        Movie[] movies = null;
        try {
            JSONObject result = new JSONObject(moviesJson);
            JSONArray movieJsonArray = result.getJSONArray(RESULTS);
            movies = new Movie[movieJsonArray.length()];

            for (int i = 0; i < movieJsonArray.length(); i++) {
                JSONObject movie = movieJsonArray.getJSONObject(i);
                String poster = movie.getString(POSTER_PATH);
                String overview = movie.getString(SYNOPSIS);
                String day = movie.getString(RELEASE_DAY);
                String title = movie.getString(TITLE);
                String average = movie.getString(VOTE);
                String id = movie.getString(MOVIE_ID);

                movies[i] = new Movie(poster, overview, day, title, average, id);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Failed to parse JSON", e);
        }
        return movies;
    }

    public MovieVideo[] getVideo(String movieJson) {
        MovieVideo[] movieVideos = null;

        try {
            JSONObject result = new JSONObject(movieJson);
            JSONArray movieJsonArray = result.getJSONArray(RESULTS);
            movieVideos = new MovieVideo[movieJsonArray.length()];
            for (int i = 0; i < movieJsonArray.length(); i++) {
                JSONObject jsonObject = movieJsonArray.getJSONObject(i);
                String key = jsonObject.getString(KEY);
                String name = jsonObject.getString(NAME);
                movieVideos[i] = new MovieVideo(key, name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieVideos;
    }

        public MovieReviews[] getReviews(String movieJson){
        MovieReviews[] movieReviews = null;

        try {
            JSONObject result = new JSONObject(movieJson);
            JSONArray movieJsonArray = result.getJSONArray(RESULTS);
            movieReviews = new MovieReviews[movieJsonArray.length()];
            for (int i = 0; i <movieJsonArray.length() ; i++) {
                JSONObject jsonObject = movieJsonArray.getJSONObject(i);
                String author = jsonObject.getString(AUTHOR);
                String content = jsonObject.getString(REVIEWS);
                movieReviews[i] = new MovieReviews(author,content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieReviews;

    }

}








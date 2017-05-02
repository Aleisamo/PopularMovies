package aleisamo.github.com.popular_movie_stage1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class FetchMovieReviews extends AsyncTask<String, Void, MovieReviews[]> {

    // Log check Class
    private static final String LOG_TAG = FetchMovieReviews.class.getSimpleName();

    private Context context;
    private ListView listView;
    private URL url;

    public FetchMovieReviews(Context context, ListView listView, URL url) {
        this.context = context;
        this.listView = listView;
        this.url = url;
    }

    @Override
    protected MovieReviews[] doInBackground(String... params) {

        // start Http request

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // read input stream in to String

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }
            if (stringBuffer.length() == 0) {
                return null;
            }

            movieJsonStr = stringBuffer.toString();
            Log.v(LOG_TAG, "MovieJson String:" + movieJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Check connection", e);
            return null;
        }
        MovieReviews[] reviews;
        try {
            reviews = new MovieJson(context).getReviews(movieJsonStr);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return reviews;
    }


    @Override
    protected void onPostExecute(final MovieReviews[] reviews) {
        if (reviews != null) {

            final ReviewsAdapter reviewAdapter = new ReviewsAdapter(context, Arrays.asList(reviews));
            listView.setAdapter(reviewAdapter);

        }
    }
}


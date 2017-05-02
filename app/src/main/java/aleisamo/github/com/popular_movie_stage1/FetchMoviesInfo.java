package aleisamo.github.com.popular_movie_stage1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class FetchMoviesInfo extends AsyncTask<String, Void, Movie[]> {

    // Log check Class
    private static final String LOG_TAG = FetchMoviesInfo.class.getSimpleName();

    private Context context;
    private GridView gridView;
    private URL url;

    // pass parameters within constructor later..
    public FetchMoviesInfo(Context context, GridView gridView, URL url) {
        this.context = context;
        this.gridView = gridView;
        this.url = url;
    }

    @Override
    protected Movie[] doInBackground(String... params) {

        // start Http request

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;


        //http://api.themoviedb.org/3/movie/top_rated?api_key=APIKEY

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
        Movie[] movie;
        try {
            movie = new MovieJson(context).getMovieDataFromJson(movieJsonStr);
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
        return movie;
    }

    @Override
    protected void onPostExecute(final Movie[] movies) {
        if (movies != null) {
            Log.v(getClass().getSimpleName(), "onPostExecute");
            final MovieAdapter movieAdapter = new MovieAdapter(context, Arrays.asList(movies));
            gridView.setAdapter(movieAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    final Movie item = movieAdapter.getItem(position);
                    Log.v(LOG_TAG,"item:"+item);
                    Intent movieDetail = new Intent(view.getContext(), MovieDetails.class);
                    movieDetail.putExtra("from","web");
                    movieDetail.putExtra("poster", item.getPosterPath());
                    movieDetail.putExtra("title", item.getTitle());
                    movieDetail.putExtra("overview", item.getOverview());
                    movieDetail.putExtra("date", item.getReleaseDate());
                    movieDetail.putExtra("average", item.getAverage());
                    movieDetail.putExtra("id", item.getId());
                    context.startActivity(movieDetail);
                }
            });
        }
    }
}


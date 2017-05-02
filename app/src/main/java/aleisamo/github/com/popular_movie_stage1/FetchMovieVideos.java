package aleisamo.github.com.popular_movie_stage1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class FetchMovieVideos extends AsyncTask<String, Void, MovieVideo[]> {

    // Log check Class
    private static final String LOG_TAG = FetchMovieVideos.class.getSimpleName();

    private Context context;
    private ListView listView;
    private URL url;

    public FetchMovieVideos(Context context, ListView listView, URL url) {
        this.context = context;
        this.listView = listView;
        this.url = url;
    }

    @Override
    protected MovieVideo[] doInBackground(String... params) {

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
        MovieVideo[] videos;
        try {
            videos = new MovieJson().getVideo(movieJsonStr);
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
        return videos;
    }


    @Override
    protected void onPostExecute(final MovieVideo[] videos) {
        if (videos != null) {

            final TrailerNameAdapter videoAdapter = new TrailerNameAdapter(context, Arrays.asList(videos));
            listView.setAdapter(videoAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    final MovieVideo item = videoAdapter.getItem(position);
                    Intent youtube = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getVideoPath()));
                    Intent trailerPath = new Intent(view.getContext(),DetailsFragment.class);
                    trailerPath.putExtra("trailer_path",item.getVideoPath());
                    context.startActivity(youtube);
                }
            });
        }
    }
}


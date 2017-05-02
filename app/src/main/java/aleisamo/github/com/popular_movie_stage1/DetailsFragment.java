package aleisamo.github.com.popular_movie_stage1;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import aleisamo.github.com.popular_movie_stage1.data.MovieContract.detailEntry;

public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;
    static final String DETAIL_URI = "URI_D";
    static final String VIDEOS = "videos";
    static final String REVIEWS = "reviews";

    private URL trailersUrl;
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mOverview;
    private TextView mDate;
    private TextView mVote;
    private TextView mTrailersTitle;
    private ListView mListTrailers;
    private TextView mReviewsTitle;
    private ListView mListReviews;
    private Button mAddToFavorite;
    private Intent movieDetails;
    private URL reviewsUrl;

    public DetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate view from xml
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        // get intent after click on movie poster and display details

        movieDetails = getActivity().getIntent();
        if (movieDetails != null) {
            final String movieId = movieDetails.getStringExtra("id");
            try {
                trailersUrl = new BuildUrl(getContext()).buildMovieUrl(movieId, VIDEOS);
                reviewsUrl = new BuildUrl(getContext()).buildMovieUrl(movieId, REVIEWS);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            // pass poster_path to picasso method
            final String movie_poster = movieDetails.getStringExtra("poster");
            final String movie_release_day = movieDetails.getStringExtra("date");
            final String movie_title = movieDetails.getStringExtra("title");
            final String movie_overView = movieDetails.getStringExtra("overview");
            final String movie_vote = movieDetails.getStringExtra("average");


            // set up ImageView and TextView using id from layout xml resources
            mPoster = (ImageView) rootView.findViewById(R.id.movie_posterDetails);
            mTitle = (TextView) rootView.findViewById(R.id.movie_title);
            mOverview = (TextView) rootView.findViewById(R.id.movie_overView);
            mDate = (TextView) rootView.findViewById(R.id.movie_date);
            mVote = (TextView) rootView.findViewById(R.id.movie_average);
            mTrailersTitle = (TextView) rootView.findViewById(R.id.trailers);
            Log.v(getClass().getSimpleName(), "trailerpath videos:" + trailersUrl.getPath().contains(VIDEOS));
            mListTrailers = (ListView) rootView.findViewById(R.id.list_video);
            mListTrailers.setOnTouchListener(new View.OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            mReviewsTitle = (TextView) rootView.findViewById(R.id.reviews);
            mListReviews = (ListView) rootView.findViewById(R.id.list_review);
            mListReviews.setOnTouchListener(new View.OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            if (hasNetworkAccess()) {
                new FetchMovieVideos(getContext(), mListTrailers, trailersUrl).execute();
                new FetchMovieReviews(getContext(), mListReviews, reviewsUrl).execute();
            }
            // set String from movie to TextView
            mTitle.setText(movie_title);
            mOverview.setText(movie_overView);
            mDate.setText(movie_release_day);
            mVote.setText("Average:  " + movie_vote + "/" + "10");

            // picasso to load the image and set into ImageView
            Picasso.with(getContext()).load(movie_poster).into(mPoster);

            // Action Button save movie detail in to DB
            mAddToFavorite = (Button) rootView.findViewById(R.id.favorite_movie);
            mAddToFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Picasso.with(getContext()).load(movie_poster).into(picassoImageMovieTarget(getContext(), "imageDir", movie_title + ".jpeg"));
                    ContextWrapper cw = new ContextWrapper(getContext());
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                    File movieImageFile = new File(directory, movie_title + ".jpeg");

                    Log.i("movieImage", "movieImage loaded from" + movieImageFile.getAbsolutePath());

                    savedMovieDetails(movieImageFile.getAbsolutePath(), movie_release_day, movie_title, movie_overView, movieId, movie_vote);
                }

            });
        }
        return rootView;
    }

    private boolean hasNetworkAccess() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        Log.v(getClass().getSimpleName(), "Netword info: " + netInfo);
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // save image in internal storage using picasso
    //image save  /data/data/com.your.app.package.path/app_imageDir/my_image.png

    private Target picassoImageMovieTarget(Context context, final String imageMovieDir, final String imageMovieName) {
        Log.d("picassoImageTarget", "picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageMovieDir, Context.MODE_PRIVATE);
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                final File imageMovieFile = new File(directory, imageMovieName);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(imageMovieFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("movieImage", "movieImage save to" + imageMovieFile.getAbsolutePath());
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }

    private void savedMovieDetails(String movie_poster, String movie_release_day, String movie_title, String movie_overView, String movie_id, String vote) {
        ContentValues movieDetails = new ContentValues();
        movieDetails.put(detailEntry.COLUMN_MOVIE_ID, movie_id);
        movieDetails.put(detailEntry.COLUMN_POSTER_PATH, movie_poster);
        movieDetails.put(detailEntry.COLUMN_RELEASE_DAY, movie_release_day);
        movieDetails.put(detailEntry.COLUMN_TITLE, movie_title);
        movieDetails.put(detailEntry.COLUMN_SYNOPSIS, movie_overView);
        movieDetails.put(detailEntry.COLUMN_VOTE, vote);

        // check if movie is on favorite list and add  toasts
        // when button is clicked
        String selection = detailEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectArgs = new String[]{movie_id};
        Cursor checkMovieId = getContext().getContentResolver().query(detailEntry.CONTENT_URI, null, selection, selectArgs, null, null);
        String movieId = "";
        if (checkMovieId.moveToNext()) {

            movieId = checkMovieId.getString(checkMovieId.getColumnIndex("id"));
            Log.v(getClass().getSimpleName(), "cursor:" + movieId);

        }
        if (movieId.equals(movie_id)) {

            Toast.makeText(getActivity(), "Already on the list", Toast.LENGTH_SHORT).show();
        } else {
            Uri newUri = getContext().getContentResolver().insert(detailEntry.CONTENT_URI, movieDetails);

            if (newUri == null) {
                Toast.makeText(getActivity(), "Error saving movie", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "movie saved successfully", Toast.LENGTH_SHORT).show();
            }
        }
        checkMovieId.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri uri = movieDetails.getData();
        String[] projection = {
                detailEntry.COLUMN_TITLE,
                detailEntry.COLUMN_POSTER_PATH,
                detailEntry.COLUMN_SYNOPSIS,
                detailEntry.COLUMN_RELEASE_DAY,
                detailEntry.COLUMN_VOTE
        };
        if (uri == null) {
            Uri mUri = detailEntry.buildMovieDetailUri(id);
            return new CursorLoader(getActivity(), mUri, projection, null, null, null);
        } else {

            Log.v(getClass().getSimpleName(), "uri:" + uri);
            return new CursorLoader(getActivity(), uri, projection, null, null, null);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {
            int col_index_title = data.getColumnIndex(detailEntry.COLUMN_TITLE);
            int col_index_poster_path = data.getColumnIndex(detailEntry.COLUMN_POSTER_PATH);
            int col_index_synopsis = data.getColumnIndex(detailEntry.COLUMN_SYNOPSIS);
            int col_index_date = data.getColumnIndex(detailEntry.COLUMN_RELEASE_DAY);
            int col_index_vote = data.getColumnIndex(detailEntry.COLUMN_VOTE);


            // extract values from the cursor

            String title = data.getString(col_index_title);
            String poster_path = data.getString(col_index_poster_path);
            Log.v(getClass().getSimpleName(), "poster:" + poster_path);
            String synopsis = data.getString(col_index_synopsis);
            String date = data.getString(col_index_date);
            String vote = data.getString(col_index_vote);

            // updated view

            Picasso.with(getContext()).load(new File(poster_path)).into(mPoster);
            mTitle.setText(title);
            mOverview.setText(synopsis);
            mDate.setText(date);
            mVote.setText("Average: " + vote + "/10");
            mAddToFavorite.setVisibility(View.GONE);
            mTrailersTitle.setVisibility(View.GONE);
            mListTrailers.setVisibility(View.GONE);
            mReviewsTitle.setVisibility(View.GONE);
            mListReviews.setVisibility(View.GONE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}

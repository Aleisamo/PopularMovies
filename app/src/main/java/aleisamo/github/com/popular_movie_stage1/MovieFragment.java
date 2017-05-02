package aleisamo.github.com.popular_movie_stage1;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import aleisamo.github.com.popular_movie_stage1.data.MovieContract;

public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MOVIE_LOADER = 0;

    static final String MOVIE_FRAGMENT_URI = "URI";

    private GridView posterMovies;
    private MovieCursorAdapter mMovieCursorAdapter;

    public MovieFragment() {
        // Required empty public constructor
    }

    //TODO implement savedInstanceState for the activity lifecycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_stage1_, container, false);
        mMovieCursorAdapter = new MovieCursorAdapter(getActivity(), null);
        posterMovies = (GridView) rootView.findViewById(R.id.movieGrid);

        try {
            String selectedMenu = getContext()
                    .getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE)
                    .getString("selectedMenu", "popular");
            loader(selectedMenu);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void loader(String menuOption) throws MalformedURLException {
        getContext().getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE)
                .edit().putString("selectedMenu", menuOption)
                .apply();
        if (menuOption.equals(getString(R.string.most_popular)) || menuOption.equals(getString(R.string.top_rate))) {
            URL url = new BuildUrl(getContext()).buildMoviesUrl(menuOption);
            new FetchMoviesInfo(getActivity(), posterMovies, url).execute(menuOption);
        }
        // Fetch data from favoriteList DB change null by a cursor but how to do it
        else {
            Log.v(getClass().getSimpleName(), "menuOption=" + menuOption);
            posterMovies.setAdapter(mMovieCursorAdapter);
            posterMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent movieDetails = new Intent(getActivity(), MovieDetails.class);
                    movieDetails.putExtra("from", "uri");
                    Uri currentMovieUri = ContentUris.withAppendedId(MovieContract.detailEntry.CONTENT_URI, id);
                    Log.v(getClass().getSimpleName(), "Uri data:" + currentMovieUri);
                    movieDetails.setData(currentMovieUri);
                    startActivity(movieDetails);
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                try {
                    loader(getString(R.string.most_popular));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.top_rate:
                try {
                    loader(getString(R.string.top_rate));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.favorite_movie:
                try {
                    loader(getString(R.string.favorite));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieContract.detailEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        Log.v(getClass().getSimpleName(), "onLoadFinished");
        Log.v(getClass().getSimpleName(), "data:" + Arrays.toString(data.getColumnNames()));
        mMovieCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //mMovieCursorAdapter.swapCursor(null);
    }

    public interface CallBack {
    }
}

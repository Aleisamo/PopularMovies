package aleisamo.github.com.popular_movie_stage1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static aleisamo.github.com.popular_movie_stage1.data.MovieContract.detailEntry.buildMovieDetailUri;

public class MovieProvider extends ContentProvider {

    private static final String LOG_TAG = MovieProvider.class.getSimpleName();
    public static final int MOVIE_POSTER = 100;
    public static final int MOVIE_DETAILS_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieDbHelper;

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        Cursor movieCursor;

        int match = sUriMatcher.match(uri);

        switch (match) {

            case MOVIE_POSTER:
                projection = new String[]{MovieContract.detailEntry._ID,
                        MovieContract.detailEntry.COLUMN_MOVIE_ID,
                        MovieContract.detailEntry.COLUMN_POSTER_PATH,
                        MovieContract.detailEntry.COLUMN_RELEASE_DAY,
                        MovieContract.detailEntry.COLUMN_TITLE,
                        MovieContract.detailEntry.COLUMN_SYNOPSIS,
                        MovieContract.detailEntry.COLUMN_VOTE};
                        //MovieContract.detailEntry.COLUMN_TRAILER};
                movieCursor = db.query(MovieContract.detailEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_DETAILS_ID:
                projection = new String[]{MovieContract.detailEntry._ID,
                        MovieContract.detailEntry.COLUMN_MOVIE_ID,
                        MovieContract.detailEntry.COLUMN_POSTER_PATH,
                        MovieContract.detailEntry.COLUMN_RELEASE_DAY,
                        MovieContract.detailEntry.COLUMN_TITLE,
                        MovieContract.detailEntry.COLUMN_SYNOPSIS,
                        MovieContract.detailEntry.COLUMN_VOTE};
                       // MovieContract.detailEntry.COLUMN_TRAILER};
                selection = MovieContract.detailEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                movieCursor = db.query(MovieContract.detailEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknow URI" + uri);
        }
        movieCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return movieCursor;
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_POSTER:
                return MovieContract.detailEntry.CONTENT_TYPE;
            case MOVIE_DETAILS_ID:
                return MovieContract.detailEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown Uri" + uri + match);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri = null;
        switch (match) {
            case MOVIE_POSTER: {
                long id = db.insert(MovieContract.detailEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = buildMovieDetailUri(id);
                } else
                    //returnUri= buildMovieDetailUri(db.insertWithOnConflict(MovieContract.detailEntry.TABLE_NAME,null,values,SQLiteDatabase.CONFLICT_REPLACE));
                    Log.e(LOG_TAG, "Failed insert: " + uri);
                break;
            }
            // check if i need to do MOVIE_DETAILS_ID
            default:
                throw new UnsupportedOperationException("unknow uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }



    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int deleteRow;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_POSTER:
                deleteRow = db.delete(MovieContract.detailEntry.TABLE_NAME, selection, selectionArgs);
                if (deleteRow != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return deleteRow;
            case MOVIE_DETAILS_ID:
                selection = MovieContract.detailEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deleteRow = db.delete(MovieContract.detailEntry.TABLE_NAME, selection, selectionArgs);
                if (deleteRow != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return deleteRow;
            default:
                throw new IllegalArgumentException("Deletion failed for " + uri);

        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int updateRow;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_POSTER:
                updateRow = db.update(MovieContract.detailEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MOVIE_DETAILS_ID:
                selection = MovieContract.detailEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                updateRow = db.update(MovieContract.detailEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Updated Failed for " + uri);
        }
        if (updateRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateRow;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_DETAIL, MOVIE_POSTER);
        matcher.addURI(authority, MovieContract.PATH_DETAIL + "/#", MOVIE_DETAILS_ID);
        return matcher;

    }
}

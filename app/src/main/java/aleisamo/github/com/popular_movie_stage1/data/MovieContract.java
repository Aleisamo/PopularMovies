package aleisamo.github.com.popular_movie_stage1.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final  String CONTENT_AUTHORITY ="aleisamo.github.com.popular_movie_stage1";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_DETAIL = "detail";

    // define table contents

    public static final class detailEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_URI.buildUpon().appendPath(PATH_DETAIL).build();
        // multiples rows
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DETAIL;
        // specific row
         public static final String CONTENT_ITEM_TYPE =
                 ContentResolver.ANY_CURSOR_ITEM_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DETAIL;

        // table name
        public static  final String TABLE_NAME = "detail";

        // column names

        public static  final String COLUMN_MOVIE_ID = "id";
        public static  final String COLUMN_TITLE = "title";
        public static  final String COLUMN_POSTER_PATH = "poster_path";
        public static  final String COLUMN_SYNOPSIS = "overview";
        public static  final String COLUMN_RELEASE_DAY = "release_date";
        public static  final String COLUMN_VOTE = "vote_average";
        //public static  final String COLUMN_TRAILER = "trailer";

        // check utility of builMovieDetailUri
        public static Uri buildMovieDetailUri (long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }
}

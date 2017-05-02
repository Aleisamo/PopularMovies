package aleisamo.github.com.popular_movie_stage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import aleisamo.github.com.popular_movie_stage1.data.MovieContract.detailEntry;

public class MovieDbHelper extends SQLiteOpenHelper{

    // data base version
    private static final int DB_VERSION = 1;
    static final String DB_NAME = "movie.db";




    public MovieDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // db
        String SQL_CREATE_DETAIL_TABLE =  "CREATE TABLE " + detailEntry.TABLE_NAME + " ("
                + detailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + detailEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                + detailEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + detailEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
                + detailEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, "
                + detailEntry.COLUMN_RELEASE_DAY + " TEXT NOT NULL, "
                + detailEntry.COLUMN_VOTE + " TEXT NOT NULL);";
                //+ detailEntry.COLUMN_TRAILER + " TEXT NOT NULL);";
    sqLiteDatabase.execSQL(SQL_CREATE_DETAIL_TABLE);
}

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}

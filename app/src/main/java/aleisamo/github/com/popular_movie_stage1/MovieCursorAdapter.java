package aleisamo.github.com.popular_movie_stage1;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import aleisamo.github.com.popular_movie_stage1.data.MovieContract;

public class MovieCursorAdapter extends CursorAdapter {

    public MovieCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //return LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
        return LayoutInflater.from(context).inflate(R.layout.image_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView poster = (ImageView) view.findViewById(R.id.moviePost);
        String posterLocationPath = cursor.getString(cursor.getColumnIndex(MovieContract.detailEntry.COLUMN_POSTER_PATH));
        Log.v("MovieCursorAdapter", "posterPath:" + posterLocationPath);
        Picasso.with(context).load(new File(posterLocationPath)).into(poster);
    }


}

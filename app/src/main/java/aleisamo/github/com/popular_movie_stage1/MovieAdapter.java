package aleisamo.github.com.popular_movie_stage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {
// TODO pass a Cursor for implement data from data base
    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    public View getView(int position, View view, ViewGroup parent) {
        Movie movie = getItem(position);

        //if this is a new view we need to inflate , if not, this view has already a layout called in the previous getView
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.image_view, parent, false);
        }

        ImageView poster = (ImageView) view.findViewById(R.id.moviePost);

        Picasso.with(getContext()).load(movie.getPosterPath()).into(poster);
        return view;
    }
}


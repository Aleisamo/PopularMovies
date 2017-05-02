package aleisamo.github.com.popular_movie_stage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReviewsAdapter extends ArrayAdapter<MovieReviews> {

    public ReviewsAdapter(Context context, List<MovieReviews> reviews) {
        super(context, 0, reviews);
    }

    public View getView(int position, View view, ViewGroup parent) {
        MovieReviews reviews = getItem(position);

        //if this is a new view we need to inflate if not this view has already a layout call in the previous getView
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.review, parent, false);
        }

        TextView author = (TextView) view.findViewById(R.id.review_author);
        author.setText("REVIEW" + "\n" + "\n" + "Author: " + reviews.getAuthor() + "\n" + reviews.getReviews());

        return view;
    }
}

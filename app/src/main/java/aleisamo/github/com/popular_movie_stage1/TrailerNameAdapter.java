package aleisamo.github.com.popular_movie_stage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TrailerNameAdapter extends ArrayAdapter<MovieVideo>{

    public TrailerNameAdapter(Context context, List<MovieVideo>videos) {
        super(context, 0,videos);
    }

    public View getView(int position, View view, ViewGroup parent) {
        MovieVideo video = getItem(position);

        //if this is a new view we need to inflate if not this view has already a layout call in the previous getView
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.video_title, parent, false);
        }

        TextView textView = (TextView) view.findViewById(R.id.movie_title_video);
        textView.setText(video.getName());
        ImageView imageView =(ImageView)view.findViewById(R.id.video_icon);
        imageView.setImageResource(R.drawable.video);
        return view;
    }}

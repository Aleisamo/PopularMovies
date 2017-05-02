package aleisamo.github.com.popular_movie_stage1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PopularMovieMain extends AppCompatActivity implements MovieFragment.CallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movie_main);
        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putParcelable(MovieFragment.MOVIE_FRAGMENT_URI, getIntent().getData());
            MovieFragment movieFragment = new MovieFragment();
            movieFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieFragment())
                    .commit();
        }
    }

}

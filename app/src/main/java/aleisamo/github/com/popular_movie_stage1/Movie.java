package aleisamo.github.com.popular_movie_stage1;

public class Movie {

    private String posterPath;
    private String overview;
    private String releaseDate;
    private String title;
    private String average;
    private String id;

    public Movie(String posterPath, String overview, String releaseDate, String title, String average, String id) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.title = title;
        this.average = average;
        this.id = id;
    }

    public String getPosterPath() {
        return "http://image.tmdb.org/t/p/w185//" + posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getAverage() {
        return average;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "posterPath='" + posterPath + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", title='" + title + '\'' +
                ", average='" + average + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

package aleisamo.github.com.popular_movie_stage1;

public class MovieReviews {
    private final String author;
    private final String reviews;

    public MovieReviews(String author, String reviews) {
        this.author = author;
        this.reviews = reviews;
    }

    public String getAuthor() {
        return author;
    }

    public String getReviews() {
        return reviews;
    }
}

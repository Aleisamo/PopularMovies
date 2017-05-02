package aleisamo.github.com.popular_movie_stage1;

public class MovieReview {
    private final String author;
    private final String review;

    public MovieReview(String author, String review) {
        this.author = author;
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public String getReview() {
        return review;
    }

    @Override
    public String toString() {
        return "MovieReview{" +
                "author='" + author + '\'' +
                ", review='" + review + '\'' +
                '}';
    }
}

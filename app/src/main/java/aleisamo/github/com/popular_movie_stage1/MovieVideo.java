package aleisamo.github.com.popular_movie_stage1;

class MovieVideo {

    private final String key;
    private final String name;

    MovieVideo(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getVideoPath() {
        return "https://www.youtube.com/watch?v=" + key;
    }

    @Override
    public String toString() {
        return "MovieVideo{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

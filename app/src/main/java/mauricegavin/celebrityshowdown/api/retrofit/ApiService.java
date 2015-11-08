package mauricegavin.celebrityshowdown.api.retrofit;

import mauricegavin.celebrityshowdown.model.MovieDetails;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface ApiService {

    int MAX_CAST_SHOWN = 10;
    int MAX_IMAGES_SHOWN = 10;

    String API_ENDPOINT = "https://api.themoviedb.org/3";
    String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w130/";

    String API_KEY = "a5c1950eaa7741716957de641c84bec2";

    @GET("/movie/popular")
    Observable<PopularMovies> getPopularMovies (@Query("api_key") String apiKey);

    @GET("/movie/{movieId}")
    Observable<MovieDetails> getMovieDetails (@Query("api_key") String apiKey,
                          @Path("movieId") long movieId);

    @GET("/movie/{movieId}/credits")
    Observable<MovieCast> getMovieCast (@Query("api_key") String apiKey,
                       @Path("movieId") long movieId);


}
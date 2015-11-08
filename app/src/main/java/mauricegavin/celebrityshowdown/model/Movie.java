package mauricegavin.celebrityshowdown.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Movie implements Comparable<Movie>, Parcelable {
	
	public interface MovieDetailsListener {
		void onMovieDetailsReceived(MovieDetails details);
	}

	private static final long serialVersionUID = -2351677725648457058L;
	
	public String title;
	public double popularity;
	public String poster_path;
	public String release_date;
	public double vote_average;
	public long id;

	private MovieDetails mDetails;

	/* 
	 * Example of a JSON object with a movie
	 {
      "adult": false,
      "backdrop_path": "/cKw3HY835PMp6bzse3LMivIY5Nl.jpg",
      "id": 1884,
      "original_title": "The Ewok Adventure",
      "release_date": "1984-11-25",
      "poster_path": "/x2nKP0FCJwNLHgCyUI1cL8bF6nL.jpg",
      "popularity": 0.72905031478,
      "title": "The Ewok Adventure",
      "vote_average": 10.0,
      "vote_count": 4
    }
	*/

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getReleaseDateAsString() {
		return release_date;
	}
	
	/*public void getDetails(final MovieDetailsListener listener) {
		if (mDetails != null) {
			listener.onMovieDetailsReceived(mDetails);			
		}
		else {
			MoviesApiFactory.getInstance().getMovieDetails(this, new MovieDetailsListener() {
                @Override
                public void onMovieDetailsReceived(MovieDetails details) {
                    mDetails = details;
                    listener.onMovieDetailsReceived(mDetails);
                }
            });
		}
	}*/
	
	public double getScore() {
		return vote_average;
	}

	@Override
	public int compareTo(Movie arg0) {
		if (popularity > arg0.popularity) {
			return -1;
		}
		else if (popularity > arg0.popularity) {
			return 1;
		}
		return 0;
	}

//	public Uri getThumbnailUrl() {
//		return Uri.parse(MoviesApiRetrofitImpl.IMAGE_BASE_URL+ poster_path);
//	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.title);
		dest.writeDouble(this.popularity);
		dest.writeString(this.poster_path);
		dest.writeString(this.release_date);
		dest.writeDouble(this.vote_average);
		dest.writeLong(this.id);
		dest.writeSerializable(this.mDetails);
	}

	public Movie() {
	}

	protected Movie(Parcel in) {
		this.title = in.readString();
		this.popularity = in.readDouble();
		this.poster_path = in.readString();
		this.release_date = in.readString();
		this.vote_average = in.readDouble();
		this.id = in.readLong();
		this.mDetails = (MovieDetails) in.readSerializable();
	}

	public static final Creator<Movie> CREATOR = new Creator<Movie>() {
		public Movie createFromParcel(Parcel source) {
			return new Movie(source);
		}

		public Movie[] newArray(int size) {
			return new Movie[size];
		}
	};
}

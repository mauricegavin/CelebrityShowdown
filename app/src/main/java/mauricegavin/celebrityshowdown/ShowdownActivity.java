package mauricegavin.celebrityshowdown;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import mauricegavin.celebrityshowdown.api.ApiServiceBuilder;
import mauricegavin.celebrityshowdown.api.retrofit.ApiService;
import mauricegavin.celebrityshowdown.api.retrofit.PopularMovies;
import mauricegavin.celebrityshowdown.model.Movie;
import mauricegavin.celebrityshowdown.ui.ShowdownFragment;
import mauricegavin.celebrityshowdown.ui.ShowdownFragment.ShowdownFragmentListener;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShowdownActivity extends AppCompatActivity implements ShowdownFragmentListener {

    private String TAG = ShowdownActivity.class.getName();

    private ApiService api;
    PopularMovies movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdown);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        api = new ApiServiceBuilder(this)
                .setAuthToken(ApiService.API_KEY)
                .create();

        api.getPopularMovies()
                .doOnNext(popularMovies -> movies = popularMovies)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieCast -> showComparisonFragment(movies.results.get(0), movies.results.get(1)));
    }

    //todo this would be a good place to use Kotlin
    public void showComparisonFragment(Movie movie1, Movie movie2) {
        Fragment fragment = ShowdownFragment.newInstance(movie1, movie2);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_showdown, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

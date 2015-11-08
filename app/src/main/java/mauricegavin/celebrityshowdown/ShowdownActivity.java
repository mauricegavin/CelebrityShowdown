package mauricegavin.celebrityshowdown;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import mauricegavin.celebrityshowdown.api.ApiServiceBuilder;
import mauricegavin.celebrityshowdown.api.retrofit.ApiService;
import mauricegavin.celebrityshowdown.api.retrofit.PopularMovies;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShowdownActivity extends AppCompatActivity {

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PopularMovies>() {
                    @Override
                    public void onCompleted() {
                        showComparisonActivity();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PopularMovies popularMovies) {
                        movies = popularMovies;
                    }
                });
    }

    public void showComparisonActivity() {
        if(movies.results.size() > 0) {
            Log.i(TAG, "Show ComparisonActivity");
            Intent intent = new Intent(this, ComparisonActivity.class);
            intent.putExtra("movie", movies.results.get(0));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //pop off all other activities
            startActivity(intent);
        }
        else {
            Log.i(TAG, "Cannot show ComparisonActivity, not data to show.");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("No movies available")
                    .setMessage("Unfortunately there are no movies available right now." +
                            "Maybe try going outside for a walk instead?")
                    .setCancelable(false)
                    .setPositiveButton("I love walking!",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .create()
                    .show();
        }

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

    private void initialiseDataSet() {

        Observable<PopularMovies> movies = api.getPopularMovies();

    }

}

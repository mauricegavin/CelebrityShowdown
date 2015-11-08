package mauricegavin.celebrityshowdown;

/**
 * Created by maurice on 08/11/15.
 */

import android.app.Fragment;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.util.Log;

import mauricegavin.celebrityshowdown.api.ApiServiceBuilder;
import mauricegavin.celebrityshowdown.api.retrofit.ApiService;
import mauricegavin.celebrityshowdown.model.Cast;
import mauricegavin.celebrityshowdown.model.Movie;
import mauricegavin.celebrityshowdown.ui.ShowdownFragment;
import mauricegavin.celebrityshowdown.viewModel.AbstractViewModel;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by maurice on 11/09/15.
 */
public class ShowdownFragmentViewModel extends AbstractViewModel {

    private final String TAG = ShowdownFragmentViewModel.class.getSimpleName();

    private ShowdownFragment mListener;
    private ApiService api;

    // Fields of this type should be declared final because bindings only detect changes in the field's value, not of the field itself.
    private Movie movie1, movie2;

    @Bindable
    public final ObservableField<Cast> person1 = new ObservableField<>();
    @Bindable
    public final ObservableField<Cast> person2 = new ObservableField<>();
    @Bindable
    public final ObservableField<Cast> moviePoster = new ObservableField<>();

    /**
     * Create a new instance of the ReportsViewModel
     *
     * @param fragment
     */
    public ShowdownFragmentViewModel(Fragment fragment, Movie movie1, Movie movie2) {
        try {
            mListener = (ShowdownFragment) fragment;
            this.movie1 = movie1;
            this.movie2 = movie2;
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString()
                    + " must implement ShowdownFragmentViewModelListener");
        }

        // Networking initialisation
        api = new ApiServiceBuilder(fragment.getActivity())
                .setAuthToken(ApiService.API_KEY)
                .create();

        api.getMovieCast(movie1.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieCast -> person1.set(movieCast.cast.get(0)));

        api.getMovieCast(movie2.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieCast -> person2.set(movieCast.cast.get(0)));

        }

    /**
     * Initialise the ViewModel's data from local memory
     */
    @Override
    protected void subscribeToDataStoreInternal(CompositeSubscription compositeSubscription) {
        Log.v(TAG, "subscribeToDataStoreInternal");
    }

    @Override
    protected void subscribeToDataStoreExternal(CompositeSubscription compositeSubscription) {
        Log.v(TAG, "subscribeToDataStoreExternal");
    }

    /**
     * Set the parent Fragment as a listener
     *
     * @param listener
     */
    public void setListener(ShowdownFragment listener) {
        this.mListener = listener;
    }


    /**
     * A listener interface that is implemented by the View that is bound to this ViewModel
     * It allows the ViewModel to invoke methods in the View.
     */
    public interface ShowdownFragmentViewModelListener {

        public void finish();

    }

}

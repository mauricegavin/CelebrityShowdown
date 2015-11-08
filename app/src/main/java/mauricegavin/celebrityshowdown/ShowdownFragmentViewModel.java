package mauricegavin.celebrityshowdown;

/**
 * Created by maurice on 08/11/15.
 */

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import org.apache.commons.io.IOUtils;

import java.util.Calendar;
import java.util.List;

import mauricegavin.celebrityshowdown.api.ApiServiceBuilder;
import mauricegavin.celebrityshowdown.api.retrofit.ApiService;
import mauricegavin.celebrityshowdown.ui.ShowdownFragment;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by maurice on 11/09/15.
 */
public class ShowdownFragmentViewModel {

    private final String TAG = ShowdownFragmentViewModel.class.getSimpleName();

    private ShowdownFragment mListener;
    private Resources resources;

    private ApiService api;

    // Fields of this type should be declared final because bindings only detect changes in the field's value, not of the field itself.
    @Bindable
    public final ObservableBoolean loginMode = new ObservableBoolean();

    @Bindable
    private ObservableField<String> progressTitle = new ObservableField<String>(null);
    private ObservableField<String> progressMessage = new ObservableField<String>(null);

    /**
     * Create a new instance of the ReportsViewModel
     *
     * @param fragment
     */
    public ShowdownFragmentViewModel(Fragment fragment, Resources resources) {
        try {
            mListener = (ShowdownFragment) fragment;
            this.resources = resources;
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString()
                    + " must implement LoginViewModelListener");
        }

        // Networking initialisation
        api = new ApiServiceBuilder(fragment.getActivity())
                .setAuthToken("7vzn9CmuqjX3uva5ektH") //todo set this to null)
                .create();

        loginMode.set(true);
    }

    /**
     * Initialise the ViewModel's data from local memory
     */
    protected void subscribeToDataStoreInternal(CompositeSubscription compositeSubscription) {
        Log.v(TAG, "subscribeToDataStoreInternal");

/*        databaseManager.loadPatientCase("266")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(patientCase -> patientCase != null)
                .subscribe(patientCase -> this.addPatientCase(patientCase));*/
    }

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
        public void showDialog(MaterialDialog.Builder dialogBuilder);

        public void login();

        public void modeChanged(boolean loginMode);

        public void finish();

        public void showProgress();

        public void hideProgress();
    }

}

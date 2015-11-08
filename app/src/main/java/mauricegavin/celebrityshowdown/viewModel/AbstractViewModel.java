package mauricegavin.celebrityshowdown.viewModel;

import android.databinding.BaseObservable;
import android.util.Log;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by maurice on 09/09/15.
 */
abstract public class AbstractViewModel extends BaseObservable {
    private static final String TAG = AbstractViewModel.class.getSimpleName();
    private CompositeSubscription compositeSubscription;

    final public void subscribeToDataStore() {
        Log.v(TAG, "subscribeToDataStore");
        unsubscribeFromDataStore();
        compositeSubscription = new CompositeSubscription();
        subscribeToDataStoreInternal(compositeSubscription);
        subscribeToDataStoreExternal(compositeSubscription);
    }

    final public CompositeSubscription getSubscription() {
        Log.v(TAG, "getSubscription");
        return compositeSubscription;
    }

    public void dispose() {
        Log.v(TAG, "dispose");

        if (compositeSubscription != null) {
            Log.e(TAG, "Disposing without calling unsubscribeFromDataStore first");

            // Unsubscribe in case we are still for some reason subscribed
            unsubscribeFromDataStore();
        }
    }

    protected abstract void subscribeToDataStoreInternal(CompositeSubscription compositeSubscription);

    protected abstract void subscribeToDataStoreExternal(CompositeSubscription compositeSubscription);

    public void unsubscribeFromDataStore() {
        Log.v(TAG, "unsubscribeToDataStore");
        if (compositeSubscription != null) {
            compositeSubscription.clear();
            compositeSubscription = null;
        }
    }

}

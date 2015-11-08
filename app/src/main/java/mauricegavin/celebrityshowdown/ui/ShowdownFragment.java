package mauricegavin.celebrityshowdown.ui;

import android.app.Activity;
import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mauricegavin.celebrityshowdown.R;
import mauricegavin.celebrityshowdown.ShowdownFragmentViewModel;
import mauricegavin.celebrityshowdown.databinding.FragmentShowdownBinding;
import mauricegavin.celebrityshowdown.model.Cast;

/**
 * Created by maurice on 08/11/15.
 */
public class ShowdownFragment extends Fragment implements ShowdownFragmentViewModel.ShowdownFragmentViewModelListener {

    private final static String TAG = ShowdownFragment.class.getName();

    private FragmentShowdownBinding binding;
    private ShowdownFragmentViewModel viewModel;
    private ShowdownFragmentListener mListener;

    private Cast person1;
    private Cast person2;

    public static ShowdownFragment newInstance(Cast person1, Cast person2) {
        ShowdownFragment fragment = new ShowdownFragment();
        Bundle args = new Bundle();
        args.putParcelable("person1", person1);
        args.putParcelable("person2", person2);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowdownFragment() {
    } // Required empty public constructor

    //region Overridden Activity Lifecycle Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.person1 = getArguments().getParcelable("person1");
            this.person2 = getArguments().getParcelable("person2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // The ViewModel needs to be created first because the view will rely on it to resolve its
        // content
        viewModel = new ShowdownFragmentViewModel(this, person1, person2);
        viewModel.setListener(this);

        // Setup of the View
        final View view = inflater.inflate(R.layout.fragment_showdown, container, false);
        binding = DataBindingUtil.bind(view);
        binding.setViewModel(viewModel);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.subscribeToDataStore(); // Network call to refresh data
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.unsubscribeFromDataStore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.dispose();
        viewModel = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ShowdownFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ShowdownFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    //endregion

    @Override
    public void finish() {
        mListener.finish();
    }

    public interface ShowdownFragmentListener {
        public void finish();
    }
}




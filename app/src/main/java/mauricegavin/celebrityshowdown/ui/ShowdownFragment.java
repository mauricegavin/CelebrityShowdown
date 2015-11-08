package mauricegavin.celebrityshowdown.ui;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import mauricegavin.celebrityshowdown.ShowdownFragmentViewModel;
import mauricegavin.celebrityshowdown.R;
import mauricegavin.celebrityshowdown.databinding.FragmentShowdownBinding;
import mauricegavin.celebrityshowdown.model.Cast;

/**
 * Created by maurice on 08/11/15.
 */
public class ShowdownFragment extends Fragment {

    private final static String TAG = ShowdownFragment.class.getName();

    private FragmentShowdownBinding binding;
    private ShowdownFragmentViewModel viewModel;

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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // The ViewModel needs to be created first because the view will rely on it to resolve its
        // content
        viewModel = new ShowdownFragmentViewModel(this);
        viewModel.setListener(this);

        // Setup of the View
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        mContainer = ((RelativeLayout) view.findViewById(R.id.login_container));
        binding = DataBindingUtil.bind(view);
        binding.loginContainer.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        binding.setLoginViewModel(viewModel);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        showLoginViews();

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
        Utils.hideKeyboard(
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE), getView().getWindowToken());
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
            mListener = (ILoginFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ILoginFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Crouton.cancelAllCroutons();
        mListener = null;
    }
    //endregion

    @Override
    public void showDialog(MaterialDialog.Builder dialogBuilder) {
        dialogBuilder.build().show();
    }

    @Override
    public void login() {
        mListener.login();
    }

    @Override
    public void modeChanged(boolean loginMode) {
        if (loginMode) {
            showLoginViews();
        } else {
            showActivateAccountViews();
        }
    }

    @Override
    public void finish() {
        mListener.finish();
    }

    /**
     * Show the view elements for Login mode
     */
    @DebugLog
    private void showLoginViews() {

        //region Adjust the RelativeLayout's margin from the top of the screen
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mContainer.getLayoutParams();
        params.setMargins(0, 160, 0, 0);
        mContainer.setLayoutParams(params);
        //endregion

        // Insert the cursor into the first EditText field
        mContainer.findViewById(R.id.editText_email_field).requestFocus();

        // Notify the parent Activity
        mListener.showLoginViews();
    }

    /**
     * Show view elements for Activate Account mode
     */
    @DebugLog
    private void showActivateAccountViews() {

        //region Hide views which are not applicable to this (Activate Account) mode
        // Hide the ForgotPassword TextView
        //TODO Update the ForgotPasswordFragment ((LinearLayout)mContainer.findViewById(R.id.login_forgot_password_layout)).removeView(mContainer.findViewById(R.id.button_forgot_password));
        //endregion

        //region Adjust the RelativeLayout's margin from the top of the screen
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mContainer.getLayoutParams();
        params.setMargins(0, 60, 0, 0);
        mContainer.setLayoutParams(params);
        //endregion

        // Insert the cursor into the first EditText field
        mContainer.findViewById(R.id.member_id_inputlayout).requestFocus();

        // Notify the parent Activity
        mListener.showActivateAccountViews();
    }

    /**
     * Navigation method for navigating from Activate Account to Login mode
     */
    public void navigateUp() {
        viewModel.navigateUp();
    }

    @Override
    public void showProgress() {
        mProgressDialog.setTitle(viewModel.getProgressTitle());
        mProgressDialog.setMessage(viewModel.getProgressMessage());
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        mProgressDialog.hide();
    }

// Prefer the user of RenderableException and descriptive messages from the API
    /**
     * Handle the error thrown
     *
     * @param e
     *//*
    private void handleError(Throwable e) {

        if (e instanceof NetworkUtils.DeviceOfflineException) {
            UiUtils.createNetworkUnavailableNotification(getActivity(),
                    getResources().getString(R.string.network_unavailable_message_login),
                    this.getResources().getDrawable(R.drawable.ic_warning_white_48dp))
                    .show();
        } else if (e instanceof RetrofitError) {

            *//*if (((RetrofitError)e).getKind().equals(RetrofitError.Kind.NETWORK)) { // Error communicating with the server
                Log.e(TAG, e.toString());
                UiUtils.createTwoChoiceDialog(getActivity(),
                        getResources().getString(R.string.error_login_unsuccessful),
                        getResources().getString(R.string.error_could_not_reach_server),
                        getResources().getString(R.string.action_contact_support),
                        getResources().getString(R.string.action_OK),
                        this.getResources().getDrawable(R.drawable.ic_warning_white_48dp),
                        null)
                        .show();
            }
            else if (((RetrofitError)e).getKind().equals(RetrofitError.Kind.HTTP)) { // Non-200 HTTP code returned by the server
                UiUtils.createTwoChoiceDialog(getActivity(),
                        getResources().getString(R.string.error_login_unsuccessful),
                        getResources().getString(R.string.error_server_returned_non_200), //todo +Error code
                        getResources().getString(R.string.action_OK),
                        getResources().getString(R.string.action_contact_support),
                        this.getResources().getDrawable(R.drawable.ic_warning_white_48dp),
                        null)
                        .show();
            }
            else if (((RetrofitError)e).getKind().equals(RetrofitError.Kind.CONVERSION)) { // Error converting into local DB models

            }
            else if (((RetrofitError)e).getKind().equals(RetrofitError.Kind.UNEXPECTED)) { // Unexpected Retrofit Error
            }*//*
        } else if (e instanceof RetrofitErrorHandler.ApiErrorResponses) {
            if (((RetrofitErrorHandler.ApiErrorResponses) e).getErrorResponses() != null
                    && ((RetrofitErrorHandler.ApiErrorResponses) e).getErrorResponses().size() > 0) {
                RenderableException errorResponse = ((RetrofitErrorHandler.ApiErrorResponses) e).getErrorResponses().get(0);

                Log.e(TAG, "Error " + errorResponse.getInternal_code() + ": " + errorResponse.getMessage());

                UiUtils.createNotificationDialog(getActivity(),
                        getResources().getString(R.string.error_login_unsuccessful),
                        "Error " + errorResponse.getInternal_code() + ": " + errorResponse.getMessage(),
                        getResources().getString(R.string.action_OK),
                        this.getResources().getDrawable(R.drawable.ic_warning_white_48dp),
                        null)
                        .show();
            }
        } else {
            Log.e(TAG, e.toString());

            UiUtils.createNotificationDialog(getActivity(),
                    getResources().getString(R.string.error_login_unsuccessful),
                    e.toString(), //todo +Error code
                    getResources().getString(R.string.action_OK),
                    this.getResources().getDrawable(R.drawable.ic_warning_white_48dp),
                    null)
                    .show();

        }
    }*/

    //region UserFormData inner class which is used to hold form data from the Fragment

    /**
     * An inner class which holds the data that the user has entered into the Fragment
     */
    public class UserFormData {

        private String memberId;
        private String organisationId;
        private Long dateOfBirth = Long.MIN_VALUE;
        private String email;
        private String confirmEmail;
        private String password;

        public UserFormData() {
            restoreState();
        }

        /**
         * Constructor for data object used for Login requests
         *
         * @param email
         * @param password
         */
        public UserFormData(@NonNull String email, @NonNull String password) {
            this.email = email.trim();
            this.password = password.trim();
        }

        /**
         * Constructor for data object used for Activate Account requests
         *
         * @param memberId
         * @param organisationId
         * @param dateOfBirth    DOB is stored as a Long, this is so that a change in the user's Locale will not change the meaning of the Date
         * @param email
         * @param password
         */
        public UserFormData(@Nullable String memberId, @Nullable String organisationId,
                            @Nullable Long dateOfBirth, @NonNull String email,
                            @Nullable String confirmEmail, @NonNull String password) {
            this.memberId = memberId;
            this.organisationId = organisationId;
            this.dateOfBirth = dateOfBirth;
            this.email = email.trim();
            this.confirmEmail = confirmEmail;
            this.password = password.trim();
        }

        public String getMemberId() {
            return memberId;
        }

        public String getOrganisationId() {
            return organisationId;
        }

        public String getDateOfBirthAsString() {
            return Utils.getLocalisedDate(getActivity(), dateOfBirth * 1000);
        }

        /**
         * Note that date of birth is stored in seconds before/since epoch. It may need to be converted
         * to milliseconds
         *
         * @return the number of seconds before/since epoch time
         */
        public long getDateOfBirthAsLong() {
            return dateOfBirth;
        }

        /**
         * @return true if has a value for DateOfBirth, false otherwise
         */
        public boolean isDateOfBirthSet() {
            return ((dateOfBirth != null) && (dateOfBirth != Long.MIN_VALUE));
        }

        public String getEmail() {
            return email;
        }

        public String getConfirmEmail() {
            return confirmEmail;
        }

        public String getPassword() {
            return password;
        }

        /**
         * Initilaises the object with the Saved State
         */
        public void restoreState() {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            this.memberId = sharedPref.getString(DataFormElement.MEMBER_ID.getKey(), null);
            this.organisationId = sharedPref.getString(DataFormElement.ORGANISATION_ID.getKey(), null);
            this.dateOfBirth = sharedPref.getLong(DataFormElement.DOB.getKey(), Long.MIN_VALUE);
            this.email = sharedPref.getString(DataFormElement.EMAIL.getKey(), null);
            this.confirmEmail = sharedPref.getString(DataFormElement.CONFIRM_EMAIL.getKey(), null);
            this.password = sharedPref.getString(DataFormElement.PASSWORD.getKey(), null);
        }

        public void saveState() {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            if (this.memberId != null)
                editor.putString(DataFormElement.MEMBER_ID.getKey(), this.memberId);
            if (this.organisationId != null)
                editor.putString(DataFormElement.ORGANISATION_ID.getKey(), this.organisationId);
            if ((this.dateOfBirth != null) && (this.dateOfBirth != Long.MIN_VALUE))
                editor.putLong(DataFormElement.DOB.getKey(), this.dateOfBirth);
            if (this.email != null)
                editor.putString(DataFormElement.EMAIL.getKey(), this.email);
            if (this.confirmEmail != null)
                editor.putString(DataFormElement.CONFIRM_EMAIL.getKey(), this.email);
            if (this.password != null)
                editor.putString(DataFormElement.PASSWORD.getKey(), this.password);

            editor.commit();
        }

        public void clearState() {
            LoginFragment.clearState(getActivity());
        }

    }

    /**
     * Clear the saved state of all form data
     *
     * @param activity the parent activity
     */
    public static void clearState(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.remove(DataFormElement.MEMBER_ID.getKey());
        editor.remove(DataFormElement.ORGANISATION_ID.getKey());
        editor.remove(DataFormElement.DOB.getKey());
        editor.remove(DataFormElement.EMAIL.getKey());
        editor.remove(DataFormElement.CONFIRM_EMAIL.getKey());
        editor.remove(DataFormElement.PASSWORD.getKey());

        editor.commit();
    }

    private enum DataFormElement {
        MEMBER_ID("login_memberid"),
        ORGANISATION_ID("login_orgid"),
        DOB("login_dob"),
        EMAIL("login_email"),
        CONFIRM_EMAIL("login_confirm_email"),
        PASSWORD("login_password");

        private String key;

        DataFormElement(String s) {
            this.key = s;
        }

        public String getKey() {
            return key;
        }
    }
    //endregion

    //region ILoginFragmentListener used for callbacks to parent Activity

    /**
     * Callback listener interface for this Fragment's parent Activity
     *
     * @author maurice
     */
    public interface ILoginFragmentListener {

        /**
         * Initiate the network call to log in the user.
         * Parameters that are passed should already have been validated with regular expressions
         * where appropriate
         */
        void login();

        /**
         * A callback to the parent Activity to let it know that the LoginFragment is redrawing
         * itself to show only the view elements that are related to logging in (i.e. all buttons
         * are labelled with contextual information about logging in as opposed to activating an
         * account)
         * The parent Activity is responsible for updating the toolbar to hide the navigationUp arrow
         */
        void showLoginViews();

        /**
         * A callback to the parent Activity to let it know that the LoginFragment is redrawing
         * itself to show only the view elements that are related to account activation (i.e. all
         * buttons are labelled with contextual information about account activation as opposed to
         * logging in a user)
         * The parent Activity is responsible for updating the toolbar to show the navigationUp
         * arrow.
         * It should also verify that a list of providers is available. If they are not available
         * then it should attempt to download them now. If they still cannot be fetched the user
         * should be informed with a Dialog notification.
         */
        void showActivateAccountViews();


        /**
         * A callback to the parent Activity to let it know that the LoginFragment should be
         * replaced with the TermsAndConditionsFragment
         */
        void showTermsAndConditionsScreen();

        /**
         * A callback to the parent Activity to let it know that the LoginFragment should be
         * replaced with the TermsAndConditionsFragment
         *
         * @param email the email of the user who has forgotten their password
         */
        void showForgotPasswordScreen(@Nullable String email);

        void finish();
    }
    //endregion


}




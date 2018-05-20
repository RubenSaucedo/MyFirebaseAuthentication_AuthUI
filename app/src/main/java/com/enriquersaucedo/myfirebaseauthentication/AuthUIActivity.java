package com.enriquersaucedo.myfirebaseauthentication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthUIActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TOS_URL = "http://www.enriquersaucedo.com";
    private static final String PRIVACY_POLICY = "http://www.enriquersaucedo.com";
    private static final String TAG = "AuthUIActivity";

    @BindView(R.id.root) View mRootView;


    public static Intent createIntent(Context context){
        return new Intent(context, AuthUIActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authui);
        //Bind views in the layout with ButterKnife
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Check whether a user is signed in from a previous session
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            // already signed in
            startSignedInActivity(null);
        } else{
            // not signed id
        }
    }

    @OnClick({R.id.sign_in})
    public void signIn(View view){
        startActivityForResult(
                //Get an instance of AuthUI based on the default app
                AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                ))
                .setTheme(R.style.PurpleTheme)
                .setLogo(R.drawable.ensa_logo)
                .setTosUrl(TOS_URL)
                .setPrivacyPolicyUrl(PRIVACY_POLICY)
                .build(),
                RC_SIGN_IN
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            handleSignInResponse(resultCode, data);
        }
    }

    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if(resultCode == RESULT_OK){
            startSignedInActivity(response);
        } else{
            //Sign in failed
            if(response == null){
                //User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                showSnackbar(R.string.no_internet_connection);
            }

            showSnackbar(R.string.unknown_error);
            Log.e(TAG, "Sign-in Error: ", response.getError());
        }
    }


    private void startSignedInActivity(IdpResponse response) {
        startActivity(SignedInActivity.createIntent(this, response));
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}

package com.enriquersaucedo.myfirebaseauthentication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignedInActivity extends AppCompatActivity {

    private static final String TAG = "SignedInActivity";

    @BindView(android.R.id.content) View mRootView;

    @BindView(R.id.user_profile_picture) ImageView mUserProfilePicture;
    @BindView(R.id.user_email) TextView mUserEmail;
    @BindView(R.id.user_display_name) TextView mUserDisplayName;
    @BindView(R.id.user_phone_number) TextView mUserPhoneNumber;
    @BindView(R.id.user_enabled_providers) TextView mEnabledProviders;

    public static Intent createIntent(Context context, IdpResponse idpResponse){
        return new Intent().setClass(context, SignedInActivity.class)
        .putExtra(ExtraConstants.IDP_RESPONSE, idpResponse);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(AuthUIActivity.createIntent(this));
            finish();
            return;
        }

        IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);

        setContentView(R.layout.activity_signed_in);
        ButterKnife.bind(this);
        populateProfile();
    }

    @OnClick(R.id.sign_out)
    public void signOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()){
                                                   startActivity(AuthUIActivity.createIntent(SignedInActivity.this));
                                                   finish();
                                               } else{
                                                   Log.w(TAG, "signOut:failure", task.getException());
                                                   showSnackbar(R.string.sign_out_failed);
                                               }
                                           }
                                       });
    }

    @OnClick(R.id.delete_account)
    public void deleteAccountClicked(){
        new AlertDialog.Builder(this)
                .setMessage(R.string.message_delete_account)
                .setPositiveButton(R.string.delete_confirmation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            startActivity(AuthUIActivity.createIntent(SignedInActivity.this));
                            finish();
                        } else{
                            showSnackbar(R.string.delete_account_failed);
                        }
                    }
                });
    }

    private void populateProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getPhotoUrl() != null){
            GlideApp.with(this)
                    .load(user.getPhotoUrl())
                    .fitCenter()
                    .into(mUserProfilePicture);
        }

        mUserEmail.setText(
                TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail());
        mUserPhoneNumber.setText(
                TextUtils.isEmpty(user.getPhoneNumber()) ? "No phone number" : user.getPhoneNumber());
        mUserDisplayName.setText(
                TextUtils.isEmpty(user.getDisplayName()) ? "No display name" : user.getDisplayName());

        List<String> providers = new ArrayList<>();
        if (user.getProviderData().isEmpty()) {
            providers.add("Anonymous");
        } else {
            for (UserInfo info : user.getProviderData()) {
                switch (info.getProviderId()) {
                    case GoogleAuthProvider.PROVIDER_ID:
                        providers.add(getString(R.string.providers_google));
                        break;
                    case EmailAuthProvider.PROVIDER_ID:
                        providers.add(getString(R.string.providers_email));
                        break;
                    case PhoneAuthProvider.PROVIDER_ID:
                        providers.add(getString(R.string.providers_phone));
                        break;
                    case FirebaseAuthProvider.PROVIDER_ID:
                        // Ignore this provider, it's not very meaningful
                        break;
                    default:
                        throw new IllegalStateException(
                                "Unknown provider: " + info.getProviderId());
                }
            }
        }

        mEnabledProviders.setText(getString(R.string.used_providers, providers));


    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}

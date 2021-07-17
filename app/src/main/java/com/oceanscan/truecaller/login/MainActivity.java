package com.oceanscan.truecaller.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueProfile;
import com.truecaller.android.sdk.TruecallerSDK;
import com.truecaller.android.sdk.TruecallerSdkScope;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (findViewById(R.id.login_with_truecaller)).setOnClickListener((View v) -> {
            //check if TrueCaller SDk is usable
            if(TruecallerSDK.getInstance().isUsable()){
                TruecallerSDK.getInstance().getUserProfile( MainActivity.this);
            }else{

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setMessage("Truecaller App not installed.");

                dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                            Log.d(TAG, "onClick: Closing dialog");

                            dialog.dismiss();
                        }
                );

                dialogBuilder.setIcon(R.drawable.com_truecaller_icon);
                dialogBuilder.setTitle(" ");

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        // customisation of TrueCaller function like color , text can be done here
        TruecallerSdkScope trueScope = new TruecallerSdkScope.Builder(this, sdkCallback)
                .consentMode(TruecallerSdkScope.CONSENT_MODE_BOTTOMSHEET)
                .loginTextPrefix(TruecallerSdkScope.LOGIN_TEXT_PREFIX_TO_GET_STARTED)
                .loginTextSuffix(TruecallerSdkScope.LOGIN_TEXT_SUFFIX_PLEASE_VERIFY_MOBILE_NO)
                .ctaTextPrefix(TruecallerSdkScope.CTA_TEXT_PREFIX_USE)
                .buttonShapeOptions(TruecallerSdkScope.BUTTON_SHAPE_ROUNDED)
                .privacyPolicyUrl("<<YOUR_PRIVACY_POLICY_LINK>>")
                .termsOfServiceUrl("<<YOUR_PRIVACY_POLICY_LINK>>")
                .footerType(TruecallerSdkScope.FOOTER_TYPE_NONE)
                .consentTitleOption(TruecallerSdkScope.SDK_CONSENT_TITLE_LOG_IN)
                .build();
        TruecallerSDK.init(trueScope);
    }
    private final ITrueCallback sdkCallback = new ITrueCallback() {

        @Override
        public void onSuccessProfileShared(@NonNull final TrueProfile trueProfile) {
            Log.i(TAG, trueProfile.firstName + " " + trueProfile.lastName);
       launchHome(trueProfile);
        }

        @Override
        public void onFailureProfileShared(@NonNull final TrueError trueError) {
            Log.i(TAG, trueError.toString());
        }

        @Override
        public void onVerificationRequired(@Nullable final TrueError trueError) {
            Log.i(TAG, "onVerificationRequired");
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TruecallerSDK.SHARE_PROFILE_REQUEST_CODE) {
            TruecallerSDK.getInstance().onActivityResultObtained(this, requestCode, resultCode, data);
        }    }

    private void launchHome(TrueProfile trueProfile) {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class)
                .putExtra("profile", trueProfile));
        finish();
    }

}

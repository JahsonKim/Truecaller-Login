package com.oceanscan.truecaller.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueProfile;
import com.truecaller.android.sdk.TrueSDK;
import com.truecaller.android.sdk.TrueSdkScope;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (findViewById(R.id.login_with_truecaller)).setOnClickListener((View v) -> {
            //check if Truecaller SDk is usable
            if (TrueSDK.getInstance().isUsable()) {
                TrueSDK.getInstance().getUserProfile(MainActivity.this);
            }
        });


        TrueSdkScope trueScope = new TrueSdkScope.Builder(this, new ITrueCallback() {
            @Override
            public void onSuccessProfileShared(@NonNull TrueProfile trueProfile) {
                Log.i(TAG, trueProfile.firstName + " " + trueProfile.lastName);
                launchHome(trueProfile);
            }

            @Override
            public void onFailureProfileShared(@NonNull TrueError trueError) {
                Log.i(TAG, trueError.toString());
            }

            @Override
            public void onVerificationRequired() {
                Log.i(TAG, "onVerificationRequired");
            }
        })
                .consentMode(TrueSdkScope.CONSENT_MODE_POPUP)
                .consentTitleOption(TrueSdkScope.SDK_CONSENT_TITLE_VERIFY)
                .footerType(TrueSdkScope.FOOTER_TYPE_SKIP).build();
        TrueSDK.init(trueScope);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TrueSDK.getInstance().onActivityResultObtained(this, resultCode, data);
    }

    private void launchHome(TrueProfile trueProfile) {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class)
                .putExtra("profile", trueProfile));
        finish();
    }

}

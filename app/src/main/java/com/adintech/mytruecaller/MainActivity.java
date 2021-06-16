package com.adintech.mytruecaller;

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
    private static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (findViewById(R.id.login_with_truecaller)).setOnClickListener((View v) -> {
            //check if Truecaller SDk is usable
            if (TruecallerSDK.getInstance().isUsable()) {
                TruecallerSDK.getInstance().getUserProfile(MainActivity.this);
            } else {
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

        TruecallerSdkScope trueScope = new TruecallerSdkScope.Builder(this, new ITrueCallback() {
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
                .consentMode(TruecallerSdkScope.CONSENT_MODE_POPUP)
                .consentTitleOption(TruecallerSdkScope.SDK_CONSENT_TITLE_VERIFY)
                .footerType(TruecallerSdkScope.FOOTER_TYPE_SKIP).build();
        TruecallerSDK.init(trueScope);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TruecallerSDK.getInstance().onActivityResultObtained(this, resultCode, data);
    }

    private void launchHome(TrueProfile trueProfile) {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class)
                .putExtra("profile", trueProfile));
        finish();
    }
}
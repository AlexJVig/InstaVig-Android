package com.example.instavig_android;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        if(fbUser != null) {
            // User already signed in
            // go to feed activity
            Intent intent = new Intent(this, FeedActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void signIn(View view) {
        startActivityForResult(
                // Get an instance of AuthUI based on the default app
                AuthUI.getInstance().createSignInIntentBuilder().build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                // go to feed activity
                Intent intent = new Intent(this, FeedActivity.class);
                startActivity(intent);
            } else {
                // Sign in failed, check response for error code
                if (response != null) {
                    Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

package mikekamau.com.openchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Views
    private RecyclerView messageList;
    private AppCompatImageView attachFile;
    private TextView message;
    private FloatingActionButton sendMessage;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    private String profileName;
    private String profilePicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (!isUserAuthenticated()) {
            // user not signed in. launch SignInActivity
            Intent signInIntent = new Intent(this, SignInActivity.class);
            startActivity(signInIntent);
            finish();
            return;
        } else {
            // user is authenticated.
            profileName = firebaseUser.getDisplayName();
            if (firebaseUser.getPhotoUrl() != null) {
                profilePicUrl = firebaseUser.getPhotoUrl().toString();
            }
        }
    }

    private boolean isUserAuthenticated() {
        return firebaseUser == null;
    }
}

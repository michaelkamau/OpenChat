package mikekamau.com.openchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import mikekamau.com.openchat.entities.ChatMessage;
import mikekamau.com.openchat.entities.User;
import mikekamau.com.openchat.messages.FirebaseDBUtils;
import mikekamau.com.openchat.messages.MyFirebaseInstanceIdService;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";

    // Views
    private RecyclerView messageListRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private AppCompatImageView attachFileImageView;
    private TextView messageTextView;
    private FloatingActionButton sendMessageFab;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseRecyclerAdapter fbAdapter;

    private GoogleApiClient googleApiClient;

    private String profileName;
    private String profilePicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init Joda Time Lib
        JodaTimeAndroid.init(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (!isUserAuthenticated()) {
            Intent signInIntent = new Intent(this, SignInActivity.class);
            startActivity(signInIntent);
            finish();
            return;
        } else {
            profileName = firebaseUser.getDisplayName();
            if (firebaseUser.getPhotoUrl() != null) {
                profilePicUrl = firebaseUser.getPhotoUrl().toString();
            }
            new MyFirebaseInstanceIdService().onTokenRefresh();
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        messageTextView = findViewById(R.id.ed_message_text);
        sendMessageFab = findViewById(R.id.fab_send_message);
        sendMessageFab.setOnClickListener(this);

        messageListRecyclerView = findViewById(R.id.messages_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageListRecyclerView.setLayoutManager(linearLayoutManager);

        //
        fbAdapter = FirebaseDBUtils.getConfiguredFirebaseAdapter(
                FirebaseDatabase.getInstance().getReference(),
                firebaseUser
        );
        messageListRecyclerView.setAdapter(fbAdapter);
    }

    private boolean isUserAuthenticated() {
        return firebaseUser != null;
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.fab_send_message) {
            sendMessage();
        }
    }

    private void sendMessage() {
        DateTime currentDateTime = new DateTime();
        String now = currentDateTime.toString();
        User sender = new User(firebaseUser.getUid(), profileName, profilePicUrl);
        final String message = messageTextView.getText().toString();
        if (message != null && !message.isEmpty()) {
            ChatMessage chatMessage = new ChatMessage(now,
                    message, null, sender);
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseDBUtils.MESSAGES_CHILD)
                    .push()
                    .setValue(chatMessage);
            messageTextView.setText("");
        } else {
            Snackbar.make(findViewById(R.id.main_activity_layout),
                    getString(R.string.empty_message_error), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        fbAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fbAdapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                // log out user
                firebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(googleApiClient);
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
        Snackbar.make(findViewById(R.id.main_activity_layout),
                "Connection to Google Services failed", Snackbar.LENGTH_LONG).show();
    }
}

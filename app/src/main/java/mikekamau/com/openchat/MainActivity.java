package mikekamau.com.openchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;

import mikekamau.com.openchat.entities.ChatMessage;
import mikekamau.com.openchat.entities.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    // Views
    private RecyclerView messageListRecyclerView;
    private AppCompatImageView attachFileImageView;
    private TextView messageTextView;
    private FloatingActionButton sendMessageFab;

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
            Intent signInIntent = new Intent(this, SignInActivity.class);
            startActivity(signInIntent);
            finish();
            return;
        } else {
            profileName = firebaseUser.getDisplayName();
            if (firebaseUser.getPhotoUrl() != null) {
                profilePicUrl = firebaseUser.getPhotoUrl().toString();
            }
        }

        messageTextView = findViewById(R.id.ed_message_text);
        sendMessageFab = findViewById(R.id.fab_send_message);
        sendMessageFab.setOnClickListener(this);
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
        Instant instant = Instant.now();
        String now = String.valueOf(instant.toEpochMilli());
        User sender = new User(profileName, profilePicUrl);
        final String message = messageTextView.getText().toString();
        if (message != null && !message.isEmpty()) {
            ChatMessage chatMessage = new ChatMessage(now,
                    message, null, sender);
            FirebaseDatabase.getInstance().getReference()
                    .push()
                    .setValue(chatMessage);
            messageTextView.setText("");
        } else {
            Snackbar.make(findViewById(R.id.main_activity_layout),
                    getString(R.string.empty_message_error), Snackbar.LENGTH_SHORT).show();
        }
    }
}

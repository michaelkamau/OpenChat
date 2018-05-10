package mikekamau.com.openchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int REQUEST_CODE_SIGN_IN = 1001;

    // Views
    private TextView openChatHeader;
    private TextView openChatSubHeader;
    private ImageView openChatLogo;
    private Button googleSignInBtn;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initSignInViews();

        configureGoogleSignIn();
    }

    private void initSignInViews() {
        openChatHeader = findViewById(R.id.tv_open_chat_header);
        openChatSubHeader = findViewById(R.id.tv_open_chat_subeader);
        openChatLogo = findViewById(R.id.iv_open_chat_logo);
        googleSignInBtn = findViewById(R.id.btn_google_sign_in);
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions googleSignInOptions
                = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void signIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult();
            authenticateWithFirebase(account);
        }
    }

    private void authenticateWithFirebase(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // user signed in
                            Log.d(TAG, "User Authenticated; Name: " +
                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                            Snackbar.make(findViewById(R.id.activity_sign_in),
                                    "Authentication successfully via Google",
                                    Snackbar.LENGTH_LONG).show();
                        } else {
                            Log.d(TAG, "authenticateWithFirebase failed", task.getException());
                            Snackbar.make(findViewById(R.id.activity_sign_in),
                                    "Authentication via Google failed",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.btn_google_sign_in) {
            signIn();
        }
    }
}

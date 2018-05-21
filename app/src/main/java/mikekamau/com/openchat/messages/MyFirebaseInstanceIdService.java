package mikekamau.com.openchat.messages;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFBInstanceIdService";
    private static final String FCM_REG_TOKENS = "fcm_registration_tokens";

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    public void onTokenRefresh() {
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "This device updated token: " + refreshedToken);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Query checkForExistingRegToken = databaseReference.child(FCM_REG_TOKENS)
                    .orderByValue()
                    .equalTo(refreshedToken);
            checkForExistingRegToken.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!(dataSnapshot.getChildren()).iterator().hasNext()) {
                        sendRegistrationTokenToServer(refreshedToken);
                    } else {
                        Log.d(TAG, "This device reg token is already known.");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Error saving registration token to realtime database " +
                            databaseError.toException());
                }
            });

        } else {
            Log.d(TAG, "User not authenticated. No token send.");
        }
    }

    private void sendRegistrationTokenToServer(final String refreshedToken) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(FCM_REG_TOKENS)
                .push()
                .setValue(refreshedToken);
        Log.d(TAG, "sendRegistrationTokenToServer: NEW TOKEN: " + refreshedToken);
    }

}

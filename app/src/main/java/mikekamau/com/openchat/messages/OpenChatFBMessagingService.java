package mikekamau.com.openchat.messages;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class OpenChatFBMessagingService extends FirebaseMessagingService {
    private static final String TAG = "OpenChatFBMessaging";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: Message ID: " + remoteMessage.getMessageId());
        Log.d(TAG, "onMessageReceived: Payload size: " + remoteMessage.getData().size());
        Log.d(TAG, "onMessageReceived: Notification Message: " + remoteMessage.getNotification());
        Log.d(TAG, "onMessageReceived: Data Received: " + remoteMessage.getData());
    }
}

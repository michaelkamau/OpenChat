package mikekamau.com.openchat.messages;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import mikekamau.com.openchat.R;
import mikekamau.com.openchat.entities.ChatMessage;

public class FirebaseDBUtils {
    private static final int MESSAGE_TYPE_SENT = 1;
    private static final int MESSAGE_TYPE_RECEIVED = 2;

    private static final String TAG = "FirebaseDBUtils";
    public static final String MESSAGES_CHILD = "messages";

    public static FirebaseRecyclerAdapter getConfiguredFirebaseAdapter(
            final DatabaseReference firebaseDatabaseRef,
            final FirebaseUser firebaseUser) {

        FirebaseRecyclerOptions<ChatMessage> options =
                new FirebaseRecyclerOptions.Builder<ChatMessage>()
                        .setQuery(firebaseDatabaseRef.child(MESSAGES_CHILD), ChatMessage.class)
                        .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ChatMessage, RecyclerView.ViewHolder>(options) {

                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                      int viewType) {
                        if (MESSAGE_TYPE_SENT == viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.item_sent_message, parent, false);
                            return new SentMessageViewHolder(view);
                        } else if (MESSAGE_TYPE_RECEIVED == viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.item_received_message, parent, false);
                            return new ReceivedMessageViewHolder(view);
                        }
                        return null;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                                    int position, @NonNull ChatMessage chatMessage) {
                        if (MESSAGE_TYPE_SENT == getItemViewType(position)) {
                            // bind using sent message VH
                            SentMessageViewHolder sentMessageVH = (SentMessageViewHolder) holder;
                            sentMessageVH.bind(chatMessage);
                        } else if (MESSAGE_TYPE_RECEIVED == getItemViewType(position)) {
                            // bind using received message VH
                            ReceivedMessageViewHolder receivedMessageVH = (ReceivedMessageViewHolder) holder;
                            receivedMessageVH.bind(chatMessage);
                        }
                    }

                    @Override
                    public int getItemViewType(int position) {
                        ChatMessage chatMessage = getItem(position);
                        final String userId = chatMessage.getSender().getId();
                        Log.d(TAG, "GET_ID: id  = " + userId);
                        Log.d(TAG, "FIREBASE ID: id = " + firebaseUser.getUid());
                        if (firebaseUser.getUid().equals(userId)) {
                            return MESSAGE_TYPE_SENT;
                        } else {
                            return MESSAGE_TYPE_RECEIVED;
                        }
                    }
                };
        return firebaseRecyclerAdapter;
    }

    /**
     * Ensures that the RecyclerView scrolls to the last item once the adapter has more items than
     * can fit the screen.
     *
     * @param recyclerAdapter
     * @param recyclerView
     * @param linearLayoutManager
     */
    public static void adjustRVToViewLatestItems(
            final FirebaseRecyclerAdapter recyclerAdapter,
            final RecyclerView recyclerView,
            final LinearLayoutManager linearLayoutManager) {
        recyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = recyclerAdapter.getItemCount();
                int lastVisiblePosition =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });
    }


    public static FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}

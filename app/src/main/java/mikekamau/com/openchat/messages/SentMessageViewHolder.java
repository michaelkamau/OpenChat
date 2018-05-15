package mikekamau.com.openchat.messages;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import mikekamau.com.openchat.R;
import mikekamau.com.openchat.entities.ChatMessage;

public class SentMessageViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "SentMessageViewHolder";

    private TextView sentMessageTextView;
    private TextView sentTimeTextView;

    public SentMessageViewHolder(View itemView) {
        super(itemView);
        sentMessageTextView = itemView.findViewById(R.id.tv_sent_message);
        sentTimeTextView = itemView.findViewById(R.id.tv_sent_time);
    }

    public void bind(final ChatMessage chatMessage) {
        String message = chatMessage.getMessage();
        if (message != null && !message.isEmpty()) {
            sentTimeTextView.setText(message);
            String timeSent = chatMessage.getTimestamp();
            //TODO: Create a TimeUtils class for time conversions
            sentTimeTextView.setText(timeSent);
        } else {
            Log.d(TAG, "Invalid message");
        }
    }
}

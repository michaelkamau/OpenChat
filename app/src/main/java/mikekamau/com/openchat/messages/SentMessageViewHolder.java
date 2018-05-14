package mikekamau.com.openchat.messages;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import mikekamau.com.openchat.R;

public class SentMessageViewHolder extends RecyclerView.ViewHolder {

    private TextView sentMessageTextView;
    private TextView sentTimeTextView;

    public SentMessageViewHolder(View itemView) {
        super(itemView);
        sentMessageTextView = itemView.findViewById(R.id.tv_sent_message);
        sentTimeTextView = itemView.findViewById(R.id.tv_sent_time);
    }
}

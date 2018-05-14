package mikekamau.com.openchat.messages;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import mikekamau.com.openchat.R;

public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView profilePic;
    private TextView receivedMessage;
    private TextView senderName;
    private TextView receivedTime;

    public ReceivedMessageViewHolder(View itemView) {
        super(itemView);

        profilePic = itemView.findViewById(R.id.iv_received_profile_pic);
        receivedMessage = itemView.findViewById(R.id.tv_received_message);
        senderName = itemView.findViewById(R.id.tv_sender_name);
        receivedTime = itemView.findViewById(R.id.tv_time_message_received);
    }
}

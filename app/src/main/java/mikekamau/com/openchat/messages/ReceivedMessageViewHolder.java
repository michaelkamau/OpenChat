package mikekamau.com.openchat.messages;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import mikekamau.com.openchat.R;
import mikekamau.com.openchat.entities.ChatMessage;
import mikekamau.com.openchat.utils.TimeUtils;

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

    public void bind(final ChatMessage chatMessage) {
        String message = chatMessage.getMessage();
        String time = TimeUtils.getTimeFromTimestamp(chatMessage.getTimestamp());
        String name = chatMessage.getSender().getName();
        String profileUrlStr = chatMessage.getSender().getProfilePicUrlString();

        // TODO: Provide alternative profile icon in the event profileUrlStr is null
        Glide.with(profilePic.getContext())
                .load(profileUrlStr)
                .into(profilePic);
        senderName.setText(name);
        receivedMessage.setText(message);
        // TODO: Provide proper time formatting using some Time utils methods
        receivedTime.setText(time);
    }
}

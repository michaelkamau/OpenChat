package mikekamau.com.openchat.entities;

public class ChatMessage {
    private String timestamp;
    private String message;
    private String fileAttachmentUrl;
    private User sender;

    public ChatMessage() {
    }

    public ChatMessage(String timestamp, String message, String fileAttachmentUrl, User sender) {
        this.timestamp = timestamp;
        this.message = message;
        this.fileAttachmentUrl = fileAttachmentUrl;
        this.sender = sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileAttachmentUrl() {
        return fileAttachmentUrl;
    }

    public void setFileAttachmentUrl(String fileAttachmentUrl) {
        this.fileAttachmentUrl = fileAttachmentUrl;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}

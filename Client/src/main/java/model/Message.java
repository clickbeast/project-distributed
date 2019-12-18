package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class Message {
    private String text;
    private int contactId;
    private long timeStamp;
    private boolean fromUser;
    private boolean delivered;
    private boolean seen;
    private int messageId;

    public Message(String text, int contactId, long timeStamp, boolean fromUser, boolean delivered, boolean seen) {
        this.text = text;
        this.contactId = contactId;
        this.timeStamp = timeStamp;
        this.fromUser = fromUser;
        this.delivered = delivered;
        this.seen = seen;
    }

    public Message(int messageID, String text, int convoId, long messageDate, boolean fromUser, boolean delivered,
                   boolean seen) {
        this.messageId = messageID;
        this.text = text;
        this.contactId = convoId;
        this.timeStamp = messageDate;
        this.fromUser = fromUser;
        this.delivered = delivered;
        this.seen = seen;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isFromUser() {
        return fromUser;
    }

    public void setFromUser(boolean fromUser) {
        this.fromUser = fromUser;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getFormattedTimeStamp() {
        String whoop = String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(this.getTimeStamp()),
                TimeUnit.MILLISECONDS.toSeconds(this.getTimeStamp()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.getTimeStamp()))
        );

        return (new SimpleDateFormat("HH:mm")).format(new Date(this.getTimeStamp()));

    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", contactId=" + contactId +
                ", timeStamp=" + timeStamp +
                ", fromUser=" + fromUser +
                ", delivered=" + delivered +
                '}';
    }

    public void setMessageId(int messageId) {

        this.messageId = messageId;
    }

    public int getMessageId() {
        return messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return messageId == message.messageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }
}

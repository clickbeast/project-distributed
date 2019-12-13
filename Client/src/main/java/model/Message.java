package model;

import java.util.concurrent.TimeUnit;

public class Message {
    private String text;
    private int contactId;
    private long timeStamp;
    private boolean fromUser;
    private boolean delivered;
    private boolean seen;

    public Message(String text, int contactId, long timeStamp, boolean fromUser, boolean delivered,boolean seen) {
        this.text = text;
        this.contactId = contactId;
        this.timeStamp = timeStamp;
        this.fromUser = fromUser;
        this.delivered = delivered;
        this.seen = seen;
    }


    //TODO: remove this, currently just some temp for merge...
    public Message(String hello_world, int i, boolean b, int i1) {
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
        String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(this.getTimeStamp()),
                TimeUnit.MILLISECONDS.toSeconds(this.getTimeStamp()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.getTimeStamp()))
        );
        return "";
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

}

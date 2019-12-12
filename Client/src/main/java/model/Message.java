package model;

public class Message {
    private String text;
    private int userId;
    private long timeStamp;
    private boolean fromUser;
    private boolean delivered;

    public Message(String text, int userId, long timeStamp, boolean fromUser, boolean delivered) {
        this.text = text;
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.fromUser = fromUser;
        this.delivered = delivered;
    }


    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", userId=" + userId +
                ", timeStamp=" + timeStamp +
                ", fromUser=" + fromUser +
                ", delivered=" + delivered +
                '}';
    }

}

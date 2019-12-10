package model;

public class Message {
    private String text;
    private int userId;
    private long timeStamp;

    public Message(String text, int userId, long timeStamp) {
        this.text = text;
        this.userId = userId;
        this.timeStamp = timeStamp;
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

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", userId=" + userId +
                ", timeStamp=" + timeStamp +
                '}';
    }
}

package model;

import javafx.collections.ObservableList;

import java.util.Objects;

public class Conversation {

    private int userId;
    private String userName;
    private BoardKey boardKey;
    private BoardKey boardKeyUs;
    private boolean read;

    private ObservableList<Message> messages;

    public Conversation(int userId, String userName, BoardKey boardKey, BoardKey boardKeyUs,
                        ObservableList<Message> messages) {
        this.userId = userId;
        this.userName = userName;
        this.boardKey = boardKey;
        this.boardKeyUs = boardKeyUs;
        this.messages = messages;
        this.read = true;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BoardKey getBoardKey() {
        return boardKey;
    }

    public void setBoardKey(BoardKey boardKey) {
        this.boardKey = boardKey;
    }

    public BoardKey getBoardKeyUs() {
        return boardKeyUs;
    }

    public void setBoardKeyUs(BoardKey boardKeyUs) {
        this.boardKeyUs = boardKeyUs;
    }

    public ObservableList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ObservableList<Message> messages) {
        this.messages = messages;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}

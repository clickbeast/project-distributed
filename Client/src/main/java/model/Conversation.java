package model;

public class Conversation {
    private int userId;
    private String contactname;
    private BoardKey boardKey;
    private BoardKey boardKeyUs;

    public Conversation(int userId, String contactname, BoardKey boardKey, BoardKey boardKeyUs) {
        this.userId = userId;
        this.contactname = contactname;
        this.boardKey = boardKey;
        this.boardKeyUs = boardKeyUs;
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

    @Override
    public String toString() {
        return "Conversation{" +
                "userId=" + userId +
                ", boardKey=" + boardKey +
                ", boardKeyUs=" + boardKeyUs +
                '}';
    }
}

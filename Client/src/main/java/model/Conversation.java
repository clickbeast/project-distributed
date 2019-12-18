package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Conversation {
    private int contactId;
    private int userId;
    private String userName;
    private BoardKey boardKey;
    private BoardKey boardKeyUs;
    private boolean read;

    private ObservableList<Message> messages;

    public Conversation(int contactId, int userId, String userName, BoardKey boardKey, BoardKey boardKeyUs,
                        ObservableList<Message> messages) {
        this.contactId = contactId;
        this.userId = userId;
        this.userName = userName;
        this.boardKey = boardKey;
        this.boardKeyUs = boardKeyUs;
        this.messages = messages;
        if (messages == null) {
            this.messages = FXCollections.observableArrayList();
        }
        this.read = true;
    }

    public Conversation(String name, int bound) {
        //TODO: Check if getting bound wouldn't add to much delay
        this.userName = name;
        Random random = new Random();
        this.boardKey = new BoardKey(random.nextInt(bound));
        this.boardKeyUs = new BoardKey(random.nextInt(bound));
        if (messages == null) {
            this.messages = FXCollections.observableArrayList();
        }
    }

    public Conversation(String name, File location) throws FileNotFoundException {
        this.userName = name;
        this.initializeBoardKeysFromFile(location);
        if (messages == null) {
            this.messages = FXCollections.observableArrayList();
        }
    }

    public void initializeBoardKeysFromFile(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        String line = null;
        for (int i = 0; i < 2; i++) {
            line = sc.nextLine();
            String[] linesplit = line.split(",");
            if (i == 0) {
                this.boardKeyUs = new BoardKey(linesplit[0], linesplit[1], Integer.parseInt(linesplit[2]));
            } else {
                this.boardKey = new BoardKey(linesplit[0], linesplit[1], Integer.parseInt(linesplit[2]));
            }
        }

    }

    public void writeBoardKeysToFile(File file) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(this.boardKey.toFileString() + System.lineSeparator());
        bufferedWriter.flush();
        bufferedWriter.write(this.boardKeyUs.toFileString() + System.lineSeparator());
        bufferedWriter.flush();


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
    public String toString() {
        return "Conversation{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", boardKey=" + boardKey +
                ", boardKeyUs=" + boardKeyUs +
                ", read=" + read +
                ", messages=" + messages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return contactId == that.contactId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactId);
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getContactId() {
        return this.contactId;
    }

    public void addMessage(Message message) {
        if (!messages.contains(message)) {
            messages.add(message);
        }
    }
}

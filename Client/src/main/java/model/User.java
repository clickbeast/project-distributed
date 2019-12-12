package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    List<Conversation> conversations;

    public User(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public User() {
        conversations = new ArrayList<>();
    }

}

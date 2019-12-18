
import exceptions.AccountAlreadyExistsException;
import interfaces.ThreadListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Conversation;
import model.Message;
import ui.Feedback;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.function.Consumer;

public class ClientManager {


    private MainWindowViewController mainWindowViewController;
    Conversation currentConversation;
    ObservableList<Conversation> conversations;
    private LocalStorageManager localStorageManager;
    private MessageManager messageManager;

    private int userID = -1;

    /**
     * Creates a dummy conversation used for UI testing
     *
     * @param name
     * @return
     */
    public Conversation conversationDummy(String name) {
        //TODO: not working now

        Message message1 = new Message(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore " +
                        "et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
                        "nisi ut aliquip ex ea commodo consequat.",
                0
                , System.currentTimeMillis(),
                true
                , true
                , true);

        Message message2 = new Message("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud " +
                "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 0,
                System.currentTimeMillis(), false, true, true);

        ObservableList<Message> messages = FXCollections.observableArrayList();
        messages.add(message1);
        messages.add(message2);

        Conversation conversation = new Conversation(99999999, userID, name, null, null, messages);

        return conversation;

    }

    /* LOADING ------------------------------------------------------------------ */

    public LocalStorageManager prepareLocalStorage() {
        String path = "/home/adegeter/test/testdb.db";
        return new LocalStorageManager(path);
    }

    public ClientManager() {
        this.setLocalStorageManager(prepareLocalStorage());
        this.localStorageManager.createDatabase();

        conversations = FXCollections.observableArrayList();
        messageManager = new MessageManager();
        ThreadListener listener = new ThreadListener() {

            @Override
            public void threadFinished() {
                System.err.println("You shouldn't be here");
            }

            @Override
            public void newMessage(Message message, Conversation conversation) {
                Platform.runLater(() -> messageReceived(conversation, message));


            }
        };

        messageManager.getMessages(listener);
        /*Conversation conversation = conversationDummy("Vincent");
        Conversation conversation1 = conversationDummy("Greet");
        Conversation conversation2 = conversationDummy("Stijn");

        this.conversations.add(conversation);
        this.conversations.add(conversation1);
        this.conversations.add(conversation1);
        this.conversations.add(conversation2);*/


    }

    public void loadUserContents(int userId) {
        //get current conversation
     //   conversations.clear();
        conversations.addAll(localStorageManager.getConversations(userId));
        for (Conversation conversation : conversations) {
            for (Message message : localStorageManager.getMessagesFromConvoId(conversation.getContactId())) {
                conversation.addMessage(message);
            }
        }
        if (conversations.isEmpty()) {
            this.mainWindowViewController.loadEmptyConversation();
        } else {
            this.currentConversation = this.getConversations().get(0);
        }

        this.mainWindowViewController.loadApplicationView();
        this.mainWindowViewController.loadInbox();
        this.mainWindowViewController.loadConversation(getCurrentConversation());
    }

    public void sortConversationsAccordingToPolicy() {
        //TODO:

    }


    /* ACTIONS ------------------------------------------------------------------ */

    public void login(String username, String password, Consumer<Feedback> callback) {
        System.out.println("Logging in");
        userID = localStorageManager.login(username, password);
        System.out.println(userID);
        if (userID == -1) {
            callback.accept(new Feedback(false, "Login failed please try again"));
        } else {
            callback.accept(new Feedback(true, "Login success"));
            this.loadUserContents(userID);
        }
        //this.loadUserContents(0);
    }

    public void logout() {
        System.out.println("Logging out");
        this.mainWindowViewController.loadEmptyConversation();
        this.mainWindowViewController.freezeUI();
        this.conversations = FXCollections.observableArrayList();
        this.currentConversation = null;
        messageManager.clearConversations();

        this.mainWindowViewController.loadLoginView();
    }

    public void createAccount(String username, String password, File directoryLocation) {
        System.out.println("Creating account");
//        localStorageManager.setPath(directoryLocation.getPath());
        try {
            localStorageManager.addAccount(username, password, "");
        } catch (AccountAlreadyExistsException e) {
            this.mainWindowViewController.loginViewController.createAccountInfoLabel.setText("Account with login " + username + " already exists.");
            return;
        }


        this.login(username, password, b -> {
            if (!b.isSucces()) {
                this.mainWindowViewController.loginViewController.createAccountInfoLabel.setText("Login failed -> try" +
                        " to login");
            }
        });
    }

    public void addNewConversation(String name, File location) {
        //create conversation
        System.out.println("userid: " + userID);

        Conversation conversation = null;
        try {
            conversation = new Conversation(name, location);
        } catch (FileNotFoundException e) {
            //TODO: @simon give notification if wrong file?
        }
        if (conversation != null) {
            conversation.setUserId(userID);
            localStorageManager.saveConversation(conversation);
            conversations.add(conversation);
            messageManager.addConversation(conversation);

            this.mainWindowViewController.loadConversation(conversation);
        }
    }

    public void createNewConversation(String name) {
        System.out.println(userID);


        Conversation c = new Conversation(name, messageManager.getLastBound());
        localStorageManager.initializeConversationsDatabase();
        c.setUserId(userID);

        int id = localStorageManager.saveConversation(c);
        if (id != -1) {
            c.setContactId(id);
        }
        conversations.add(c);
        messageManager.addConversation(c);

        this.mainWindowViewController.loadConversation(c);

    }

    public void sendMessage(Conversation conversation, String text) {
        Message message = new Message(text, conversation.getContactId(), System.currentTimeMillis(), true, false, true);
        message.setMessageId(localStorageManager.storeMessage(message));
        conversation.getMessages().add(message);

        ThreadListener listener = new ThreadListener() {
            @Override
            public void threadFinished() {
                Platform.runLater(() -> messageDelivered(message, conversation));

            }
            @Override
            public void newMessage(Message message, Conversation conversation) {
                System.err.println("You shouldn't be here");
            }
        };
        messageManager.sendMessage(conversation, message, listener);

        this.mainWindowViewController.messageField.setText("");

        //TODO:@simon CALLBACK MECHANISM
        this.mainWindowViewController.reloadUI();

    }

    public void deleteConversation(Conversation conversation) {
        this.conversations.remove(conversation);
        localStorageManager.deleteConversation(conversation);
        //NOTE: This will make the thread not check anymore for new, I assume that's enough
        messageManager.removeConversation(conversation);
        this.mainWindowViewController.loadEmptyConversation();
    }

    public void editPartnerName(String text) {
        System.out.println("edit partner name");
        if (this.currentConversation != null) {
            localStorageManager.updateConversation(text, this.currentConversation.getContactId());
            this.currentConversation.setUserName(text);
        }
        this.mainWindowViewController.reloadUI();
    }

    /* RESPONSES ------------------------------------------------------------------ */

    //JIIUUUWP
    public synchronized void messageDelivered(Message message, Conversation conversation) {
        message.setContactId(conversation.getContactId());
        conversation.addMessage(message);
        localStorageManager.updateMessage(message);
        System.out.println("MESSAGE DELIVERED");
        this.mainWindowViewController.reloadUI();
        this.loadUserContents(userID);
    }

    //PING
    public synchronized void messageReceived(Conversation conversation, Message message) {
        conversation.addMessage(message);
        message.setContactId(conversation.getContactId());
        localStorageManager.storeMessage(message);

        System.out.println("MESSAGE RECEIVED");
        this.currentConversation = conversation;
        this.mainWindowViewController.reloadUI();

    }

    /*
     * GETTERS & SETTERS
     * -----------------------------------------------------------------------------
     *
     */

    public MainWindowViewController getMainWindowViewController() {
        return mainWindowViewController;
    }

    public void setMainWindowViewController(MainWindowViewController mainWindowViewController) {
        this.mainWindowViewController = mainWindowViewController;
    }

    public ObservableList<Conversation> getConversations() {
        return conversations;
    }


    public void getKeyForConversation(int id, File directoryLocation) {


    }

    public void setCurrentConversation(Conversation currentConversation) {
        this.currentConversation = currentConversation;
    }

    public Conversation getCurrentConversation() {
        return currentConversation;
    }

    public void setConversations(ObservableList<Conversation> conversations) {
        this.conversations = conversations;
    }

    public LocalStorageManager getLocalStorageManager() {
        return localStorageManager;
    }

    public void setLocalStorageManager(LocalStorageManager localStorageManager) {
        this.localStorageManager = localStorageManager;
    }


}
